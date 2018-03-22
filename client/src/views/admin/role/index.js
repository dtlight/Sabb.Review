import React from 'react';
import axios from 'axios';
import {RoleTable, RoleEditor} from '../../../components/role/';

export class ViewRole extends React.Component {

  render() {
    return (
      <div>
        <h1 class="display-4"><strong>Edit Roles:</strong> {this.state.title}</h1>
        <p class="lead">These are the roles that you can assign to the reviews.</p>
        <hr />
      </div>
    );
  }
}



export class ViewRoles extends React.Component {
    constructor(props) {
        super(props);
        this.props = props;
        this.state = {
            s: 0
        }
    }


  render() {
    return (
      <div>
        <h1 class="display-4">View Roles</h1>
        <p class="lead">These are the roles that can be assigned to a new review.</p>

          <RoleTable s={this.state.s}/>
          <RoleEditor onChange={() => {
              this.setState({
                  s: this.state.s ++
              })
          }}>Edit Role</RoleEditor>
      </div>
    );
  }
}
