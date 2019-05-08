# from src.template_matching import remove_selected
from src.template_matching import remove_selected
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
        return jsonify()
    elements: list = list(map(lambda x: Element(x["attributes"]), elements_json))
    # todo отправлять только найденные
    filtered = remove_selected(screenshot, elements)
    print('Got from client: ' + str(len(elements)) + ' elements. Filtered: ' + str(len(filtered)))
    return jsonify(filtered)


if __name__ == '__main__':
    app.run()
