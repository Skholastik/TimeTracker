import {Component} from '@angular/core';
import {RouteConfig, Router} from '@angular/router-deprecated';

import {API_LogIn} from '../../globalServices/api/API_LogIn.service';


@Component({
  selector: 'log-in',
  pipes: [],
  providers: [API_LogIn],
  directives: [],
  styles: [require('./logIn.css')],
  template: require('./logIn.html')
})


export class LogIn {

  private username:string;
  private password:string;

  constructor(private api_LogIn:API_LogIn,
              private router:Router) {
  }

  authenticate() {
    let result:number;
    this.api_LogIn.authenticate(this.username, this.password).subscribe(
      data => {
        result = data;
        if (result == 200)
          this.router.navigateByUrl('/projects');
      },
      error => {
        console.log(error);
      }
    );
  }


  ngOnInit() {
  }
}

