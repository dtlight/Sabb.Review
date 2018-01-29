import React from 'react';
import { Link, withRouter } from 'react-router-dom'

export default class extends React.Component {
  constructor (props) {
    super(props);
    this.props = props;
    this.state = { navExpanded: false }
    this.toggleNav = this.toggleNav.bind(this);
    this.hideNav = this.hideNav.bind(this);
  }

  toggleNav() {
    this.setState({ navExpanded: !this.state.navExpanded });
  }

  hideNav() {
    this.setState({ navExpanded: false });
  }

  render() {
    return (
      <nav className="navbar navbar-expand-lg navbar-dark bg-primary">
        <Link to="/" className="navbar-brand">
          <img style={{"height": "40px"}} alt="logo" src="/brand/sabbreview.png"/>
        </Link>
        <IfLoggedIn>
          <button class="navbar-toggler" type="button" onClick={this.toggleNav} aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
          </button>
        </IfLoggedIn>

        <div onClick={this.hideNav} class={(this.state.navExpanded)?"collapse navbar-collapse show":"collapse navbar-collapse"} id="navbarSupportedContent">
          <IfLoggedIn>
            <ul class="navbar-nav mr-auto">
              <NavLink to="/" exact>Home</NavLink>
              <NavLink to="/apply">Apply</NavLink>
              <NavLink to="/review">Review</NavLink>
            </ul>
            <ul class="navbar-nav ml-auto">
              <li class="nav-item  ">
                <Link to="/logout" class="nav-link">Logout</Link>
              </li>
            </ul>
          </IfLoggedIn>

        </div>
      </nav>
    )
  }
}

let IfLoggedIn = withRouter((props) => {
  let {location} = props;
  console.log(location);
  if(!location.pathname.startsWith("/auth/")) {
    return (
      props.children
    );
  } else {
    return (
      ""
    );
  }
});

let NavLink = withRouter((props) => {
  const { location } = props;
  return (<li className={((props.exact && location.pathname === props.to)
      || (!props.exact && location.pathname.startsWith(props.to)))?"active nav-item":"nav-item"} {...props}><Link className="nav-link" to={props.to}>{props.children}</Link></li>)
});
