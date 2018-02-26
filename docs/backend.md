# Backend Notes

[UML for the backend](backendUML.uxf)
(open with UMLET)

## Building & Installing

### Prerequisites
All dependencies are provided by Maven. To build SabbReview, make sure [Maven](https://maven.apache.org/install.html) is installed.

To run the server locally, you'll need the [Heroku CLI tools](https://devcenter.heroku.com/articles/heroku-cli).

### Building & Running

To install the require dependencies run:

     mvn clean install


Two resulting jar files (for the modules 'mail' and 'backend') are created inside 'target' in the respective modules.
(see Procfile for exact paths)

To test the server locally, run:

    heroku local

or you can upload the project to your own heroku instance.

If you're having issues running it on Windows, try replacing the respective lines "*-with-dependencies.jar*" in procfile with the actual file name.


## Configuration

backend.iml is used internally by intellij to make use of maven. You don't need to worry about it.

pom.xml stores dependencies used by maven. If you want to add a dependency, add it here


## Key Dependencies

### Spark

The backend (especially sabbReview.java and the controllers) use [SparkJava](http://sparkjava.com/) to handle the low level HTTP stuff.

Key points:

* [Routes](http://sparkjava.com/documentation#routes)
* [Requests](http://sparkjava.com/documentation#request)
* [Responses](http://sparkjava.com/documentation#response)
* [Filters](http://sparkjava.com/documentation#filters)

### Authentiction: JSON Web Tokens, claims, & principles

We're using [JSON Web Tokens](https://github.com/auth0/java-jwt) to manage API request authentication.

Tokens have an attribute isAuthenticated which is initially set in SabbReview.acceptAuthentication().

### GSON ( & Adapters)

GSON's used to convert back and forth between java objects and JSON.
We've created [Adapters](../backend/src/main/java/com/sabbreview/adapters) to convert our Util [Models](../util/src/main/java/com/sabbreview/model) to JSON so Spark can make use of them.

The adapters extend GSON's [JSONSerializer](https://google.github.io/gson/apidocs/com/google/gson/JsonSerializer.html)
