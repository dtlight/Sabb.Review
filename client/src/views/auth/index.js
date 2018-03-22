import React from 'react';
import {Router, Switch, Route} from 'react-router-dom'
import LoginComponent from '../../components/user/loginform.js'
import RegisterComponent from '../../components/user/registrationform.js'
import { Container } from 'reactstrap';

export default class Authentication extends React.Component {
    render() {
      return (
        <Container>
          <Router {...this.props}>
            <Switch>
              <Route exact path='/auth/' render={(authProps) => <LoginComponent {...authProps} onAuthChange={this.props.onAuthChange}/>}/>
              <Route path='/auth/register' component={RegisterComponent}/>
            </Switch>
          </Router>
      </Container>);
    }
}
