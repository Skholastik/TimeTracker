import {Component,ViewEncapsulation} from '@angular/core';
import {RouteConfig, Router} from '@angular/router-deprecated';

import {LogIn} from './logIn/logIn.component';
import {SignUp} from './signUp/signUp.component';

@Component({
  selector: 'login',
  pipes: [],
  providers: [],
  directives: [LogIn,SignUp],
  encapsulation: ViewEncapsulation.Native,
  styles: [require('./login.css')],
  template: require('./login.html')
})

export class Login {

  private showLogIn:boolean=true;
  private showSignUp:boolean=false;

  private logInClass:string='tab active';
  private signUpClass:string='tab';


  constructor() {
  }

  public ngOnInit():void {

  }

  public changeMenu(menu:string):void {
    if(menu==='LogIn'){
      this.showLogIn=true;
      this.showSignUp=false;
      this.logInClass='tab active';
      this.signUpClass='tab';
    }
    else{
      this.showLogIn=false;
      this.showSignUp=true;
      this.logInClass='tab';
      this.signUpClass='tab active';
    }
  }
}

