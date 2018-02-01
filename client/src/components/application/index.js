import React from 'react';
import {Redirect } from 'react-router-dom';
import axios from 'axios';
import {Input} from 'reactstrap';

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
    axios.post(`/application/template/1`)
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

export class EditApplication extends React.Component {
  constructor(props) {
    super(props);
    this.props = props;
    this.state = {
      fieldInstances: [],
      isLoading: true,
      isError: false
    }
    this.submitApplication = this.submitApplication.bind(this);
  }

  componentDidMount() {
    axios.get(`/application/${this.props.id}`).then(({data})=> {
      this.setState((state) => {
        if(data.state === "STATUS_ERROR") {
          state.isError = true
        } else {
          for(var fieldInstance of data.value.fields) {
            state.fieldInstances.push(fieldInstance);
          }
          state.isLoading = false;
        }
        return state;
      })
    })
  }

  submitApplication() {//http://{{host}}/application/4/state/accepted
    axios.put(`/application/${this.props.id}/state/SUBMITTED`).then(({data})=> {
      alert(data);
      console.log(data);
    })
  }

  render() {
    if(this.state.isError) {
      return <h1 class="text-danger display-6" style={{"textAlign": "center"}}>
        Could not load requested application
      </h1>;
    } else if(this.state.isLoading) {
      return <div class="loader">Loading...</div>;
    } else if(this.state.fieldInstances) {
      let fieldInstances = [];
      for (var fieldInstance of this.state.fieldInstances) {
          fieldInstances.push(<FieldInstance value={fieldInstance.value} fieldInstance={fieldInstance}/>);
      }
      return (
          <form>
            {fieldInstances}

            <div class="form-group">
              <input type="button" class="btn btn-primary" value="Submit for Review" style={{"marginRight": "10px"}} onClick={this.submitApplication}/>
            </div>
        </form>
        );
    }
  }
}

class FieldInstance extends React.Component {
  constructor(props) {
    super(props);
    this.props = props;
    this.state = {
      value: props.value
    }
    this.updateValue = this.updateValue.bind(this);
  }

  updateValue(value) {
    axios.put(`/fieldinstance/${this.props.fieldInstance.id}`, {value: this.state.value}).then(function(data) {
      console.log(data);
    });
  }

    render() {
    //Assume multichoice to starts
    console.log(this.props.fieldInstance);

    var inner = "";
    if(this.props.fieldInstance.field.type === "DIVIDER") {
      return <div><hr /><p class="lead">{this.props.fieldInstance.field.title}</p></div>
    } else if(this.props.fieldInstance.field.type === "MULTICHOICE") {
      let options = [];
      for (let option of this.props.fieldInstance.field.fieldOptions) {
          options.push(<option value={option.id}>{option.title}</option>);
      }
      inner = <span><p><small>Press <code>shift</code> to select multiple items</small></p><Input type="select" multiple> {options} </Input></span>;

    } else if(this.props.fieldInstance.field.type === "SINGLECHOICE") {
      let options = [];
      for (let option of this.props.fieldInstance.field.fieldOptions) {
          options.push(<option value={option.id}>{option.title}</option>);
      }
      inner = <Input type="select"> {options} </Input>;
    } else if(this.props.fieldInstance.field.type === "LONGTEXT") {
      inner = <Input type="textarea"/>;
    } else {
      inner = <Input
        value={this.state.value}
        onChange={(e) => {
          this.setState({
            value: e.target.value
          })
        }}
        onBlur={this.updateValue} />

    }

    return ( <div class="form-group" style={{"paddingBottom": "10px"}}>
              <label>{this.props.fieldInstance.field.title}</label>
              {inner}
            </div>)

  }
}
