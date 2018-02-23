# SabbReview

A sabbatical period review system for university staff.

## Getting Started

### Frontend

#### Prerequisites

You need [Node JS](https://nodejs.org/en/) to run an npm server.

#### Building & Running

To test the frontend locally, run (inside 'client'):

    npm install
    npm start


You can change which backend the npm server uses by changing the line:

    axios.defaults.baseURL = '';

to whatever server you want.


### Backend

#### Prerequisites
All dependencies are provided by Maven. To build SabbReview, make sure [Maven](https://maven.apache.org/install.html) is installed.

To run the server locally, you'll need the [Heroku CLI tools](https://devcenter.heroku.com/articles/heroku-cli).

#### Building & Running

To install the require dependencies run:

     mvn clean install


Two resulting jar files (for the modules 'mail' and 'backend') are created inside 'target' in the respective modules.
(see Procfile for exact paths)

To test the server locally, run:

    heroku local


If you're having issues running it on Windows, try replacing the respective lines "*-with-dependencies.jar*" in procfile with the actual file name.


## Contributing
All pull requests accepted into master will be automatically deployed.

Pull requests accepted into staging will be deployed to the staging (test) heroku instance.
