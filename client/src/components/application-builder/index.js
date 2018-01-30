import React from 'react';
import {ListGroup,
   ListGroupItem,
   Card,
   CardBody,
   CardTitle,
   FormGroup,
   Modal,
   ModalFooter,
   ModalBody,
   Button,
   ModalHeader,
   Input,
   InputGroupAddon,
   InputGroup,
    Label} from 'reactstrap';
import axios from 'axios';

export class FieldList extends React.Component {
  constructor(props) {
    super(props);
    this.props = props;
    this.state = {
    }
    this.deleteField = this.deleteField.bind(this);
  }

  deleteField(id) {
    axios.delete(`/template/${this.props.id}/field/${id}`).then(({data})=> {
      if(this.props.onChange){
        this.props.onChange();
      }
    })
  }

  render() {
    if(this.props.fields) {
      var visibleFieldList = [];
      for (var field of this.props.fields) {
        visibleFieldList.push(<Field id={field.id}
          title={field.title}
          fieldOptions={field.fieldOptions}
          onDelete={this.deleteField}
        />);
      }
      return (
        <p style={{"marginBottom": "20px"}}>
          {visibleFieldList}
        </p>
      )
    } else {
      return "";
    }

  }
}


export class NewQuestion extends React.Component {

  questionTypes = [
    {
      id: "TEXT",
      pretty: "Text",
      choice: false
    },
    {
      id: "DATE",
      pretty: "Date",
      choice: false
    },
    {
      id: "MULTICHOICE",
      pretty: "Multiple Choice",
      choice: true
    },
    {
      id: "SINGLECHOICE",
      pretty: "Single Choice",
      choice: true
    }
  ];

  defaultState =  {
    modal: false,
    title: "",
    type: 0,
    answers: []
  };
  constructor(props) {
    super(props);
    this.props = props;
    this.state = this.defaultState;
    this.toggle = this.toggle.bind(this);
    this.addAnswer = this.addAnswer.bind(this);
    this.submit = this.submit.bind(this);

  }
  toggle() {
    this.setState({
      modal: !this.state.modal
    });
  }
  addAnswer() {
    this.setState((state) => {
      state.answers.push({
        title: ""
      })
      return state;
    })
  }
  submit() {
    axios.post(`/template/${this.props.id}/field`, {
        	"title": this.state.title,
        	"type": this.questionTypes[this.state.type].id,
        	"fieldOptions": this.state.answers
        }).then((response) => {
          if(response.data.state === "STATUS_OK") {
            this.toggle();
            if(this.props.onChange){
              this.props.onChange();
            }
            this.setState(this.defaultState);
          } else {
            alert(response.data.value);
          }
        });
  }
  render() {
    let optionItems = [];
    for (var i = 0; i < this.state.answers.length; i++) {
      optionItems.push(<OptionListItem order={i}
                            onChange={(e, o) => {
                              this.setState((state) => {
                                state.answers[o].title = e
                                return state;
                              })
                            }}>{this.state.answers[i]}</OptionListItem>);
    }
    let typeOfInputs = [];
    for (var typeOfInput of this.questionTypes) {
        typeOfInputs.push(<TypeOfInputSelect>{typeOfInput.pretty}</TypeOfInputSelect>)
    }
    return (
      <div className={this.props.className}>
        <Button color="secondary" onClick={this.toggle}>Add Question</Button>
        <Modal isOpen={this.state.modal} toggle={this.toggle}>
          <ModalHeader toggle={this.toggle}>Add Question</ModalHeader>
          <ModalBody>
            <FormGroup>
              <label>Question Title</label>
              <Input onChange={(e) => {
                this.setState({
                  title: e.target.value
                })
              }} value={this.state.title} />
            </FormGroup>
            <FormGroup>
              <Label for="exampleSelect">Question Type</Label>
              <Input type="select" name="select" onChange={(e)=>{
                this.setState({
                  type: e.target.selectedIndex
                })
              }}>
                {typeOfInputs}
              </Input>
            </FormGroup>
            <ListGroup style={{"display": (this.questionTypes[this.state.type].choice)?"block":"none"}}>
              {optionItems}
              <ListGroupItem>
                <Button block color="secondary" onClick={this.addAnswer}><i class="fa fa-plus" aria-hidden="true"></i></Button>
              </ListGroupItem>
            </ListGroup>
            <ModalFooter>
              <Button color="primary" onClick={this.submit}>Save Question</Button>
            </ModalFooter>
        </ModalBody>
      </Modal>
    </div>
    )
  }
}


let OptionListItem = (props) => {
  return (
    <ListGroupItem>
      <InputGroup>
       <InputGroupAddon addonType="prepend">
         <span class="input-group-text">Choice {props.order+1}</span>
       </InputGroupAddon>
       <Input onChange={(e)=> {
         props.onChange(e.target.value, props.order)
       }} value={props.children.title}/>
     </InputGroup>
    </ListGroupItem>
  )

}

let TypeOfInputSelect = (props) => {
  return(<option >{props.children}</option>)
}

let Field = (props) => {
  let optionList = [];
  if(props.fieldOptions) {
    for (var option of props.fieldOptions) {
      optionList.push(<ListGroupItem>{option.title}</ListGroupItem>);
    }
  }
  return (
    <Card style={{"marginBottom": "10px"}} className="bg-light">
      <CardBody>
      <CardTitle>
        <small>{props.title}</small>
        <Button className="float-right" color="danger" onClick={() => {
          if(props.onDelete) {
            props.onDelete(props.id)
          }
        }}>Delete</Button>
      </CardTitle>
    </CardBody>
    <ListGroup>
      {optionList}
    </ListGroup>
    </Card>
  )
}
