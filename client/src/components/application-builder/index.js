import React from 'react';
import {ListGroup,
   ListGroupItem,
   Card,
   CardBody,
   CardTitle,
   CardSubtitle,
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

let questionTypes = {
    "TEXT": {
      pretty: "Text",
      choice: false
    },
    "LONGTEXT": {
      pretty: "Paragraph",
      choice: false
    },
    "DATE": {
      pretty: "Date",
      choice: false
    },
    "MULTICHOICE": {
      pretty: "Multiple Choice",
      choice: true
    },
    "SINGLECHOICE": {
      pretty: "Single Choice",
      choice: true
    }
  };



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
          fieldType={field.type}
          onDelete={this.deleteField}
          onChange={this.props.onChange}
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

  defaultState =  {
    modal: false,
    title: "",
    type: "TEXT",
    answers: []
  };
  constructor(props) {
    super(props);
    this.props = props;
    if(this.props.questionId) {
      this.state = {
        questionId: this.props.questionId,
        title: this.props.title || "",
        type: this.props.type || "",
        answers: this.props.answers || []
      };
    } else {
      this.state = this.defaultState;
    }

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
    if(this.props.questionId) {
      axios.put(`/field`, {
            "id": this.props.questionId,
          	"title": this.state.title,
          	"type": this.state.type,
          	"fieldOptions": this.state.answers
          }).then((response) => {
            if(response.data.state === "STATUS_OK") {
              this.toggle();
              if(this.props.onChange){
                this.props.onChange();
              }
              /*this.defaultState.answers = [];
              this.setState(this.defaultState);
              */
            } else {
              alert(response.data.value);
            }
          });
    } else {
      axios.post(`/template/${this.props.templateId}/field`, {
          	"title": this.state.title,
          	"type": this.state.type,
          	"fieldOptions": this.state.answers
          }).then((response) => {
            if(response.data.state === "STATUS_OK") {
              this.toggle();
              if(this.props.onChange){
                this.props.onChange();
              }
              this.defaultState.answers = [];
              this.setState(this.defaultState);

            } else {
              alert(response.data.value);
            }
          });
    }

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

                            }}
                            onDeleteOption={(o) => {
                              this.setState((state) => {
                                state.answers.splice(o, 1);
                                return state;
                              })
                            }}
                            >{this.state.answers[i]}</OptionListItem>);
    }
    let typeOfInputs = [];
    for (var prop in questionTypes) {
        typeOfInputs.push(<TypeOfInputSelect value={prop}>{questionTypes[prop].pretty}</TypeOfInputSelect>)
    }
    console.log("type: "+this.state.type);
    return (
      <div className={this.props.className}>
        <Button color="secondary" onClick={this.toggle}>{this.props.children}</Button>
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
                console.log(e.target.value);
                this.setState({
                  type: e.target.value
                })
              }}>
                {typeOfInputs}
              </Input>
            </FormGroup>
            <ListGroup style={{"display": (questionTypes[this.state.type].choice)?"block":"none"}}>
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
       <InputGroupAddon addonType="append">
         <Button color="danger" onClick={() => {
           if(props.onDeleteOption) props.onDeleteOption(props.order)
         }}>
           <i class="fa fa-trash-o" aria-hidden="true"></i>
         </Button>
       </InputGroupAddon>
     </InputGroup>
    </ListGroupItem>
  )

}

let TypeOfInputSelect = (props) => {
  return(<option {...props}>{props.children}</option>)
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
        <NewQuestion className="float-right"
          templateId={1}
          questionId={props.id}
          title={props.title}
          type={props.fieldType}
          answers={props.fieldOptions}
          onChange={props.onChange}>
          Edit
        </NewQuestion>
        <Button className="float-right" color="danger" onClick={() => {
          if(props.onDelete) {
            props.onDelete(props.id)
          }
        }}>Delete</Button>

      </CardTitle>
      <CardSubtitle>
          {questionTypes[props.fieldType].pretty}
      </CardSubtitle>
    </CardBody>
    <ListGroup>
      {optionList}
    </ListGroup>
    </Card>
  )
}
