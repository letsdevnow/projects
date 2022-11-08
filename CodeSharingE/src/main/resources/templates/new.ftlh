<html>
<head>
    <title>Create</title>
    <link rel="stylesheet" type="text/css" href="/styles.css" />
</head>
<body>
    <textarea id="code_snippet" cols="100" rows="10"></textarea>
    <p>Test</p>
    <button id="send_snippet" type="submit" onclick="send()">Submit</button>

    <script lang="JavaScript">
    <!--
    function send() {
    let object = {
        "code": document.getElementById("code_snippet").value
    };

    let json = JSON.stringify(object);

    let xhr = new XMLHttpRequest();
    xhr.open("POST", '/api/code/new', false)
    xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
    xhr.send(json);

    if (xhr.status == 200) {
      alert("Success!");
    } else {
        alert("Does not work, code: " + xhr.status);
        }
    }
    -->
    </script>

</body>
</html>
