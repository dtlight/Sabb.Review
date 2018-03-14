import React from 'react';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter, FormGroup, Input, Table} from 'reactstrap';
import axios from "axios/index";



export class CreateRole extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            modal: false,
            roleName: ""
        };
        this.toggle = this.toggle.bind(this);
        this.submit = this.submit.bind(this);
    }

    toggle() {
        this.setState({
            modal: !this.state.modal,
            roleName: ""

        });
    }


    submit() {
        axios.post(`/assignment/application/${this.props.application}/assignee/${this.state.assignee}`)
            .then(function (response) {
                if(response.data.state !== "STATUS_ERROR") {
                    this.setState({
                        isSuccess: true,
                        isCreating: false,
                        modal: false,
                        assignee: ""

                    })
                } else {
                    this.setState({
                        isError: true,
                        isCreating: false
                    })
                }

            }.bind(this))
    }

    render() {
        return (
            <span>
                <Button {...this.props} onClick={this.toggle}>{this.props.children}</Button>
                <Modal isOpen={this.state.modal} toggle={this.toggle} className={this.props.className}>
                    <ModalHeader toggle={this.toggle}>Create Modal</ModalHeader>
                        <ModalBody>
                             <FormGroup>
                                 <label>Role Name</label>
                                 <Input onChange={(e) => {
                                  this.setState({
                                      assignee: e.target.value
                                  })
                                 }} value={this.state.assignee}/>
                            </FormGroup>


                            <FormGroup check>
                                <Label check>
                                    <Input type="checkbox" />{' '}Can the user change application state?
                                </Label>
                            </FormGroup>


                            <FormGroup check>
                                <Label check>
                                    <Input type="checkbox" />{' '}Can the user comment on applications?
                                </Label>
                            </FormGroup>


                            <FormGroup check>
                                <Label check>
                                    <Input type="checkbox" />{' '}Can the user change application state?
                                </Label>
                            </FormGroup>

                        </ModalBody>
                        <ModalFooter>
                            <Button class={(this.state.isError)?"btn btn-primary border-danger":"btn btn-primary"} color="primary" onClick={this.submit}>Create Review</Button>
                        </ModalFooter>
                </Modal>
            </span>
        )
    }
}



export class RoleTable extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isLoading: true,
            roleList: null

        };
        this.props = props;

    }


    render() {


            return (
                <div>
                    <Table striped={true}>

                    </Table>
                </div>
            )


    }
}
