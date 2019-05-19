from src.template_matching import remove_selected
from src.template_matching import compare_screenshots
from flask import Flask
from flask import request
from flask import jsonify

app = Flask(__name__)


class Element:
    def __init__(self, element: dict):
        self.element = element
        self.top: int = element["top"]
        self.bottom: int = element["bottom"]
        self.left: int = element["left"]
        self.right: int = element["right"]


@app.route("/", methods=['POST'])
def server():
    input_json: dict = request.get_json(cache=False)
    glob: dict = input_json["global"]
    screenshot: str = glob["screenshot"]
    elements_json: list = input_json["elements"]
    if len(elements_json) < 1:
        return jsonify({"detected": []})
    elements: list = list(map(lambda x: Element(x["attributes"]), elements_json))
    filtered = remove_selected(screenshot, elements)
    print(f"Got from client: {len(elements)} elements. Filtered: {len(filtered)}")
    return jsonify({"detected": filtered})


@app.route("/button-alive", methods=['POST'])
def button_alive():
    input_json: dict = request.get_json(cache=False)
    before: str = input_json["before"]
    after: str = input_json["after"]
    equals = compare_screenshots(before, after) != 0
    print(f"Button-alive: {equals}")
    return jsonify({"button-alive": str(int(equals))})


if __name__ == '__main__':
    app.run()
