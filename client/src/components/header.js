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
  DropdownMenu } from 'reactstrap';


export default class extends React.Component {
  constructor (props) {
    super(props);
    this.props = props;
    this.state = { navExpanded: false }
    this.toggleNav = this.toggleNav.bind(this);
    this.hideNave = this.hideNave.bind(this);
  }

  toggleNav() {
    this.setState({ navExpanded: !this.state.navExpanded });
  }

  hideNave() {
    if(this.state.navExpanded) {
      this.setState({ navExpanded: !this.state.navExpanded });
    }
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
            <Nav className="mr-auto" navbar >
              <NavLink onClick={this.hideNave} to="/" exact>Home</NavLink>
              <NavLink onClick={this.hideNave} to="/apply">Apply</NavLink>
              <NavLink onClick={this.hideNave} to="/review">Review</NavLink>

            </Nav>
            <Nav className="ml-auto" navbar>
              <UncontrolledDropdown nav inNavbar>
                <DropdownToggle nav caret>
                  Administration
                </DropdownToggle>
                <DropdownMenu>
                    <DropdownItem onClick={this.hideNave} exact to="/admin/">
                        View Departments
                    </DropdownItem>

                  <DropdownItem onClick={this.hideNave} to="/admin/template/">
                    Edit Templates
                  </DropdownItem>

                </DropdownMenu>
              </UncontrolledDropdown>
                <NavLink onClick={this.hideNave} to="/logout">Logout</NavLink>
            </Nav>
          </IfLoggedIn>
        </Collapse>
      </Navbar>
    )
  }
}

let IfLoggedIn = withRouter((props) => {
  let {location} = props;
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


let DropdownItem = withRouter((props) => {
  const { location } = props;
 return (<Link className={((props.exact && location.pathname === props.to)
        || (!props.exact && location.pathname.startsWith(props.to)))?"active dropdown-item":"dropdown-item"} {...props} to={props.to}>{props.children}</Link>);
});

let NavLink = withRouter((props) => {
  const { location } = props;
  return (<li className={((props.exact && location.pathname === props.to)
      || (!props.exact && location.pathname.startsWith(props.to)))?"active nav-item":"nav-item"} {...props}><Link className="nav-link" to={props.to}>{props.children}</Link></li>)
});
