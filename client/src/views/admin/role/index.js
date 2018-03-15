import React from 'react';
import axios from 'axios';
import {RoleTable, RoleEditor} from '../../../components/role/';

export class ViewRole extends React.Component {

  render() {
    return (
      <div>
        <h1 class="display-4"><strong>Edit Template:</strong> {this.state.title}</h1>
        <p class="lead">These are the roles that can be assigned to reviewers.</p>
        <hr />
      </div>
    );
  }
}



export class ViewRoles extends React.Component {
    constructor(props) {
        super(props);
        this.props = props;
    }


  render() {
    return (
      <div>
        <h1 class="display-4">View Roles</h1>
        <p class="lead">These are the roles that can be assigned to a new review.</p>
        <hr />
          <RoleTable />
          <RoleEditor>Create a new role</RoleEditor>
      </div>
    );
  }
}
