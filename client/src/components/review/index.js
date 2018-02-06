import React from 'react';
import { Link } from 'react-router-dom';
import { Card, Badge, CardText, CardBody,
    CardTitle, CardSubtitle, Button, Row } from 'reactstrap';
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
        buttonsVisible: true
    },
    REFUSED: {
        humanStatus: "Rejected",
        body: "This application has been rejected by the department or review group.",
        colours: "danger",
        buttonsVisible: true
    },
    SUBMITTED: {
        humanStatus: "Submitted",
        body: "The application has been submitted and is currently awaiting approval.",
        colours: "secondary",
        buttonsVisible: true
    }
}



export class ApplicationCard extends React.Component {
    constructor(props) {
        super(props);
        this.props = props;
        this.state = {
            id: this.props.id,
            status: this.props.status,
            applicationDate: "Tuesday, 16th March 2018"
        }
    }

    render() {
        return (
            <Card style={{"marginBottom": "20px", "height": "100%", "minHeight": "180px"}} className={`border-${applicationStates[this.state.status].colours}`}>
                <CardBody>
                    <CardTitle><h5>Application to the Computer Science dept. <Badge color={applicationStates[this.state.status].colours}>{applicationStates[this.state.status].humanStatus}</Badge></h5></CardTitle>
                    <CardSubtitle className="mb-2 text-muted">{this.state.applicationDate}</CardSubtitle>
                    <CardText>{[this.state.id]}</CardText>
                    <div class={(applicationStates[this.state.status].buttonsVisible)?"visible":"invisible"}>
                        <Link class="text-danger float-right" style={{"position": "absolute", "bottom": "0", "paddingBottom": "15px", "paddingRight": "20px", "right": "0"}} to={`/apply/${this.state.id}`}>View</Link>
                    </div>
                </CardBody>
            </Card>
        )
    }
}

export class ApplicationList extends React.Component {
    constructor(props) {
        super(props);
        this.props = props;
        this.state = {
            applicationList: [],
            isLoading: true
        }
        this.load = this.load.bind(this);
    }
    componentDidMount() {
        this.load();
    }

    load() {
        axios.get(`/user/applications`).then(({data})=> {
            this.setState({
                applicationList: data.value,
                isLoading: false
            })
            console.log(data);
        })
    }

    render() {
        if(this.state.isLoading) {
            return <div class="loader">Loading...</div>;
        } else {
            let applicationListView = [];
            for (let application of this.state.applicationList) {
                applicationListView.push(
                    <div class="col-lg-12">
                        <ApplicationCard id={application.id} status={application.state} onChange={this.load}/>
                    </div>
                )
            }
            return (
                <Row className="row applications-collapse">
                    {applicationListView}
                </Row>
            )
        }
    }

}
