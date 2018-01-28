import React from 'react';
import './style.css'
import axios from 'axios';
import {Link, Redirect} from 'react-router-dom'

export default class Register extends React.Component {

  constructor(props) {
      super(props);
      this.props = props;
      this.state = {
        emailAddress: "",
        password: "",
        repeatPassword: "",
        isError: false,
        isSuccess: false,
        isLoading: false
      }
      this.register = this.register.bind(this);
  }

  register() {
    this.setState({
      isError: false,
      isLoading: true
    });
    if(this.state.password === this.state.repeatPassword) {
      axios.post('/user', {
        emailAddress: this.state.emailAddress,
        password: this.state.password
      })
      .then(function (response) {
        if(response.data.state !== "STATUS_ERROR") {
          this.setState({
            isSuccess: true,
            isLoading: false
          })
        } else {
          this.setState({
            isError: true,
            isLoading: false,
            emailAddress: "",
            password: "",
            repeatPassword: ""
          });
        }
        console.log(response);
      }.bind(this));
    } else {
      this.setState({
        isError: true,
        isLoading: false
      })
    }
  }


    render() {
      var buttonContent = (this.state.isLoading)?<i class="fa fa-refresh fa-spin"></i>:"Register";

      if(this.state.isSuccess) {
        return (
          <Redirect to={{
              pathname: './',
              state: { newUser: this.state.emailAddress }
            }} />
        )
      } else {
        return (
            <div>
              <div style={{
                "max-width": "380px",
                "padding": "15px",
                "margin": "0 auto"
              }} className="register-form">
                <form onSubmit={(e) => {
                  e.preventDefault();
                  this.register();
                  return false;
                }}>
                <h1 className="display-4" style={{"paddingBottom": "10px"}}>Register</h1>
                <div class="form-group">
                  <input type="email"
                    class="form-control form-control-lg"
                    placeholder="Email Address"
                    className={(this.state.isError)?"form-control is-invalid form-control form-control-lg"
                              :"form-control form-control-lg"}
                    onChange={(e) => {
                      this.setState({
                        emailAddress: e.target.value
                      })
                    }}
                    value={this.state.emailAddress} />
                  <hr />
                  <input type="password"
                      class="form-control form-control-lg"
                      placeholder="Password"
                      className={(this.state.isError)?"form-control is-invalid form-control form-control-lg"
                                :"form-control form-control-lg"}
                      onChange={(e) => {
                        this.setState({
                          password: e.target.value
                        })
                      }}
                      value={this.state.password}/>
                  <input type="password"
                        class="form-control form-control-lg"
                        placeholder="Repeat Password"
                        className={(this.state.isError)?"form-control is-invalid form-control form-control-lg"
                                  :"form-control form-control-lg"}
                        onChange={(e) => {
                          this.setState({
                            repeatPassword: e.target.value
                          })
                        }}
                        value={this.state.repeatPassword}/>
                  </div>
                  <button onClick={this.register} value="Register" class="btn btn-lg btn-secondary btn-block">
                    {
                      buttonContent
                    }
                  </button>
                  <Link to="/auth/" class="btn btn-lg btn-link btn-block">or Sign In</Link>

                    </form>
                  </div>
                </div>);
              }
    }
}
