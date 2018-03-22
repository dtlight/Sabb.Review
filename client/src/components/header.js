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
import withAdmin from "../AdminHOC";


class Header extends React.Component {
  constructor (props) {
    super(props);
    this.props = props;
    this.state = { navExpanded: false, isAdmin: props.isAdmin || false}
    this.toggleNav = this.toggleNav.bind(this);
    this.hideNave = this.hideNave.bind(this);
  }

    componentWillReceiveProps(nextProps) {
        this.setState({
            isAdmin: nextProps.isAdmin || false
        })
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
        <Brand />
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
              {this.state.isAdmin?<Nav className="ml-auto" navbar>
                <NavLink onClick={this.hideNave} to="/admin/department/1">Department Admin</NavLink>
              <UncontrolledDropdown nav inNavbar>
                <DropdownToggle nav caret>
                  Administration
                </DropdownToggle>
                <DropdownMenu>
                    <DropdownItem onClick={this.hideNave} exact to="/admin/">
                         Departments
                    </DropdownItem>

                  <DropdownItem onClick={this.hideNave} to="/admin/roles/">Roles
                  </DropdownItem>

                    <DropdownItem onClick={this.hideNave} to="/admin/users/">
                      Users
                      </DropdownItem>
                </DropdownMenu>
              </UncontrolledDropdown>
                <NavLink onClick={this.hideNave} to="/logout">Logout</NavLink>
            </Nav> :
                  <Nav className="ml-auto" navbar>
                      <NavLink onClick={this.hideNave} to="/logout">Logout</NavLink>
                  </Nav>}
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

let Brand = withRouter((props) => {
  return (<Link to={(!props.location.pathname.startsWith("/auth/"))?"/":"/auth/"}>
      <NavbarBrand >
          <img style={{"height": "40px"}} alt="logo" src="/brand/sabbreview.png"/>
      </NavbarBrand>
  </Link>);
})
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

export default withAdmin(Header);
