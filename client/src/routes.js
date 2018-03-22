import React from 'react';
import {
    Route,
    BrowserRouter as Router,
    Switch,
    Redirect
} from 'react-router-dom'
import Auth from './views/auth/'
import Home from './views/home/'
import {
    Introduction,
    EditExisting
} from './views/apply/'

import Review, {
    ReviewDetail
} from './views/review/'
import Header from './components/header.js'
import Footer from './components/footer.js'

import {
    TemplateList,
    ViewTemplate
} from './views/admin/template/';

import {
    ViewRole,
    ViewRoles
} from './views/admin/role/';
import Admin from './views/admin/home/';
import {
    DepartmentInfo
} from './views/admin/department/';
import Users from "./views/admin/user";
import withAdmin from './AdminHOC.js';

export default class Routes extends React.Component {
    state = {
        sessionCount: 0
    }
    render() {
        return (
            <span>
                <Router {...this.props}>
                  <span>
                      <Header isLoggedIn={isLoggedIn} sessionCount={this.state.sessionCount}/>
                      <Switch>
                         <PrivateRoute exact path='/apply/:id' component={EditExisting} />
                         <PrivateRoute path='/review/:id' component={ReviewDetail} />

                         <div style={{"marginTop": "20px", "paddingBottom": "20px"}} className="container">

                           <Route path='/auth/' component={(props) => {
                               return (
                                   <Auth {...props} onAuthChange={() => {
                                       this.setState({
                                           sessionCount: this.state.sessionCount++
                                       });
                                   }}/>)
                           }} />
                           <Route exact path='/logout' render={({history}) => {
                               window.localStorage.removeItem("token")
                               window.localStorage.removeItem("isAdmin")

                               return (
                                   <Redirect to="/auth/" />
                               );
                           }} />
                           <PrivateRoute exact path='/' component={Home} />
                           <PrivateRoute exact path='/apply' component={Introduction} />

                           <PrivateRoute exact path='/review' component={Review} />

                           <PrivateRoute exact path='/admin/department/:id' component={DepartmentInfo} />
                           <PrivateRoute exact path='/admin/' component={Admin} />
                           <PrivateRoute exact path='/admin/roles' component={ViewRoles} />
                             <PrivateRoute exact path='/admin/users' component={Users} />

                           <PrivateRoute path='/admin/template/:id' component={ViewTemplate} />

                        </div>
                        <Route path="**" render={() =>{
                            return (<h1 className="display-4"><center><strong>404</strong> - Nicht Found</center></h1>)
                        }} />
                      </Switch>
                    </span>
                </Router>
          <Footer />
        </span>);
    }
}

const PrivateRoute = (props) => {
    if (isLoggedIn()) {
        return (
            <span>
        <Route {...props} />
      </span>
        );
    } else {
        return (
            <Redirect to={{ pathname: '/auth/', state: { from: props.location} }} />
        );
    }
};

let isLoggedIn = () => {
    return window.localStorage.getItem("token") !== null;
}
