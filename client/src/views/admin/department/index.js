import React from 'react';
import {Row, Col, ListGroupItem, ListGroup} from 'reactstrap';
import {FieldList, NewQuestion} from '../../../components/template/index.js';
import {ApplicationList} from '../../../components/home/index.js';
import {TemplateTable, CreateTemplate} from '../../../components/template/index.js';

import axios from 'axios';

export class DepartmentInfo extends React.Component {
  constructor(props) {
    super(props);
    this.props = props;
    this.state = {
      isLoading: true,
      name: ""
    }
  }
  componentDidMount() {
    axios.get(`/department/${this.props.match.params.id}`).then(({data}) => {
      if(data.state === "STATUS_OK") {
        this.setState({
          isLoading: false,
          hod: data.value.HOD,
          name: data.value.name,
          applications: data.value.applications,
<<<<<<< Updated upstream
            templates: data.value.templateList
=======
            templates: data.value.templates
>>>>>>> Stashed changes
        })
      }
    });
  }
  render() {
    if(this.state.isLoading) {
      return (<div class="loader">Loading...</div>);
    } else {
      return (
        <div>
          <h1 class="display-4">{this.state.name} Department</h1>
          <p class="lead"></p>
          <hr />
          <Row>
            <Col md={3} bg={'primary'} style={{"marginBottom": "1em"}}>
              <ListGroup>
<<<<<<< Updated upstream
                <ListGroupItem><strong>Head of Department</strong>: <br/>{(this.state.hod)?this.state.hod.emailAddress:""}</ListGroupItem>
=======
                <ListGroupItem><strong>Head of Department</strong>: <br/>{this.state.hod}</ListGroupItem>
>>>>>>> Stashed changes
              </ListGroup>
            </Col>
            <Col md={9}>
              <div>
                <h2 class="lead">Applications</h2>
                  <ApplicationList applications={this.state.applications} />
              </div>
              <hr />
              <div>
                <h2 class="lead">Templates
                </h2>
                <TemplateTable templates={this.state.templates} />
                  <CreateTemplate departmentId={this.props.match.params.id} style={{"marginTop":"15px", "marginBottom":"15px"}}/>
              </div>
            </Col>
          </Row>
        </div>
      );
    }
  }
}
