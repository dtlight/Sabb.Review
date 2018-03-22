import React from 'react'
import {Link} from 'react-router-dom'
import {CreateApplication, EditApplication, ApplicationAdminButtons} from '../../components/application/index.js'
import axios from 'axios';

export class Introduction extends React.Component {
  render() {
    return (<div>
              <h1 class="display-4">Start a New Application</h1>
              <p class="lead">To start the sabbatical application process, please fill out the form below.</p>
              <hr />
              <CreateApplication />
           </div>)
  }
}

export class EditExisting extends React.Component {
    constructor(props){
        super(props);
        this.props = props;
        this.state = {
            newState: null
        }
    }

  render() {
    return (<div>
        <ApplicationAdminButtons id={this.props.match.params.id} onStateChange={(newState)=>{
            this.setState({
                newState: newState
            })
        }}/>
        <div style={{"marginTop": "20px", "paddingBottom": "20px"}} className="container">

        <h1 class="display-4">Edit Application</h1>
              <p class="lead">Please fill out the required fields below. You can save your application to return to it later or submit to forward the application for review.</p>
              <hr />
              <EditApplication id={this.props.match.params.id} newState={this.state.newState}/>
        </div>
           </div>)
  }
}
