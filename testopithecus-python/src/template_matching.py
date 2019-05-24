import base64
import cv2
import imutils
import numpy as np
import json
import matplotlib.pyplot as plt
from PIL import Image
from io import BytesIO

#
# visualised = True
rect_threshold = 0.9
template_locations = []
global_scale = None


def update_template_locations():
    with open('../config.json') as config_file:
        config = json.load(config_file)
        global template_locations
        template_locations = config["templates"]


class Rect:
    def __init__(self, top: int, bottom: int, left: int, right: int):
        self.top: int = top
        self.bottom: int = bottom
        self.left: int = left
        self.right: int = right

    def __str__(self):
        return f"{self.top} {self.bottom} {self.left} {self.right}"


def readb64(base64_string):
    sbuf = BytesIO()
    sbuf.write(base64.b64decode(base64_string))
    pimg = Image.open(sbuf)
    cv2.imwrite('android_screenshot.png', np.array(pimg))
    return cv2.cvtColor(np.array(pimg), cv2.COLOR_RGB2BGR)


def compare_screenshots(before: str, after: str):
    before = readb64(before)
    after = readb64(after)
    return np.array_equal(before, after)


def compare_rectangles(rect1: Rect, rect2: Rect):
    x_overlap = max(0, min(rect1.right, rect2.right) - max(rect1.left, rect2.left))
    y_overlap = max(0, min(rect1.bottom, rect2.bottom) - max(rect1.top, rect2.top))
    overlapArea = x_overlap * y_overlap
    return overlapArea


def draw_image(img, scheme='', label='', visualised=True, filename=None):
    if not visualised:
        return
    img_rgb = img.copy()
    # if scheme == 'gray':
    #     img_rgb = cv2.cvtColor(img_rgb, cv2.COLOR_GRAY2RGB)
    # elif scheme == 'BGR':
    #     img_rgb = cv2.cvtColor(img_rgb, cv2.COLOR_BGR2RGB)
    if filename is not None:
        plt.imsave(filename, img_rgb)
        return
    plt.imshow(img_rgb)


def match_template(screenshot, template, visualised=False, threshold=0.9, cache_scale=True):
    save_folder = 'result_images/'
    template = cv2.cvtColor(template, cv2.COLOR_BGR2GRAY)
    draw_image(template, 'gray', visualised=visualised, filename=save_folder + 'template.png')
    (tH, tW) = template.shape[:2]
    # if visualised:
    # cv2.imshow("Template", template)
    image = screenshot
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    found = None
    best_scale = -1
    global global_scale
    if cache_scale and (global_scale is not None):
        scales = np.array([global_scale])
    else:
        scales = np.linspace(0.2, 2, 20)
        scales = np.append(scales, 1)
    for scale in scales[::-1]:
        resized = imutils.resize(gray, width=int(gray.shape[1] * scale))
        r = gray.shape[1] / float(resized.shape[1])
        if resized.shape[0] < tH or resized.shape[1] < tW:
            break
        edged = resized
        result = cv2.matchTemplate(edged, template, cv2.TM_CCOEFF_NORMED)
        # loc = np.where(result >= threshold)
        (_, maxVal, _, maxLoc) = cv2.minMaxLoc(result)
        if visualised:
            # if maxVal >= threshold and visualised:
            clone = np.dstack([edged, edged, edged])
            cv2.rectangle(clone, (maxLoc[0], maxLoc[1]),
                          (maxLoc[0] + tW, maxLoc[1] + tH), (0, 0, 255), 2)
            draw_image(clone, 'gray', visualised=True, filename=f"{save_folder}rect_{scale}.png")
        print(maxVal)
        if maxVal >= threshold and (found is None or maxVal > found[0]):
            best_scale = scale
            found = (maxVal, maxLoc, r)
    if found is None:
        return None
    if cache_scale:
        global_scale = best_scale
    print(f'Best scale found is: {best_scale}')
    (_, maxLoc, r) = found
    (startX, startY) = (int(maxLoc[0] * r), int(maxLoc[1] * r))
    (endX, endY) = (int((maxLoc[0] + tW) * r), int((maxLoc[1] + tH) * r))
    return (startX, startY), (endX, endY)


def get_rectangles(elements: list):
    rectangles = []
    for e in elements:
        rect = Rect(int(e.top), int(e.bottom), int(e.left), int(e.right))
        rectangles.append(rect)
    return rectangles


def satisfies_intersection_threshold(area_in, area_a, area_b, threshold=0.55):
    return area_in / area_a >= 0.9 and area_in / area_b >= threshold


def remove_selected(screenshot_base64: str, elements: list, cache_scale=True):
    screenshot = readb64(screenshot_base64)
    matched = []
    rectangles = get_rectangles(elements)
    update_template_locations()
    for template_location in template_locations:
        template = cv2.imread(template_location)
        assert template is not None
        result = match_template(screenshot, template, visualised=True, cache_scale=cache_scale)
        if result is None:
            continue
        (startX, startY), (endX, endY) = result
        result_rect = Rect(startY, endY, startX, endX)
        for r, e in zip(rectangles, elements):
            area_ui_rectangle = compare_rectangles(r, r)
            area_cv_rectangle = compare_rectangles(result_rect, result_rect)
            area_intersect = compare_rectangles(r, result_rect)
            if satisfies_intersection_threshold(area_intersect, area_ui_rectangle, area_cv_rectangle):
                matched.append(e)
    return [ind for ind, _ in list(filter(lambda x: x[1] in matched, enumerate(elements)))]
