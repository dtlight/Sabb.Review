import React from 'react';
import ReactDOM from 'react-dom';
import {Link} from 'react-router-dom';

export default class extends React.Component {
  render() {
    return (
      <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
        <Link to="/" className="navbar-brand">
          <img style={{"height": "40px"}} src="https://res.cloudinary.com/postman/image/upload/w_152,h_56,c_fit,f_auto,t_team_logo/v1/team/e4b14c733efa3519b29546c649c4271e6a71ecb5cd8f64876e312e436497cd1e"/>
        </Link>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse " id="navbarSupportedContent">
          <ul class="navbar-nav mr-auto">
            <li class="nav-item active ">
              <a class="nav-link" href="#">Home</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#">Apply</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#">Review</a>
            </li>
          </ul>
          <ul class="navbar-nav ml-auto">
            <li class="nav-item  ">
              <Link to="/logout" class="nav-link">Logout</Link>
            </li>
          </ul>
        </div>
      </nav>
    )
  }
}
