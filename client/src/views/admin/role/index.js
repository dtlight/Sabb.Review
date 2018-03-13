import React from 'react';
import axios from 'axios';

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
            roles: null
        };
        this.load = this.load.bind(this);
    }
    componentDidMount() {
        this.load();
    }
    load() {
        axios.get(`/role`).then(({data}) => {
            console.log(data);
            if(data.state === "STATUS_OK") {
                this.setState({
                    roles: data.value
                });
            }
        });
    }

  render() {
    return (
      <div>
        <h1 class="display-4">View Roles</h1>
        <p class="lead">These are the roles that can be assigned to a new review.</p>
        <hr />
          {(this.state.roles === null)?<div class="loader">Loading...</div>:""}
      </div>
    );
  }
}
