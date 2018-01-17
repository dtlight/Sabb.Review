$(document).ready(function(){
  $("#REGISTER").click(function(){
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
});

  $("#LOGIN").click(function(){
      var email = $("#email");
      var password = $("#password");
      var responseemail = "";
      var responsepassword = "";
      var settings = {
      "async": true,
      "crossDomain": true,
      "url": "http://{{host}}:{{port}}/api/user/{{email}}",
      "method": "GET",
      "headers": {}
    }

    $.ajax(settings).done(function (response) {
      console.log(response);
      if (response.state == "STATE_ERROR"){
        window.alert("Email or password aren't correct");
        formlogin.reset();
      } else {
        responseemail = response.value.emailAddress;
        responsepassword = response.value.password;
      }
    });

    if (password != responsepassword){
      window.alert("Email or password aren't correct");
      formlogin.reset();
    }
    else {
      window.alert("Logged in!");
      if (localStorage.useremail == email) {
        console.log("there is a session open");
      } else {
        localStorage.useremail = email;
        console.log("created local storage for email");
      }

    }
  });
});
