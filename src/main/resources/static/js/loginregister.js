$(document).ready(function()){
  $("#REGISTER").click(function()){
    var email = $("#registeremail");
    var password = $("#registerpassword");
    var passwordc = $("#registerpasswordc");
    if (password = passwordc){
    var settings = {
    "async": true,
    "crossDomain": true,
    "url": "http://{{host}}:{{port}}/api/user",
    "method": "POST",
    "headers": {
      "Content-Type": "application/json"
    },
    "processData": false,
    "data": "{\n  \"emailAddress\": \email\,\n  \"password\": \password\\n}"
    }

    $.ajax(settings).done(function (response) {
      console.log(response);
    });
  }
  else {
    window.alert("Your passwords do not match");
    formregister.reset();
  }
  }

  $("#LOGIN").click(function()){
    var email = $("#email");
    var password = $("#password");
    var settings = {
    "async": true,
    "crossDomain": true,
    "url": "http://{{host}}:{{port}}/api/user/{{user_emailaddress}}",
    "method": "GET",
    "headers": {}
    }

    $.ajax(settings).done(function (response) {
      console.log(response);
      var responseemail = response.value.emailAddress;

    });
  }
}
