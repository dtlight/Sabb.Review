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
      isLoading: true,
      isCreating: false,
      selectedDepartment: -1,
      departmentList: []
    }
    this.loadDepartments = this.loadDepartments.bind(this);
    this.createApplication = this.createApplication.bind(this);
    this.selectDepartment = this.selectDepartment.bind(this);
  }
  createApplication() {
      this.setState({
        isCreating: true
      })
      axios.post(`/application/template/1/department/${this.state.selectedDepartment}`)
        .then(function (response) {
          if(response.data.state !== "STATUS_ERROR") {
            this.setState({
              isSuccess: true,
              isCreating: false,
              newApplicationId: response.data.value.id
            })
          } else {
            this.setState({
              isError: true,
              isCreating: false
            })
          }

      }.bind(this))

  }
  componentWillMount() {
    this.loadDepartments();
  }

  loadDepartments() {
      axios.get("/departments").then(({data})=> {
        this.setState({
          isLoading: false,
          departmentList: data.value,
          selectedDepartment: (!data.value[0])?-1:data.value[0][0]
        });
      })
    }

  selectDepartment(id) {
    this.setState({
      selectedDepartment: id
    })
  }

  render() {
    if(this.state.isLoading) {
      return <div class="loader">Loading...</div>;

    } else {
        var departmentList = [];

        for (var i = 0; i < this.state.departmentList.length; i++) {
          var department = this.state.departmentList[i];
          departmentList.push(
            <option value={department[0]}>{department[1]}</option>
          )
        }

        for (let department in this.state.departmentList) {
          console.log(department);

        }
          var buttonContent = (this.state.isCreating)?<i class="fa fa-refresh fa-spin"></i>:"Start Application";
           if(this.state.isSuccess) {
            return (
              <Redirect to={`/apply/${this.state.newApplicationId}`}/>
            );
          } else {
            return (
              <div>
                  <div class="form-group">
                      <label>Current Department</label>
                      <select class="form-control" onChange={(e) => {
                        this.selectDepartment(e.target.value);
                      }}>
                        {departmentList}
                      </select>
                </div>

                <p>
                  You are applying for the <strong>2017-18</strong> academic year.
                </p>
                <div class="form-group">
                    <button style={{"minWidth": "145px"}} onClick={this.createApplication}
                      class={(this.state.isError)?"btn btn-primary border-danger":"btn btn-primary"}
                      disabled={this.state.selectedDepartment === -1}>{buttonContent}</button>
                </div>
              </div>
            );
          }
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
      isError: false,
      isEditable: false
    }
    this.submitApplication = this.submitApplication.bind(this);
    this.onStateChange = this.onStateChange.bind(this);
  }

  componentDidMount() {
    axios.get(`/application/${this.props.id}`).then(({data})=> {
      this.setState((state) => {
        if(data.state === "STATUS_ERROR") {
          state.isError = true
        } else {
          state.isEditable = (data.value.state === "PENDING");
          for(var fieldInstance of data.value.fields) {
            state.fieldInstances.push(fieldInstance);
          }
          state.isLoading = false;
        }
        return state;
      })
    })
  }

  onStateChange(newState) {
    this.setState({
      isEditable: (newState === "PENDING")
    })
  }
  submitApplication() {//http://{{host}}/application/4/state/accepted
    axios.put(`/application/${this.props.id}/state/SUBMITTED`).then(({data})=> {
      this.onStateChange("SUBMITTED");
    })
  }

  render() {
    if(this.state.isError) {
      return <h1 class="text-danger display-6" style={{"textAlign": "center"}}>
        Could not load requested application
      </h1>;
    } else if(this.state.isLoading) {
      return <div class="loader">Loading...</div>;
    } else if(!this.state.isEditable) {
      return (<p>This application has been submitted</p>)
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
