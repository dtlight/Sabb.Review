import React from 'react'
import { Link } from 'react-router-dom'

export default class extends React.Component {
  render() {
    return (<div >
                <div class="jumbotron jumbotron-sm">
                    <h1 class="display-4">Welcome to SabbReview</h1>
                    <p class="lead">To start an appraisal and view the status existing applications, please review the table below.</p>
                    <hr  />
                    <p>To start a new application for sabbatical leave, please visit the apply page.</p>
                    <p class="lead">
                       <Link to="/apply" role="button" class="btn btn-secondary">Get Started</Link>
                     </p>
                </div>
                <table class="table">
                    <thead class="thead-light">
                    <tr>
                        <th scope="col">Application</th>
                        <th scope="col">Status</th>
                        <th scope="col"></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <th scope="row">1</th>
                        <td><span class="badge badge-success">Accepted</span></td>
                        <td>Otto</td>
                    </tr>
                    <tr>
                        <th scope="row">2</th>
                        <td><span class="badge badge-primary">Submitted</span></td>
                        <td>Thornton</td>
                    </tr>
                    <tr>
                        <th scope="row">3</th>
                        <td><span class="badge badge-danger">Refused</span></td>
                        <td>the Bird</td>
                    </tr>
                    </tbody>
                </table>
           </div>)
  }
}
