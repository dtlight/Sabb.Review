import React from 'react'
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs'
import 'react-tabs/style/react-tabs.css';

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
                </TabList>
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
                        <td>Mark</td>
                        <td>Otto</td>
                        <td>@mdo</td>
                      </tr>
                      <tr>
                        <th scope="row">2</th>
                        <td>Jacob</td>
                        <td>Thornton</td>
                        <td>@fat</td>
                      </tr>
                      <tr>
                        <th scope="row">3</th>
                        <td>Larry</td>
                        <td>the Bird</td>
                        <td>@twitter</td>
                      </tr>
                    </tbody>
                  </table>
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
                </Tabs>
           </div>)
  }
}
