import React from 'react';
import {FieldList, NewQuestion} from '../../components/application-builder/index.js';

export default class extends React.Component {
  constructor(props) {
    super(props);
    this.props = props;
    this.state = {

    }
  }
  render() {
    return (
      <div>
        <h1 class="display-4">Application Fields</h1>
        <p class="lead">These are the questions that you can ask applicants for sabbatical leave.</p>
        <hr />
        <FieldList />

        <NewQuestion />
      </div>
    );
  }
}
