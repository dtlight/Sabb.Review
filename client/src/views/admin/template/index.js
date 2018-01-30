import React from 'react';
import {FieldList, NewQuestion} from '../../../components/application-builder/index.js';
import axios from 'axios';

export default class extends React.Component {
  constructor(props) {
    super(props);
    this.props = props;
    this.state = {
      title: ""
    }
    this.load = this.load.bind(this);
  }
  componentDidMount() {
    this.load();
  }
  load() {
    axios.get(`/template/${this.props.match.params.id}`).then(({data}) => {
      console.log(data);
      if(data.state === "STATUS_OK") {
        this.setState({
          id: data.value.id,
          title: data.value.name,
          fields: data.value.fieldList
        });
      }
    });
  }
  render() {
    return (
      <div>
        <h1 class="display-4"><strong>Edit Template:</strong> {this.state.title}</h1>
        <p class="lead">These are the questions that you can ask applicants for sabbatical leave.</p>
        <hr />
        <FieldList id={this.state.id} fields={this.state.fields} onChange={this.load} />
        <NewQuestion id={this.state.id} onChange={this.load} />
      </div>
    );
  }
}
