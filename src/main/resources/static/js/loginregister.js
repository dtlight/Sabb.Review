$(document).ready(function () {

    $("#formregister").submit(function (e) {
        e.preventDefault();
        var email = $("#registeremail").val();
        var password = $("#registerpassword").val();
        var passwordc = $("#registerpasswordc").val();
        if (password === passwordc) {
            var settings = {
                "async": true,
                "url": "/api/user",
                "method": "POST",
                "headers": {
                    "Content-Type": "application/json"
                },
                "processData": false,
                "data": JSON.stringify({
                    emailAddress: email, password: password
                })
            };
            $.ajax(settings).done(function (response) {
                console.log(response);
            });
        }
        else {
            window.alert("Your passwords do not match");
            formregister.reset();
        }
        return false;
    });

    $("#formlogin").submit(function (e) {
        e.preventDefault();
        var email = $("#email").val();
        var password = $("#password").val();
        var settings = {
            "async": true,
            "crossDomain": true,
            "url": "/api/login?emailAddress="+email+"&password="+password,
            "method": "POST",
            "headers": {}
        }

        $.ajax(settings).done(function (response) {
            console.log(response);
            console.log(response);
            console.log(response.state);
            if (response.state.trim() == "STATUS_OK") {
                window.localStorage.setItem('token', response.value.token);
                window.location.replace("/portal.html");
            } else {
                formlogin.reset();
            }
        });
    });
});
