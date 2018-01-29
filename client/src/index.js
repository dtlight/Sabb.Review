import React from 'react';
import ReactDOM from 'react-dom';
import registerServiceWorker from './registerServiceWorker';
import Router from './routes.js';
import axios from 'axios';
import './scss/_style.scss';
import './style.css';

axios.defaults.baseURL = 'http://localhost:4567';

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
