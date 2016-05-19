import {Component,ViewEncapsulation} from '@angular/core';

import {API_Project} from '../../globalServices/api/API_Project.service';

@Component({
  selector: 'work-dash-board',
  providers: [API_Project],
  directives: [],
  pipes: [],
  encapsulation: ViewEncapsulation.Native,
  styles: [require('./workDashBoard.css')],
  template: require('./workDashBoard.html')
})
export class WorkDashBoard {


  constructor(private api_Project:API_Project) {

  }

}
