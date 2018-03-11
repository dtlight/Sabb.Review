# Frontend Notes


## Building & Running

### Prerequisites

You need [Node JS](https://nodejs.org/en/) to run an npm server.

### Running

To test the frontend locally, run (inside 'client'):

    npm install
    npm start


## Configuration


You can change which backend the npm server sends API calls to by changing the line inside index.js:

    axios.defaults.baseURL = '';

By default, this is set to the staging server.
