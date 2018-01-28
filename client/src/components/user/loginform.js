import React from 'react';
import {Link, Redirect} from 'react-router-dom'
import './style.css'
import axios from 'axios';

export default class Login extends React.Component {

    constructor(props) {
        super(props);
        this.props = props;
        this.state = {
            emailAddress: "",
            password: "",
            isError: false,
            isSuccess: false,
            isLoading: false
        };
        if(props.location && props.location.state && props.location.state.newUser) {
          this.state.emailAddress = props.location.state.newUser;
          this.state.postReg = true;
        } else {
          this.state.emailAddress =  "";
        }
        this.login = this.login.bind(this);
    }

    login() {
      this.setState({
        isError: false,
        isLoading: true
      });
      axios.post(`/login`, {
          emailAddress: this.state.emailAddress,
          password: this.state.password
      })
      .then(function (response) {
        if(response.data.state !== "STATUS_ERROR") {
          window.localStorage.setItem('token', response.data.value.token);
          this.setState({
            isSuccess: true,
            isLoading: false
          });
        } else {
          this.setState({
            isError: true,
            isLoading: false,
            password: ""
          });
        }
        console.log(response);
      }.bind(this));
    }
    render() {
      if(this.state.isSuccess) {
        return (
          <Redirect to="/" />
        )
      } else {
        var buttonContent = (this.state.isLoading)?<i class="fa fa-refresh fa-spin"></i>:"Sign In";
        return (
            <div>
              <div style={{
                "max-width": "380px",
                "padding": "15px",
                "margin": "0 auto"
              }} className="login-form">
                <form onSubmit={(e) => {
                  e.preventDefault();
                  this.login();
                  return false;
                }}>
                  <div className="alert alert-secondary" role="alert" style={{"display": (this.state.postReg)?"block":"none"}}>
                      Please login with your new account
                  </div>
                  <h1 className="display-4" style={{"paddingBottom": "10px"}}>Login</h1>
                  <div class="form-group">
                    <input type="email"
                      className={(this.state.isError)?"form-control is-invalid form-control form-control-lg"
                                :"form-control form-control-lg"}
                      placeholder="Email Address"
                      onChange={(e) => {
                        this.setState({
                          emailAddress: e.target.value
                        })
                      }}
                      value={this.state.emailAddress} />
                    <input type="password"
                      class=""
                      placeholder="Password"
                      className={(this.state.isError)?"form-control is-invalid form-control form-control-lg"
                                :"form-control form-control-lg"}
                      onChange={(e) => {
                        this.setState({
                          password: e.target.value
                        })
                      }}
                      value={this.state.password}/>
                    </div>
                    <button onClick={this.login} value="Sign In" class="btn btn-lg btn-primary btn-block">
                      {
                        buttonContent
                      }
                    </button>
                    <Link to="/auth/register" class="btn btn-lg btn-link btn-block">or Register</Link>
                    </form>
                  </div>
                </div>);
              }
      }
}
