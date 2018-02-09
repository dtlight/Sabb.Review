import React from 'react'
import {  ApplicationList } from '../../components/review/'
import './style.css'

export default class extends React.Component {
  render() {
    return (
        <div>
            <h1 class="display-4">Assigned Reviews</h1>
              <p class="lead">These applications require your review.</p>
              <hr />
            <ApplicationList />
         </div>)
  }
}
