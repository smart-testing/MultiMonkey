curl request to filter UiElements stored in `src/elemenrs2.json` with template `template_small2.png` 

`curl --header "Content-Type: application/json"   --header "Accept: application/json"   --request POST --data-binary  "@elements2.json" http://127.0.0.1:5000/ -i`
