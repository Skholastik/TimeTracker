import {Component, ViewEncapsulation} from '@angular/core';
import {RouteConfig, Router} from '@angular/router-deprecated';

import {TimeTracker} from './timeTracker/timeTracker.component';

@Component({
  selector: 'app',
  pipes: [ ],
  providers: [TimeTracker],
  directives: [  ],
  encapsulation: ViewEncapsulation.Native,
  styles: [require('./app.css')],
  template: require('./app.html')

})
@RouteConfig([
  { path: '/...',  name: 'TimeTracker',  component: TimeTracker, useAsDefault: true },
  { path: '/login', name: 'Login', loader: () => require('es6-promise!./login/login.component')('Login') }
])
export class App {

  constructor() {
  /*  setInterval(() => {
      this.determinateValue += 1;
      if (this.determinateValue > 100)
        this.determinateValue = 1;
    }, 100, 0, true);*/
  }

  ngOnInit() {}
}

