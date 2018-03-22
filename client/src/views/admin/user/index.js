import React from 'react';
import {UserList} from '../../../components/user/index';
import axios from 'axios';

export default class Users extends React.Component {
    constructor(props) {
        super(props);
        this.props = props;
        this.state = {
            newDeptName: "",
        };
    }

    render() {
        return (
            <div>
                <h1 class="display-4">Users</h1>
                <p class="lead">This is a list of the users who are currently registered with SabbReview</p>
                <UserList />
            </div>
        )
    }
}
