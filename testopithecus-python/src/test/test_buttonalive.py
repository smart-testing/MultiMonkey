import unittest
import requests
import json

headers = {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
}

config_backup = None


def setup_config(filename):
    data = {'templates': [filename]}
    with open('../../config.json', 'w') as config:
        json.dump(data, config)


def prepare_test(screen_path, config):
    setup_config(config)
    with open(screen_path, 'rb') as sc_path:
        data = sc_path.read()
        response = requests.post('http://127.0.0.1:5000/', headers=headers, data=data)
        input_json: dict = response.json()
        detected: str = input_json["detected"]
        print(f"Got from server: {detected}")
        return detected


class TestButtonAlive(unittest.TestCase):
    """
    Restart 'src/server.py' before each test or set 'cache_scale' argument of 'remove_selected()' in
    'server()' to False.
    """

    @classmethod
    def setUpClass(cls) -> None:
        with open('../../config.json') as config:
            global config_backup
            config_backup = json.load(config)

    @classmethod
    def tearDownClass(cls) -> None:
        global config_backup
        with open('../../config.json', 'w') as config:
            json.dump(config_backup, config)

    def test(self):
        detected = prepare_test('../../src/resources/jsons/test.json', 'Templates/MinimalTodoButton.png')
        self.assertTrue(detected == [8])

    def test_minimaltodo_smallscreen_smalltemplate(self):
        detected = prepare_test('../../src/resources/jsons/minimaltodo_small_main.json',
                                'Templates/template_minimaltodo_small_main.png')
        self.assertTrue(detected == [8])

    def test_minimaltodo_smallscreen_bigtemplate(self):
        detected = prepare_test('../../src/resources/jsons/minimaltodo_small_main.json',
                                'Templates/template_minimaltodo_big_main.png')
        self.assertTrue(detected == [8])

    def test_minimaltodo_bigscreen_bigtemplate(self):
        detected = prepare_test('../../src/resources/jsons/minimaltodo_big_main.json',
                                'Templates/template_minimaltodo_big_main.png')
        self.assertTrue(detected == [8])

    def test_minimaltodo_bigscreen_smalltemplate(self):
        detected = prepare_test('../../src/resources/jsons/minimaltodo_big_main.json',
                                'Templates/template_minimaltodo_small_main.png')
        self.assertTrue(detected == [8])


if __name__ == '__main__':
    unittest.main()
