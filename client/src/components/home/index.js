import React from 'react';
import { Link } from 'react-router-dom';

let applicationStates = {
  PENDING: {
    humanStatus: "Pending",
    body: "This application is awaiting completion, please edit your application and press submit to proceed.",
    colours: "warning",
    buttonsVisible: true
  },
  SUCCESS: {
    humanStatus: "Accepted",
    body: "This application has been accepted by the department and review groups. You will received further information by email.",
    colours: "success",
    buttonsVisible: false
  },
  REFUSED: {
    humanStatus: "Rejected",
    body: "This application has been rejected by the department or review group.",
    colours: "danger",
    buttonsVisible: false
  },
  SUBMITTED: {
    humanStatus: "Submitted",
    body: "The application has been submitted and is currently awaiting approval.",
    colours: "secondary",
    buttonsVisible: true
  }
}



export class ApplicationCard extends React.Component {
  constructor(props) {
    super(props);
    this.props = props;
    this.state = {
      id: this.props.id,
      status: this.props.status,
      applicationDate: "Tuesday, 16th March 2018"
    }
  }

  render() {
    return (
      <div style={{"marginBottom": "20px", "height": "100%", "minHeight": "180px"}} class={`card border-${applicationStates[this.state.status].colours}`}>
        <div class="card-body">
          <h5 class="card-title">Application to the Computer Science dept. <span class={`badge badge-${applicationStates[this.state.status].colours}`}>{applicationStates[this.state.status].humanStatus}</span></h5>
          <p class="card-subtitle mb-2 text-muted">{this.state.applicationDate}</p>
          <p class="card-text">{applicationStates[this.state.status].body}</p>
          <div class={(applicationStates[this.state.status].buttonsVisible)?"visible":"invisible"}>
            <Link  style={{"position": "absolute", "bottom": "0", "paddingBottom": "15px"}} class="text-secondary" to={`/apply/${this.state.id}`}>Edit Application</Link>
            <Link class="text-danger float-right" style={{"position": "absolute", "bottom": "0", "paddingBottom": "15px", "paddingRight": "20px", "right": "0"}} to={`/apply/${this.state.id}`}>Withdraw</Link>
          </div>
        </div>
      </div>
    )
  }
}


export class NoApplications extends React.Component {
  constructor(props) {
    super(props);
    this.props = props;
  }

  render() {
    return (
      <div class="card card-success" style="width: 18rem;">
        <div class="card-body">
          <h5 class="card-title">Card title </h5>
          <h6 class="card-subtitle mb-2 text-muted">Card subtitle</h6>
          <p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
          <a href="#" class="card-link">Card link</a>
          <a href="#" class="card-link">Another link</a>
        </div>
      </div>
    )
  }
}
