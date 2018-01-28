import React from 'react';
import { Route, BrowserRouter as Router, Switch, Redirect } from 'react-router-dom'
import Auth from './views/auth/'
import Home from './views/home/'
import Apply from './views/apply/'
import Review from './views/review/'
import Header from './components/header.js'
import Footer from './components/footer.js'

export default class Routes extends React.Component {
    render() {
      return (
        <span>
            <Router {...this.props}>
              <span>
              <Header isLoggedIn={isLoggedIn()}/>
                <div style={{"marginTop": "20px", "paddingBottom": "20px"}} className="container">
                  <Switch>
                    <Route path='/auth/' component={Auth} />
                    <Route exact path='/logout' render={({history}) => {
                      window.localStorage.removeItem("token")
                      return (
                        <Redirect to="/auth/" />
                      );
                    }} />
                    <PrivateRoute exact path='/' component={Home} />
                    <PrivateRoute path='/apply' component={Apply} />
                    <PrivateRoute path='/review' component={Review} />
                    <Route path="**" render={(props) =>{
                      return (<h1 className="display-4"><center><strong>404</strong> - Nicht Found</center></h1>)
                    }} />

                  </Switch>
                </div>
                </span>
            </Router>
          <Footer />
        </span>);
    }
}

const PrivateRoute = (props) => {
  if(isLoggedIn()) {
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
