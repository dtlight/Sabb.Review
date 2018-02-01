import React from 'react'
import { ApplicationCard } from '../../components/review/'
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
                      <ApplicationCard id="test0" status="SUCCESS"/>
                  </div>
                  <div class="col-lg-12">
                      <ApplicationCard id="test1" status="PENDING"/>
                  </div>
                  <div class="col-lg-12">
                      <ApplicationCard id="test2" status="REFUSED"/>
                  </div>
                  <div class="col-lg-12">
                      <ApplicationCard id="test3" status="SUBMITTED"/>
                  </div>
              </Row>
         </div>)
  }
}
