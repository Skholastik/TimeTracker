import {Component,ViewEncapsulation} from '@angular/core';

import {API_Project} from '../../globalServices/api/API_Project.service';

@Component({
  selector: 'reports',
  providers: [API_Project],
  directives: [],
  pipes: [],
  encapsulation: ViewEncapsulation.Native,
  styles: [require('./reports.css')],
  template: require('./reports.html')
})
export class Reports {


  constructor(private api_Project:API_Project) {

  }

}
