import {Component, ViewEncapsulation} from '@angular/core';
import {RouteConfig, Router} from '@angular/router-deprecated';

import {Projects} from './projects/projects.component';
import {DateFormatter} from '../globalServices/dateFromatter/dateFormatter.service';
import {API_User} from '../globalServices/api/API_User.service';

@Component({
  selector: 'time-tracker',
  pipes: [],
  providers: [DateFormatter, API_User],
  directives: [],
  encapsulation: ViewEncapsulation.Native,
  styles: [require('./timeTracker.css')],
  template: require('./timeTracker.html')

})
@RouteConfig([
  {path: '/projects', name: 'Projects', component: Projects, useAsDefault: true},
  {path: '/workDashBoard', name: 'WorkDashBoard', loader: () => require('es6-promise!./workDashBoard/workDashBoard.component')('WorkDashBoard')},
  {path: '/reports', name: 'Reports', loader: () => require('es6-promise!./reports/reports.component')('Reports')}
])
export class TimeTracker {

  private userName:string;

  constructor(private api_User:API_User,
              private router:Router) {
    /*  setInterval(() => {
     this.determinateValue += 1;
     if (this.determinateValue > 100)
     this.determinateValue = 1;
     }, 100, 0, true);*/
  }

  public ngOnInit():void {
    this.api_User.checkAccess().subscribe(
      data=> {
        this.userName = data.responseObjects.user.userName;
      },
      error=> {
        this.router.navigate(["Login"]);
      }
    );
  }

  public logout():void {
    this.api_User.logout().subscribe(
      data=> {
        if (data == 200)
          this.router.navigate(["Login"]);

      },
      error=> {
        console.log(error);
      }
    );
  }
}

