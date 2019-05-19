import unittest
import requests

headers = {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
}


class TestButtonAlive(unittest.TestCase):

    def test(self):
        data = open('../../src/test.json', 'rb').read()
        response = requests.post('http://127.0.0.1:5000/', headers=headers, data=data)
        input_json: dict = response.json()
        detected: str = input_json["detected"]
        print(f"Got from server: {detected}")
        self.assertTrue(detected == [8])


if __name__ == '__main__':
    unittest.main()
