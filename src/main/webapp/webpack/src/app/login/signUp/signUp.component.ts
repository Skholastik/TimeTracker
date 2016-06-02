import {Component} from '@angular/core';
import {RouteConfig, Router} from '@angular/router-deprecated';
import {ControlGroup,FormBuilder,Validators} from '@angular/common';

import {API_User} from '../../globalServices/api/API_User.service';


@Component({
  selector: 'sign-up',
  pipes: [],
  providers: [API_User],
  directives: [],
  styles: [require('./signUp.css')],
  template: require('./signUp.html')
})

export class SignUp {

  private signUpForm:ControlGroup;


  constructor(private api_User:API_User,
              private router:Router,
              private formBuilder:FormBuilder) {
  }

  public ngOnInit():void {
    this.setupSighUpForm();
  }


  public setupSighUpForm():void {
    this.signUpForm = this.formBuilder.group({
      'userName': ['', Validators.required],
      'email': ['', Validators.required],
      'password': ['', Validators.required],
    });
  }

  public sighUp(value:any):void {
    this.api_User.signUp(value.userName, value.email, value.password).subscribe(
      data=> {
        this.authenticate(value.userName, value.password);
      },
      error => {
        console.log(error);
      }
    );
  }

  public authenticate(userName:string, password:string):void {
    let result:number;
    this.api_User.authenticate(userName, password).subscribe(
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
}

