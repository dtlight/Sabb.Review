import React from 'react'
import {Table, Badge, Button} from 'reactstrap';
import axios from 'axios';
import {Link} from 'react-router-dom';
export class UserList extends React.Component {
    constructor(props) {
        super(props);
        this.props = props;
        this.state = {
            users: [],
        };
        this.loadUsers = this.loadUsers.bind(this);
    }


    loadUsers() {
        axios.get("/user/all").then(({data}) => {
            if(data.state === "STATUS_OK") {
                this.setState(prevState => {
                    return {
                        users: data.value
                    }
                })
            }
        });
    }
    componentDidMount() {
        this.loadUsers();
    }

    promote = (id) => {
        axios.post(`/user/${id}/promote`).then(({data}) => {
            if(data.state === "STATUS_OK") {
                this.loadUsers();
            }
        });
    }

    render() {
        let users = [];
        for(let v of this.state.users) {
            users.push(
                <tr>
                    <td>{v.emailAddress} {v.isAdmin &&
                        <Badge>Administrator</Badge>
                    }</td>
                    <td>
                        {!v.isAdmin &&
                        <Button size="sm" color={"secondary"}
                                onClick={this.promote.bind(this, v.emailAddress)}>Promote</Button>
                        }
                    </td>
                </tr>);
        }
        return (
            <Table  striped={true}>
                <thead >
                <tr>
                    <td>Email Address</td>
                    <td>Actions</td>
                </tr>
                </thead>
                <tbody>
                {users}
                </tbody>
            </Table>
        );
    }
}
