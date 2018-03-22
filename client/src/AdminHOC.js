import React from 'react';

export default function withAdmin(WrappedComponent) {
    return class extends React.Component {
        constructor(props) {
            super(props);
        }

        componentWillReceiveProps() {
            this.setState({
                isAdmin: window.localStorage.getItem("isAdmin") === "true"
            }) ;
        }


        componentWillMount() {
            this.setState({
                isAdmin: window.localStorage.getItem("isAdmin") === "true"
            }) ;
        }

        render() {
            return <WrappedComponent isAdmin={this.state.isAdmin} {...this.props} />;
        }
    };
}
