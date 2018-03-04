import React from 'react';
import { Link } from 'react-router-dom';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter, InputGroup, FormGroup, Input} from 'reactstrap';
import axios from "axios/index";

export class AssignReview extends React.Component {


    constructor(props) {
        super(props);
        this.state = {
            modal: false,
            assignee: ""
        };

        this.toggle = this.toggle.bind(this);
        this.submit = this.submit.bind(this);

    }

    toggle() {
        this.setState({
            modal: !this.state.modal,
            assignee: ""

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
                <ModalHeader toggle={this.toggle}>Assign Review</ModalHeader>
                <ModalBody>
                         <FormGroup>
              <label>Assignee</label>
                      <Input onChange={(e) => {
                          this.setState({
                              assignee: e.target.value
                          })
                      }} value={this.state.assignee} placeholder={"example@example.com"} />
                    </FormGroup>
                  </ModalBody>
                  <ModalFooter>
                    <Button class={(this.state.isError)?"btn btn-primary border-danger":"btn btn-primary"} color="primary" onClick={this.submit}>Assign Review</Button>
                  </ModalFooter>
                </Modal>
            </span>
        )
    }
}
