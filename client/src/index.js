import React from 'react';
import ReactDOM from 'react-dom';
import './style.css';
import './vendor/bootstrap.min.css';
import registerServiceWorker from './registerServiceWorker';
import Router from './routes.js';
import axios from 'axios';


// When testing use: axios.defaults.baseURL = 'http://localhost:4567/api';
axios.defaults.baseURL = 'https://api.sabb.review';

axios.interceptors.request.use(function (config) {
  if(window.localStorage.getItem("token")) {
    config.headers['Authorization'] = "Bearer "+window.localStorage.getItem("token");
  }
  return config;
  }, function (error) {
    return Promise.reject(error);
  });

ReactDOM.render(<Router />, document.getElementById('root'));
registerServiceWorker();
