import React from 'react';
import {Router, Switch, Route} from 'react-router-dom'
import LoginComponent from '../../components/user/loginform.js'
import RegisterComponent from '../../components/user/registrationform.js'

export default class Authentication extends React.Component {
    render() {
      return (<div className="container">
        <Router {...this.props}>
          <Switch>
            <Route exact path='/auth/' component={LoginComponent}/>
            <Route path='/auth/register' component={RegisterComponent}/>
          </Switch>
        </Router>
      </div>);
    }
}
