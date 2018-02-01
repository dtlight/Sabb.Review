import React from 'react'
import { Link } from 'react-router-dom'
import {  ApplicationList } from '../../components/home/'
import './style.css'
import { Jumbotron, Button, ButtonGroup} from 'reactstrap';

export default class extends React.Component {

  render() {
    return (<div >
                <Jumbotron className="jumbotron-sm">
                    <h1 class="display-4">Welcome to SabbReview</h1>
                    <p class="lead">To view the status existing applications, please review the table below.</p>
                    <hr  />
                    <p>To start a new application for sabbatical leave, please visit the apply page.</p>
                    <p class="lead">
                       <Link to="/apply"><Button color="secondary">Get Started</Button></Link>
                     </p>
                </Jumbotron>
                <ButtonGroup style={{"paddingBottom": "10px", "textAlign": "center", "display": "block"}}>
                  <Button color="light"><i class="fa fa-chevron-left"></i> 2016/17 Academic Year</Button>
                  {/*<button class="btn btn-light">Next Year <i class="fa fa-chevron-right"></i></button>*/}
                </ButtonGroup>
                <ApplicationList />
           </div>)
  }
}
