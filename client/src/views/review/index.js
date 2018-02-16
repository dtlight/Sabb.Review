import React from 'react';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';
import { resetIdCounter } from 'react-tabs';
import {  ApplicationList } from '../../components/review/'
import { Row } from 'reactstrap';
import axios from 'axios';

export default class extends React.Component {
  constructor(props, context){
    super(props, context);
    this.handleSelect = this.handleSelect.bind(this);
    this.state = {
      key: 1,
      assignments: [{
        assignee: "matthew@bargrove.com",
        application: {
          id: "0",
          state: "PENDING"
        }
      },
      {
        assignee: "matthew@bargrove.com",
        application: {
          id: "0",
          state: "REFUSED"
        }
      },
      {
        assignee: "matthew@bargrove.com",
        application: {
          id: "0",
          state: "COMPLETE"
        }
      }
      ]
    };
  }

  componentWillMount() {
    axios.get("/assignment/").then(({data}) => {
      this.setState({
        assignments: data.value
      });
    });
  }

  handleSelect(key){
    alert('selected ${key}');
    this.setState({ key });
  }

  render() {

    let choosenList = [];

    for(var k in this.state.assignments) {
      if(k.application.state === this.state.key) {
        choosenList.push(this.state.assignments[k]);
      }
    }
    return (<div>
              <h1 className="display-4">Assigned Reviews</h1>
                <p className="lead">These applications require your review.</p>
                <hr/>
                <Tabs>
                <TabList>
                  <Tab onClick={() => {
                    this.setState({
                      key: "PENDING"
                    })
                  }}>Pending Applications</Tab>
                  <Tab onClick={() => {
                    this.setState({
                      key: "COMPLETE"
                    })
                  }}>Complete Applications</Tab>
                  <Tab onClick={() => {
                    this.setState({
                      key: "REFUSED"
                    })
                  }}>Refused Applications</Tab>
                </TabList>
                <TabPanel>
                  <Row className="row applications-collapse">
                      <ApplicationList applications={choosenList} />
                  </Row>
                </TabPanel>
                </Tabs>
           </div>);
  }
}
