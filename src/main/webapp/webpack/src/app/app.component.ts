import {Component, ViewEncapsulation} from '@angular/core';
import {RouteConfig, Router} from '@angular/router-deprecated';

import {Login} from './login/login.component';

@Component({
  selector: 'app',
  pipes: [ ],
  providers: [Login],
  directives: [  ],
  encapsulation: ViewEncapsulation.Native,
  styles: [require('./app.css')],
  template: require('./app.html')

})
@RouteConfig([
  { path: '/timeTracker/...', name: 'TimeTracker', loader: () => require('es6-promise!./timeTracker/timeTracker.component')('TimeTracker') },
  { path: '/login', name: 'Login',  component: Login, useAsDefault: true }
])

export class App {

  constructor() { }

  public ngOnInit():void {}
}

