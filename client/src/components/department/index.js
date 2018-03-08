import React from 'react';
import axios from 'axios';

export class DepartmentList extends React.Component {
    constructor(props) {
        super(props);
        this.props = props;
        this.state = {
            isError: false,
            isSuccess: false,
            isLoading: true,
            isCreating: false,
            selectedDepartment: -1,
            selectedTemplate: -1,
            departmentList: [],
            templateList:[]
        };
        this.loadDepartments = this.loadDepartments.bind(this);
    }
    componentWillMount() {
        this.loadDepartments();
    }

    loadDepartments() {
        axios.get("/departments").then(({data})=> {
            if(this.props.onLoaded) this.props.onLoaded();
            this.setState({
                isLoading: false,
                departmentList: data.value,
                selectedDepartment: (!data.value[0])?-1:data.value[0][0]
            });
            this.props.selectDepartment((!data.value[0])?-1:data.value[0][0]);

        })
    }


    render() {
        let departmentList = [];

        for (let i = 0; i < this.state.departmentList.length; i++) {
            const department = this.state.departmentList[i];
            departmentList.push(
                <option value={department[0]}>{department[1]}</option>
            )
        }
        return (
            <select class="form-control" onChange={(e) => {
                this.props.selectDepartment(e.target.value);
            }}>
                {departmentList}
            </select>
        )
    }
}
