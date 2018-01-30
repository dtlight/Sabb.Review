import React from 'react'
import { ApplicationCard } from '../../components/home/'
import './style.css'
import {Row} from "reactstrap";

export default class extends React.Component {
  render() {
    return (
        <div>
            <h1 class="display-4">Assigned Reviews</h1>
              <p class="lead">These applications require your review.</p>
              <hr />
              <Row className="row applications-collapse">
                  <div class="col-lg-12">
                      <ApplicationCard id="test" status="SUCCESS"/>
                  </div>
                  <div class="col-lg-12">
                      <ApplicationCard id="test" status="PENDING"/>
                  </div>
                  <div class="col-lg-12">
                      <ApplicationCard id="test" status="REFUSED"/>
                  </div>
                  <div class="col-lg-12">
                      <ApplicationCard id="test" status="SUBMITTED"/>
                  </div>
              </Row>
         </div>)
  }
}
