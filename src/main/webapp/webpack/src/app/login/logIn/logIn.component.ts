import {Component} from '@angular/core';
import {RouteConfig, Router} from '@angular/router-deprecated';

import {API_User} from '../../globalServices/api/API_User.service';


@Component({
  selector: 'log-in',
  pipes: [],
  providers: [API_User],
  directives: [],
  styles: [require('./logIn.css')],
  template: require('./logIn.html')
})


export class LogIn {

  private username:string;
  private password:string;

  constructor(private api_User:API_User,
              private router:Router) {
  }

  public authenticate():void {
    let result:number;
    this.api_User.authenticate(this.username, this.password).subscribe(
      data => {
        result = data;
        if (result == 200)
          this.router.navigate(['TimeTracker']);
      },
      error => {
        console.log(error);
      }
    );
  }


  public ngOnInit():void {
    this.api_User.checkAccess().subscribe(
      data=> {
        this.router.navigate(["TimeTracker"]);
      },
      error=> {
      }
    );
  }
}

