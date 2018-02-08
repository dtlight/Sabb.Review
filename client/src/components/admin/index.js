import React from 'react'
import {Table} from 'reactstrap';
import axios from 'axios';
import {Link} from 'react-router-dom';
export class DepartmentList extends React.Component {
  constructor(props) {
    super(props);
    this.props = props;
    this.state = {
      departments: [],
    };
  }

  componentWillMount() {
    axios.get("/departments").then(({data}) => {
      if(data.state === "STATUS_OK") {
        this.setState(prevState => {
          return {
            departments: data.value
          }
        })
      }
    });
  }

  render() {
    let departments = [];
    for(let v of this.state.departments) {
      departments.push(
          <tr>
              <td>{v[1]}</td>
              <td ><Link to={`/admin/department/${v[0]}`} className={"text-secondary"}>View Department &raquo;</Link></td>
          </tr>);
    }
    return (
      <Table>
        <thead>
          <tr>
            <td>Department Name</td>
            <td>Actions</td>
          </tr>
        </thead>
        <tbody>
        {departments}
        </tbody>
      </Table>
    );
  }
}
