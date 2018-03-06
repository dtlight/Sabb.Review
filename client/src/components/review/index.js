import React from 'react';
import { Link } from 'react-router-dom';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter, InputGroup, FormGroup, Input, Card, Badge, CardText, CardBody,
    CardTitle, CardSubtitle, ListGroup, ListGroupItem } from 'reactstrap';
import axios from "axios/index";


let applicationStates = {
    PENDING: {
        humanStatus: "Pending",
        body: "This application is awaiting completion, please edit your application and press submit to proceed.",
        colours: "warning",
        buttonsVisible: true
    },
    SUCCESS: {
        humanStatus: "Accepted",
        body: "This application has been accepted by the department and review groups. You will received further information by email.",
        colours: "success",
        buttonsVisible: false
    },
    REFUSED: {
        humanStatus: "Rejected",
        body: "This application has been rejected by the department or review group.",
        colours: "danger",
        buttonsVisible: false
    },
    SUBMITTED: {
        humanStatus: "Submitted",
        body: "The application has been submitted and is currently awaiting approval.",
        colours: "secondary",
        buttonsVisible: true
    }
};




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



export class ViewReviews extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            modal: false
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
                <ModalHeader toggle={this.toggle}>Assigned Reviews</ModalHeader>
                <ModalBody>
                  <ListGroup>
                    <ListGroupItem>Cras justo odio</ListGroupItem>
                    <ListGroupItem>Dapibus ac facilisis in</ListGroupItem>
                    <ListGroupItem>Morbi leo risus</ListGroupItem>
                    <ListGroupItem>Porta ac consectetur ac</ListGroupItem>
                    <ListGroupItem>Vestibulum at eros</ListGroupItem>
                  </ListGroup>
                  </ModalBody>

                </Modal>
            </span>
        )
    }
}


export class AssignmentCard extends React.Component {
    /*constructor(props) {
        super(props);
        this.props = props;

    }*/
    render() {
        return (
            <Card style={{"marginBottom": "20px", "height": "100%", "minHeight": "180px"}} className={`border-${applicationStates[this.props.status].colours} bg-light`}>
                <CardBody>
                    <CardTitle><h5>Review for {this.props.assignee} <Badge color={applicationStates[this.props.status].colours}>{applicationStates[this.props.status].humanStatus}</Badge></h5></CardTitle>
                    <CardSubtitle className="mb-2 text-muted">{this.props.application.department} Dept.</CardSubtitle>
                    <CardText>{applicationStates[this.props.status].body}</CardText>
                    <div class={(applicationStates[this.props.status].buttonsVisible)?"visible":"invisible"}>
                        <Link style={{"position": "absolute", "bottom": "0", "paddingBottom": "15px"}} class="text-secondary" to={`/review/${this.props.id}`}>View Review</Link>
                    </div>
                </CardBody>
            </Card>
        )
    }
}

export class AssignmentList extends React.Component {
    constructor(props) {
        super(props);
        this.props = props;
        this.state = {
            assignmentList: [],
            isLoading: true,

        }
        this.load = this.load.bind(this);
    }
    componentDidMount() {
        this.load();
    }

    load() {

            axios.get(`/user/assignments`).then(({data})=> {
                this.setState({
                    assignmentList: data.value,
                    isLoading: false
                });
                console.log(data);
            })

    }
    render() {
        if(this.state.isLoading) {
            return <div class="loader">Loading...</div>;
        } else {
            let applicationListView = [];
            for (let assignment of this.state.assignmentList) {
                applicationListView.push(
                        <AssignmentCard id={assignment.id} status={assignment.state} assignee={assignment.assignee} application={assignment.application} onChange={this.load}/>
                )
            }
            return (
                <div className="applications-collapse">
                    {applicationListView}
                </div>
            )
        }
    }

}

