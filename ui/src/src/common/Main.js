import React, { Component } from 'react';
import { Switch, Route } from 'react-router-dom'
import TestCases from '../testcases/TestCases'
import TestSuites from '../testsuites/TestSuites'
import Projects from '../projects/Projects'
import ProjectForm from '../projects/ProjectForm'
import Project from '../projects/Project'
import Launches from '../launches/Launches'
import Attributes from '../attributes/Attributes'
import TestCaseForm from '../testcases/TestCaseForm'
import TestCase from '../testcases/TestCase'

class Main extends Component {

    onProjectChange(project){
        this.props.onProjectChange(project)
    }

    render() {
        return(
            <main>
                <Switch>
                  <Route exact path='/' component={Projects}/>
                  <Route exact path='/projects' component={Projects}/>
                  <Route exact path='/projects/new' component={ProjectForm}/>

                  <Route path='/:project/testcases/new'
                      render={(props) => <TestCaseForm {...props}  onProjectChange={this.onProjectChange.bind(this)} /> }/>
                  <Route path='/projects/:project'
                      render={(props) => <Project {...props}  onProjectChange={this.onProjectChange.bind(this)} /> }/>
                  <Route path='/:project/testcases/:testcase'
                      render={(props) => <TestCase {...props}  onProjectChange={this.onProjectChange.bind(this)} /> }/>
                  <Route path='/:project/testcases'
                      render={(props) => <TestCases {...props}  onProjectChange={this.onProjectChange.bind(this)} /> }/>
                  <Route path='/:project/testsuites'
                      render={(props) => <TestSuites {...props}  onProjectChange={this.onProjectChange.bind(this)} /> }/>
                  <Route path='/:project/launches'
                      render={(props) => <Launches {...props}  onProjectChange={this.onProjectChange.bind(this)} /> }/>
                  <Route path='/:project/attributes'
                      render={(props) => <Attributes {...props} onProjectChange={this.onProjectChange.bind(this)} /> }/>

                </Switch>
            </main>
        );
    }
}

export default Main;