import {Component, ViewEncapsulation} from '@angular/core';
import {RouteConfig, Router} from '@angular/router-deprecated';

import {WorkDashBoard} from './workDashBoard/workDashBoard.component';
import {Projects} from './projects/projects.component';
import {DateFormatter} from '../globalServices/dateFromatter/dateFormatter.service';


@Component({
  selector: 'time-tracker',
  pipes: [],
  providers: [WorkDashBoard,DateFormatter],
  directives: [],
  encapsulation: ViewEncapsulation.Native,
  styles: [require('./timeTracker.css')],
  template: require('./timeTracker.html')

})
@RouteConfig([
  {path: '/workDashBoard', name: 'WorkDashBoard', component: WorkDashBoard, useAsDefault: true},
  { path: '/projects', name: 'Projects', loader: () => require('es6-promise!./projects/projects.component')('Projects') },
  { path: '/reports', name: 'Reports', loader: () => require('es6-promise!./reports/reports.component')('Reports') }
])
export class TimeTracker {

  constructor() {
    /*  setInterval(() => {
     this.determinateValue += 1;
     if (this.determinateValue > 100)
     this.determinateValue = 1;
     }, 100, 0, true);*/
  }

  ngOnInit() {
  }
}

