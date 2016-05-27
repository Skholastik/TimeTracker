import {Component,ViewEncapsulation,Input} from '@angular/core';

import {ProjectDTO} from '../../../globalServices/entitiesDTO/projectDTO.class';
import {TaskDTO} from '../../../globalServices/entitiesDTO/taskDTO.class';
import {ReportDTO} from '../../../globalServices/entitiesDTO/reportDTO.class';
import {UserDTO} from '../../../globalServices/entitiesDTO/userDTO.class';


@Component({
  selector: 'report-show-bar',
  providers: [],
  directives: [],
  pipes: [],
  encapsulation: ViewEncapsulation.Native,
  styles: [require('./reportShowBar.css')],
  template: require('./reportShowBar.html')
})
export class ReportShowBar {

  @Input() reportList:any;
  private reportListDTO:ReportDTO;

  constructor() {

  }

  public ngOnInit():void {

  }
}
