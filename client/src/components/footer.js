import React from 'react';

export default class extends React.Component {
  render() {
    return (
      <div className="footer bg-primary">
          <div className="container">
              <span className="float-right text-muted" style={{"paddingTop":"3px"}}>Made with <i className={"fa fa-heart"} style={{"paddingRight": "0"}}></i> by Team 16, 2018</span>
        </div>
      </div>
    )
  }
}
