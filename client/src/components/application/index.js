import React from 'react';
import {Redirect } from 'react-router-dom';
import axios from 'axios';

export class CreateApplication extends React.Component {
  constructor(props) {
    super(props);
    this.props = props;
    this.state = {
      isError: false,
      isSuccess: false,
      isLoading: false
    }
    this.createApplication = this.createApplication.bind(this);
  }
  createApplication() {
    this.setState({
      isLoading: true
    })
    axios.post(`/application`)
      .then(function (response) {
        if(response.data.state !== "STATUS_ERROR") {
          this.setState({
            isSuccess: true,
            isLoading: false,
            newApplicationId: response.data.value.id
          })
        } else {
          this.setState({
            isError: true,
            isLoading: false
          })
        }

    }.bind(this))
  }
  render() {
    var buttonContent = (this.state.isLoading)?<i class="fa fa-refresh fa-spin"></i>:"Start Application";
    if(this.state.isSuccess) {
      return (
        <Redirect to={`/apply/${this.state.newApplicationId}`}/>
      );
    } else {
      return (
        <div>
            <div class="form-group">
                <label for="exampleFormControlSelect1">Current Department</label>
                <select class="form-control" id="exampleFormControlSelect1">
                  <option>Computer Science</option>
                  <option>Mathematics</option>
                  <option>English</option>
                  <option>Liberal Arts</option>
                </select>
          </div>

          <p>
            You are applying for the <strong>2017-18</strong> academic year.
          </p>
          <div class="form-group">
              <button style={{"minWidth": "145px"}} onClick={this.createApplication} class={(this.state.isError)?"btn btn-primary border-danger":"btn btn-primary"}>{buttonContent}</button>
          </div>
        </div>
      );
    }
  }
}
