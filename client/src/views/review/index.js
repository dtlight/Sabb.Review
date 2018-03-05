import React from 'react';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';
import { resetIdCounter } from 'react-tabs';
import {  AssignmentList } from '../../components/review/'
import { Row } from 'reactstrap';

export default class extends React.Component {
  constructor(props, context){
    super(props, context);
  }

  render() {
    return (<div>
              <h1 class="display-4">Assigned Reviews</h1>
                <p class="lead">These applications require your review.</p>
                <hr/>
        <AssignmentList />

    </div>);
  }
}
