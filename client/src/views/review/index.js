import React from 'react';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';
import { resetIdCounter } from 'react-tabs';
import {  ApplicationList } from '../../components/review/'
import { Row } from 'reactstrap';

export default class extends React.Component {
  constructor(props, context){
    super(props, context);
    this.handleSelect = this.handleSelect.bind(this);
    this.state = {
      key: 1
    };
  }
  handleSelect(key){
    alert('selected ${key}');
    this.setState({ key });
  }

  render() {
    return (<div>
              <h1 class="display-4">Assigned Reviews</h1>
                <p class="lead">These applications require your review.</p>
                <hr/>
                <Tabs>
                <TabList>
                  <Tab>New Applications</Tab>
                  <Tab>Open Applications</Tab>
                  <Tab>Past Applications</Tab>
                </TabList>
                <TabPanel>
                  <Row className="row applications-collapse">
                      <ApplicationList />
                  </Row>
                </TabPanel>
                <TabPanel>
                  <Row className="row applications-collapse">
                      <ApplicationList />
                  </Row>
                </TabPanel>
                <TabPanel>
                  <Row className="row applications-collapse">
                      <ApplicationList />
                  </Row>
                </TabPanel>
                </Tabs>
           </div>);
  }
}
