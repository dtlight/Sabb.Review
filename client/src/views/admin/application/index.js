import React from 'react';
import {Button} from 'reactstrap';
import {FieldList, NewQuestion} from '../../../components/template/index.js';
import {EditApplication, ApplicationAdminButtons} from '../../../components/application/index.js';

import axios from 'axios';

export class AdminEditApplication extends React.Component {
  constructor(props) {
    super(props);
    this.props = props;
    this.state = {

    }
  }
  componentDidMount() {

  }
  render() {

      return (
          <div>
              <ApplicationAdminButtons id={this.props.match.params.id}/>
              <div style={{"marginTop": "20px", "paddingBottom": "20px"}} className="container">

              <p class="lead"></p>
              <hr />
              <EditApplication id={this.props.match.params.id}/>
            </div>
          </div>
      );
    }
}


