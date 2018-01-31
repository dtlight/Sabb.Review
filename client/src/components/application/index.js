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
      fieldInstances: []
    }
  }

  componentDidMount() {
    axios.get(`/application/${this.props.id}`).then(({data})=> {
      this.setState((state) => {
        for(var fieldInstance of data.value.fields) {
          state.fieldInstances.push(fieldInstance);
        }
        return state;
      })
    })
  }

  render() {
    if(this.state.fieldInstances) {
      let fieldInstances = [];
      for (var fieldInstance of this.state.fieldInstances) {
          fieldInstances.push(<FieldInstance fieldInstance={fieldInstance}/>);
      }
      return (
        <form>
          {fieldInstances}

          <div class="form-group">
            <input type="submit" class="btn btn-primary" value="Submit for Review" style={{"marginRight": "10px"}}/>
            <input type="submit" class="btn btn-secondary" value="Save"/>
          </div>
      </form>
      );
  } else {
    return "Loading ...";
  }
}
}

class FieldInstance extends React.Component {

  constructor(props) {
    super(props);
    this.props = props;
    this.updateValue = this.updateValue.bind(this);
  }

  updateValue() {

  }

    render() {
    //Assume multichoice to starts
    console.log(this.props.fieldInstance);

    var inner = "";
    if(this.props.fieldInstance.field.type === "MULTICHOICE") {
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
      inner = <Input />;
    }

    return ( <div class="form-group" style={{"paddingBottom": "10px"}}>
              <label>{this.props.fieldInstance.field.title}</label>
              {inner}
            </div>)

  }
}
