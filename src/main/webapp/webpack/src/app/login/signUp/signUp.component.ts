import {Component} from '@angular/core';
import {RouteConfig, Router} from '@angular/router-deprecated';

@Component({
  selector: 'sign-up',
  pipes: [],
  providers: [],
  directives: [],
  styles: [require('./signUp.css')],
  template: require('./signUp.html')
})

export class SignUp {


  constructor() {
  }

  ngOnInit() {
    console.log('Вау!');
  }
}

