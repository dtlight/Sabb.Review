import React from 'react';
import {ListGroup, ListGroupItem, FormGroup, Modal, ModalFooter, ModalBody, Button, ModalHeader} from 'reactstrap';
export class FieldList extends React.Component {
  constructor(props) {
    super(props);
    this.props = props;
    this.state = {

    }
  }
  render() {
    return (
      <ListGroup style={{"marginBottom": "20px"}}>
        <Field title="test" fieldOptions={[
          {
            title: "test"
          },
          {
            title: "test2"
          }
        ]}/>
      </ListGroup>
    )
  }
}


export class NewQuestion extends React.Component {
  constructor(props) {
    super(props);
    this.props = props;
    this.state = {
      modal: false
    };
    this.toggle = this.toggle.bind(this);

  }
  toggle() {
    this.setState({
      modal: !this.state.modal
    });
  }
  render() {
    return (
      <div className={this.props.className}>
        <Button color="secondary" onClick={this.toggle}>Add Question</Button>
        <Modal isOpen={this.state.modal}  toggle={this.toggle}>
          <ModalHeader toggle={this.toggle}>Add Question</ModalHeader>
          <ModalBody>
            <FormGroup>
              <label>Question Title</label>
              <input type="text" class="form-control" placeholder="" />
            </FormGroup>
            <ModalFooter>
              <Button color="primary" onClick={this.toggle}>Save Question</Button>
            </ModalFooter>
        </ModalBody>
      </Modal>
    </div>
    )
  }
}



let Field = (props) => {
  let optionList = [];
  if(props.fieldOptions) {
    for (var option of props.fieldOptions) {
      optionList.push(<li>{option.title}</li>);
    }
  }
  return (
    <ListGroupItem>
      <strong>{props.title}</strong>
      <hr/>
      Question Choices:
      <ul>{optionList}</ul>
      <a href="" class="">Add Choice</a>
    </ListGroupItem>
  )
}
