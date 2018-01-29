import React from 'react'
import { Link } from 'react-router-dom'
import { ApplicationCard } from '../../components/home/'
import './style.css'
export default class extends React.Component {
  render() {
    return (<div >
                <div class="jumbotron jumbotron-sm">
                    <h1 class="display-4">Welcome to SabbReview</h1>
                    <p class="lead">To view the status existing applications, please review the table below.</p>
                    <hr  />
                    <p>To start a new application for sabbatical leave, please visit the apply page.</p>
                    <p class="lead">
                       <Link to="/apply" role="button" class="btn btn-secondary">Get Started</Link>
                     </p>
                </div>
                <div class="btn-group" style={{"paddingBottom": "10px", "textAlign": "center", "display": "block"}}>
                  <button class="btn btn-light"><i class="fa fa-chevron-left"></i> 2016/17 Academic Year</button>
                  {/*<button class="btn btn-light">Next Year <i class="fa fa-chevron-right"></i></button>*/}
                </div>
                <div class="row applications-collapse">
                  <div class="col-lg-4">
                    <ApplicationCard id="test" status="SUCCESS"/>
                  </div>
                  <div class="col-lg-4">
                    <ApplicationCard id="test" status="PENDING"/>
                  </div>
                  <div class="col-lg-4">
                    <ApplicationCard id="test" status="REFUSED"/>
                  </div>
                  <div class="col-lg-4">
                    <ApplicationCard id="test" status="SUBMITTED"/>
                  </div>
                </div>
           </div>)
  }
}
