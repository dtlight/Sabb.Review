import React from 'react';
import { Link, withRouter } from 'react-router-dom'
import {
  Collapse,
  Navbar,
  NavbarToggler,
  NavbarBrand,
  Nav,
  NavItem,
  UncontrolledDropdown,
  DropdownToggle,
  DropdownMenu,
  DropdownItem } from 'reactstrap';


export default class extends React.Component {
  constructor (props) {
    super(props);
    this.props = props;
    this.state = { navExpanded: false }
    this.toggleNav = this.toggleNav.bind(this);
  }

  toggleNav() {
    this.setState({ navExpanded: !this.state.navExpanded });
  }

  render() {
    return (
      <Navbar dark color="primary" expand="lg">
        <Link to="/">
           <NavbarBrand >
            <img style={{"height": "40px"}} alt="logo" src="/brand/sabbreview.png"/>
          </NavbarBrand>
        </Link>
        <IfLoggedIn>
          <NavbarToggler onClick={this.toggleNav} />
        </IfLoggedIn>

        <Collapse isOpen={this.state.navExpanded} navbar>
          <IfLoggedIn>
            <Nav className="mr-auto" navbar>
              <NavLink to="/" exact>Home</NavLink>
              <NavLink to="/apply">Apply</NavLink>
              <NavLink to="/review">Review</NavLink>
            </Nav>
            <Nav className="ml-auto" navbar>
                <NavLink to="/logout">Logout</NavLink>
            </Nav>
          </IfLoggedIn>
        </Collapse>
      </Navbar>
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
