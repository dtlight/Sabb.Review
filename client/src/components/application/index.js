import React from 'react';
import {Redirect } from 'react-router-dom';
import axios from 'axios';
import {Input, Button, Alert} from 'reactstrap';
import SignatureCanvas from 'react-signature-canvas'
import {AssignReview, ViewReviews} from '../review/'

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
        selectedTemplate: -1,
      departmentList: [],
        templateList:[]
    }
    this.loadDepartments = this.loadDepartments.bind(this);
    this.createApplication = this.createApplication.bind(this);
    this.selectDepartment = this.selectDepartment.bind(this);
    this.selectTemplate = this.selectTemplate.bind(this);
  }
  createApplication() {
      this.setState({
        isCreating: true
      })
      axios.post(`/application/template/${this.state.selectedTemplate}/department/${this.state.selectedDepartment}`)
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
        this.selectDepartment((!data.value[0])?-1:data.value[0][0]);
      })
    }

  selectDepartment(id) {
      axios.get(`/department/${id}/templates`).then(({data})=> {
          this.setState({
              templateList: data.value,
              selectedDepartment: id
          });
      })
  }

    selectTemplate(id) {
      console.log(id)
            this.setState({
                selectedTemplate: id
            });
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

        var templateList = [];

        for (var i = 0; i < this.state.templateList.length; i++) {
            var template = this.state.templateList[i];
            templateList.push(
                <option value={template[0]}>{template[1]}</option>
            )
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
                      <label>Select Department</label>
                      <select class="form-control" onChange={(e) => {
                        this.selectDepartment(e.target.value);
                      }}>
                        {departmentList}
                      </select>
                </div>
                      <div class="form-group">
                          <label>Select Template</label>
                          <select class="form-control" onChange={(e) => {
                              console.log(e)
                              this.selectTemplate(e.target.value);
                          }}
                          disabled={templateList.length===0}>
                              <option>--</option>
                              {templateList}
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

export class ApplicationAdminButtons extends React.Component {
    constructor(props) {
        super(props);
        this.props = props;
        this.state = {

        }
        this.submitApplication = this.submitApplication.bind(this);

    }

    submitApplication() {
        axios.put(`/application/${this.props.id}/state/SUBMITTED`).then(({data})=> {
                if(this.props.onStateChange) this.props.onStateChange("SUBMITTED");
        })
    }
    render() {
        return (<div style={{
            "padding": "20px",
            "marginBottom": "20px",
            "position": "-webkit-sticky",
            "position": "sticky",
            "height": "80px",
            "top": "0em",
            "box-shadow": "0px 6px 11px 0px #65656726"}} class="bg-light">

            <Button color="primary" style={{"marginRight":"10px"}}   onClick={this.submitApplication}><i class="fa fa-save"></i> Submit Application</Button>
            <a href={`${axios.defaults.baseURL}/pdf/application/${this.props.id}`} class="btn btn-secondary" style={{"marginRight":"10px"}} ><i class="fa fa-download"></i> Download</a>
            <AssignReview application={this.props.id} color="secondary" style={{"marginRight":"10px"}}>Assign Review</AssignReview>
            <ViewReviews application={this.props.id} color="secondary" style={{"marginRight":"10px"}}>View Assigned Reviews</ViewReviews>

        </div>);
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
      isEditable: false,
        currentState: ""
    }
    this.load = this.load.bind(this);
    this.getInstances = this.getInstances.bind(this);
  }
    componentWillReceiveProps() {
        this.load();
    }
    componentDidMount() {
        this.load();
    }

    load() {
        axios.get(`/application/${this.props.id}`).then(({data})=> {
            this.setState((state) => {
                state.fieldInstances = [];
                if(data.state === "STATUS_ERROR") {
                    state.isError = true
                } else {
                    state.currentState = data.value.state;
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
    getInstances(ro = false) {
        let fieldInstances = [];
        for (var fieldInstance of this.state.fieldInstances) {
            if(this.state.currentState === "COMPLETED" && fieldInstance.field.showAtEnd){
                fieldInstances.push(<FieldInstance disabled={ro||this.props.disabled} {...fieldInstance}/>);
            } else if (this.state.currentState !== "COMPLETED" && !fieldInstance.field.showAtEnd) {
                fieldInstances.push(<FieldInstance disabled={ro||this.props.disabled} {...fieldInstance}/>);
            }
        }
        return fieldInstances;
    }
  render() {
    if(this.state.isError) {
      return <h1 class="text-danger display-6" style={{"textAlign": "center"}}>
        Could not load requested application
      </h1>;
    } else if(this.state.isLoading) {
      return <div class="loader">Loading...</div>;
    } else if(!this.state.isEditable) {
      return (<div>
          <Alert color="secondary">
              This application has been submitted.
          </Alert>
          {this.getInstances(true)}
      </div>)
    } else if(this.state.fieldInstances) {

      return (
          <form>
              {this.getInstances()}

            <div className={"bg-light"}>
                <div class="form-group" style={{"padding": "10px"}}>
                    <p class="lead">Please draw your signature in the area below</p>
                <SignatureCanvas penColor='#252f3c'
                                 canvasProps={{width: 500, height: 200, className: 'sigCanvas'}} />
                </div>
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
        value: props.value,
        selected: props.selected
    }
    this.updateValue = this.updateValue.bind(this);
    this.updateSingleSelected = this.updateSingleSelected.bind(this);
  }

  updateValue() {
      axios.put(`/fieldinstance/${this.props.id}`, {value: this.state.value}).then(function(data) {
          console.log(data);
      });
  }


    updateSingleSelected(select) {
        axios.put(`/fieldinstance/${this.props.id}`, {value: select}).then(function(data) {
            console.log(data);
        }.bind(this));
    }

    updateMultipleSelected(select) {

        axios.put(`/fieldinstance/${this.props.id}`, {value: select}).then(function(data) {
            console.log(data);
        }.bind(this));
    }

    render() {

    var inner = "";
    if(this.props.field.type === "DIVIDER") {
      return <div><hr /><p class="lead">{this.props.field.title}</p></div>
    } else if(this.props.field.type === "MULTICHOICE") {
      let options = [];
      for (let option of this.props.field.fieldOptions) {
          options.push(<option value={option.id} disabled={this.props.disabled}>{option.title}</option>);
      }
      inner = <span><p><small>Press <code>shift</code> to select multiple items</small></p>
          <Input type="select" multiple onChange={(e) => {
              console.log(e.target.options)
          }}> {options} </Input></span>;

    } else if(this.props.field.type === "SINGLECHOICE") {
      let options = [];
      for (let option of this.props.field.fieldOptions) {

          options.push(<option value={option.id}>{option.title}</option>);
      }
      inner = <Input type="select" disabled={this.props.disabled}
                     defaultValue={(this.state.selected)?this.state.selected[0].id:undefined}
                     onChange={(e) => {
                         this.updateSingleSelected(e.target.value)
                     }}> {options} </Input>;


    } else if(this.props.field.type === "LONGTEXT") {
      inner = <Input type="textarea"
                     disabled={this.props.disabled}
                     value={this.state.value}
                     onChange={(e) => {
                         this.setState({
                             value: e.target.value
                         })
                     }}
                     onBlur={this.updateValue}/>;
    } else {
      inner = <Input
            disabled={this.props.disabled}
            value={this.state.value}
            onChange={(e) => {
                this.setState({
                   value: e.target.value
                  })
            }}
            onBlur={this.updateValue} />

    }

    return ( <div class="form-group" style={{"paddingBottom": "10px"}}>
              <label>{this.props.field.title}</label>
              {inner}
            </div>)

  }
}
