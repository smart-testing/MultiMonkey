#from src.template_matching import remove_selected
from template_matching import remove_selected
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
    screenshot: str = input_json["screenshot"]
    elements_json: list = input_json["elements"]
    if len(elements_json) < 1:
        return jsonify()
    elements: list = list(map(lambda x: Element(x), elements_json))
    # todo отправлять только найденные
    filtered_tmp = remove_selected(screenshot, elements)
    filtered = list(map(lambda x: x.element, filtered_tmp))
    print('Got from client: ' + str(len(elements)) + ' elements. Filtered: ' + str(len(elements) - len(filtered)))
    return jsonify(filtered)


if __name__ == '__main__':
    app.run()
