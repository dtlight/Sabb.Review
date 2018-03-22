import React from 'react';
import 'react-tabs/style/react-tabs.css';
import {  AssignmentList, CommentArea, CommentList, reviewStates } from '../../components/review/'
import { EditApplication, ApplicationAdminButtons, DropDownStates } from '../../components/application/index.js'
import axios from "axios/index";
import { Row, Col, Alert } from 'reactstrap';

export default class extends React.Component {
  constructor(props, context){
    super(props, context);
  }

  render() {
    return (<div>
              <h1 class="display-4">Assigned Reviews</h1>
                <p class="lead">These applications require your review.</p>
                <hr/>
             <AssignmentList />

    </div>);
  }
}

export class ReviewDetail extends React.Component {
    constructor(props, context){
        super(props, context);
        this.load = this.load.bind(this);
        this.state ={
            isLoading: true

        }
    }
    componentWillMount() {
        this.load();
    }

    load() {
        axios.get(`/assignment/${this.props.match.params.id}`).then(({data})=> {
            this.setState({
                assignment: data.value,
                isLoading: false,

            });
            console.log(data);
        })

    }
    render() {
        if(this.state.isLoading) {
            return <div class="loader">Loading...</div>;
        } else {
            return (<div>
                <ApplicationAdminButtons assignment={this.state.assignment.id} id={this.state.assignment.application.id} hideSubmit={true} showChangeState={this.state.assignment.role.canChangeApplicationState} onStateChange={(newState)=>{
                    this.setState({
                        newState: newState
                    })
                }}  onAssignmentStateChange={(newState)=>{
                    this.setState({
                        newAssignmentState: newState
                    })
                }}/>
                <div style={{"marginTop": "20px", "paddingBottom": "20px"}} className="container">
                <h1 class="display-4">Edit Review</h1>
                <p class="lead">These applications require your review.</p>
                <hr/>
                <Row>
                  <Col md={8}>
                    <EditApplication id={this.state.assignment.application.id} disabled={!this.state.assignment.role.canEdit}/>
                  </Col>
                    <Col md={4} >
                        <Alert color={(this.state.newAssignmentState)?reviewStates[this.state.newAssignmentState].colours:reviewStates[this.state.assignment.state].colours}>
                            {(this.state.newAssignmentState)?reviewStates[this.state.newAssignmentState].body:reviewStates[this.state.assignment.state].body}
                            </Alert>
                        <CommentList comments={this.state.assignment.comments}/>
                        {(this.state.assignment.role.canComment)?<CommentArea  assignment={this.state.assignment.id} onChange={this.load}/>:""}
                        </Col>
                </Row>
                </div>
            </div>);
        }

    }
}
