import React from 'react';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';
import { resetIdCounter } from 'react-tabs';
import { ApplicationCard } from '../../components/review/'
import { Jumbotron, Button, ButtonGroup, Row, Col } from 'reactstrap';

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
                <Tabs
                selectedIndex={this.state.tabIndex}
                onSelect={tabIndex => this.setState({ tabIndex })}
                >
                <TabList>
                  <Tab>New Applications</Tab>
                  <Tab>Open Applications</Tab>
                  <Tab>Past Applications</Tab>
                </TabList>
                <TabPanel>
                <Row className="row applications-collapse">
                  <div class="col-lg-12">
                    <ApplicationCard id="test0" status="SUCCESS"/>
                  </div>
                  <div class="col-lg-12">
                    <ApplicationCard id="test" status="PENDING"/>
                  </div>
                  <div class="col-lg-12">
                    <ApplicationCard id="test2" status="REFUSED"/>
                  </div>
                  <div class="col-lg-12">
                    <ApplicationCard id="test3" status="SUBMITTED"/>
                  </div>
                </Row>
                </TabPanel>
                <TabPanel>
                  <table class="table table-striped">
                    <thead>
                      <tr>
                        <th scope="col">#</th>
                        <th scope="col">Owner</th>
                        <th scope="col">State</th>
                        <th scope="col">Your comment</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr>
                        <th scope="row">1</th>
                        <td>Matt</td>
                        <td>Tun</td>
                        <td>pus</td>
                      </tr>
                      <tr>
                        <th scope="row">2</th>
                        <td>Route</td>
                        <td>Mubub</td>
                        <td>@rat</td>
                      </tr>
                      <tr>
                        <th scope="row">3</th>
                        <td>Spark</td>
                        <td>React</td>
                        <td>crap</td>
                      </tr>
                    </tbody>
                  </table>
                </TabPanel>
                <TabPanel>
                testing out for harambe
                </TabPanel>
                </Tabs>
           </div>);
  }
}
