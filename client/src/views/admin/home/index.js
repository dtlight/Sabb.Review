import React from 'react';
import {DepartmentList} from '../../../components/admin/index';
import {Input, FormGroup, Button, Form} from 'reactstrap';
import axios from 'axios';

export default class Home extends React.Component {
    constructor(props) {
        super(props);
        this.props = props;
        this.state = {
            newDeptName: ""
        };
        this.addDepartment = this.addDepartment.bind(this);
    }
    addDepartment() {
        axios.post("/department", {name: this.state.newDeptName}).then(({data})=> {
            alert('done');
        })
    }
    render() {
        return (
            <div>
                <h1 class="display-4">Departments</h1>
                <p class="lead">These are the departments currently associated with SabbReview</p>
                <hr />
                <DepartmentList />
                <hr />
                <strong class="display-5" >Add Department</strong>

                <Form style={{"marginTop": "20px"}}>
                    <FormGroup>
                        <label>Department Name</label>
                        <Input type={"text"} placeholder={"Name"} onChange={(e) => {
                            this.setState({
                                newDeptName: e.target.value
                            })
                        }}/>
                    </FormGroup>

                    <FormGroup>
                        <Button color={"primary"} onClick={this.addDepartment}>Add Department</Button>
                    </FormGroup>
                </Form>
            </div>
        )
    }
}
