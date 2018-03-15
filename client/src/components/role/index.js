import React from 'react';
import { Button, Modal, Label, ModalHeader, ModalBody, ModalFooter, FormGroup, Input, Table} from 'reactstrap';
import axios from "axios/index";



export class RoleEditor extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            modal: false,
            roleName: "",
            canChangeApplicationState: false,
            canComment: false,
            canEdit: false
        };
        this.toggle = this.toggle.bind(this);
        this.submit = this.submit.bind(this);
        this.load = this.load.bind(this);
        this.updateCheckbox = this.updateCheckbox.bind(this);
    }

    toggle() {
        this.setState((state) => {
            if(this.props.id && !state.modal) {
                this.load();
                state.modal = !state.modal;
                return state;
            } else {
                return {
                    modal: !state.modal,
                    roleName: "",
                    canChangeApplicationState: false,
                    canComment: false,
                    canEdit: false
                }
            }


        });
    }

    componentWillMount() {
    }

    load() {
        axios.get(`/role/${this.props.id}`)
            .then(({data})=> {
                this.setState({
                    roleName: data.value.name,
                    canChangeApplicationState: data.value.canChangeApplicationState,
                    canComment: data.value.canComment,
                    canEdit:  data.value.canEdit
                });
            });


    }
    submit() {
        axios.post(`/role`, {
            id: this.props.id,
            name: this.state.roleName,
            canChangeApplicationState: this.state.canChangeApplicationState,
            canComment: this.state.canComment,
            canEdit: this.state.canEdit
        })
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

    updateCheckbox(v) {
        this.setState((state) => {
            state[v] = !state[v];
            return state;
        })
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
                                         roleName: e.target.value
                                     })
                                 }} value={this.state.roleName}/>
                            </FormGroup>


                            <FormGroup check>
                                <Label check>

                                    <Input type="checkbox" checked={this.state.canChangeApplicationState} onChange={()=>{
                                        this.updateCheckbox("canChangeApplicationState")
                                    }}/>{' '}Can the user change application state?
                                </Label>
                            </FormGroup>


                            <FormGroup check>
                                <Label check >
                                    <Input type="checkbox" checked={this.state.canEdit} onChange={()=>{
                                        this.updateCheckbox("canEdit")
                                    }}/>{' '}Can the user edit the application?
                                </Label>
                            </FormGroup>


                            <FormGroup check>
                                <Label check>
                                    <Input type="checkbox" checked={this.state.canComment} onChange={()=>{
                                        this.updateCheckbox("canComment")
                                    }}/>{' '}Can the user comment on the application?
                                </Label>
                            </FormGroup>

                        </ModalBody>
                        <ModalFooter>
                            <Button class={(this.state.isError)?"btn btn-primary border-danger":"btn btn-primary"} color="primary" onClick={this.submit}>Save Role</Button>
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
        if(this.state.roles) {
            let roles = [];
            for(let role of this.state.roles) {
                roles.push(<tr>
                    <td>{role[1]}</td>
                    <td><RoleEditor id={role[0]}>Edit Role</RoleEditor></td>

                </tr>)
            }
            return (
                <div>
                    <Table striped={true}>
                        <thead>
                        <tr>
                            <td>Name</td>
                            <td>Actions</td>
                        </tr>
                        </thead>
                        <tbody>
                        {roles}
                        </tbody>
                    </Table>
                </div>
            )
        } else {
            return (<div class="loader">Loading...</div>);
        }
    }
}
