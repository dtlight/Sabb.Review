import React from 'react'
import {Link} from 'react-router-dom'
import {CreateApplication} from '../../components/application/index.js'
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
  render() {
    return (<div>
              <h1 class="display-4">Edit Application</h1>
              <p class="lead">Please fill out the required fields below. You can save your application to return to it later or submit to forward the application for review.</p>
              <hr />
              <form>
                <div class="form-group">
                  <label for="exampleFormControlInput1">Present Post</label>
                  <input type="email" class="form-control" id="exampleFormControlInput1" placeholder="" />
                </div>

                <div class="form-group">
                  <label for="exampleFormControlTextarea1">Address For Further Correspondence</label>
                  <textarea class="form-control" id="exampleFormControlTextarea1" rows="3"></textarea>
                </div>

                <div class="form-group">
                  <label for="exampleFormControlSelect2">Applicable to Which College Objectives</label>
                  <select multiple class="form-control">
                    <option>To raise the national and international profile of Royal Holloway.</option>
                    <option>To offer excellent, personalised education that equips our students with knowledge and skills.</option>
                    <option>To deliver an outstanding student experience.</option>
                    <option>To develop a vibrant research environment with the facilities.</option>
                    <option>To develop a high quality, diverse and professional workforce.</option>
                  </select>
                </div>

                <div class="form-group">
                  <label for="exampleFormControlTextarea1">Expected End Date</label>
                  <input type="date" id="leaveEndDate" class="form-control" />
                </div>

                <div class="form-group">
                  <input type="submit" class="btn btn-primary" value="Submit for Review" style={{"marginRight": "10px"}}/>
                  <input type="submit" class="btn btn-secondary" value="Save"/>
                </div>
            </form>
           </div>)
  }
}