import React from 'react';
import ReactDOM from 'react-dom';
import registerServiceWorker from './registerServiceWorker';
import Router from './routes.js';
import axios from 'axios';
import './scss/_style.scss';
import './style.css';


if (window.location.hostname === "localhost") {
  axios.defaults.baseURL = 'http://localhost:4567';
} else {
  axios.defaults.baseURL = 'https://api.sabb.review';
}

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
