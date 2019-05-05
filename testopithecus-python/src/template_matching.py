import base64
import cv2
import imutils
import numpy as np
from PIL import Image
from io import BytesIO

#
# visualised = True
# threshold = 0.9
template_locations = ['template_small2.png']


class Rect:
    def __init__(self, top: int, bottom: int, left: int, right: int):
        self.top: int = top
        self.bottom: int = bottom
        self.left: int = left
        self.right: int = right

    def tostring(self):
        return str(self.top) + " " + str(self.bottom) \
               + " " + str(self.left) + " " + str(self.right)


def readb64(base64_string):
    sbuf = BytesIO()
    sbuf.write(base64.b64decode(base64_string))
    pimg = Image.open(sbuf)
    return cv2.cvtColor(np.array(pimg), cv2.COLOR_RGB2BGR)


def compare_rectangles(rect1: Rect, rect2: Rect):
    x_overlap = max(0, min(rect1.right, rect2.right) - max(rect1.left, rect2.left))
    y_overlap = max(0, min(rect1.bottom, rect2.bottom) - max(rect1.top, rect2.top))
    overlapArea = x_overlap * y_overlap
    return overlapArea


def match_template(screenshot, template, visualised=False, threshold=0.9):
    template = cv2.cvtColor(template, cv2.COLOR_BGR2GRAY)
    (tH, tW) = template.shape[:2]
    if visualised: cv2.imshow("Template", template)
    image = screenshot
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    found = None
    for scale in np.linspace(0.2, 1.0, 20)[::-1]:
        resized = imutils.resize(gray, width=int(gray.shape[1] * scale))
        r = gray.shape[1] / float(resized.shape[1])
        if resized.shape[0] < tH or resized.shape[1] < tW:
            break
        edged = resized
        result = cv2.matchTemplate(edged, template, cv2.TM_CCOEFF_NORMED)
        loc = np.where(result >= threshold)
        (_, maxVal, _, maxLoc) = cv2.minMaxLoc(result)
        if maxVal >= threshold and visualised:
            clone = np.dstack([edged, edged, edged])
            cv2.rectangle(clone, (maxLoc[0], maxLoc[1]),
                          (maxLoc[0] + tW, maxLoc[1] + tH), (0, 0, 255), 2)
        if maxVal >= threshold and (found is None or maxVal > found[0]):
            found = (maxVal, maxLoc, r)
    if found is None: return None
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


def satisfies_intersection_threshold(area_in, area_a, area_b):
    satisfies = True
    if area_in / area_a < 0.8: satisfies = False
    if area_in / area_b < 0.8: satisfies = False
    return satisfies


def remove_selected(screenshot_base64: str, elements: list):
    screenshot = readb64(screenshot_base64)
    matched = []
    rectangles = get_rectangles(elements)
    for template_location in template_locations:
        template = cv2.imread(template_location)
        result = match_template(screenshot, template)
        if result is None: continue
        (startX, startY), (endX, endY) = result
        result_rect = Rect(startY, endY, startX, endX)
        for r, e in zip(rectangles, elements):
            area_r = compare_rectangles(r, r)
            area_s = compare_rectangles(result_rect, result_rect)
            area_intersect = compare_rectangles(r, result_rect)
            if satisfies_intersection_threshold(area_intersect, area_r, area_s):
                matched.append(e)
    return list(filter(lambda x: x not in matched, elements))