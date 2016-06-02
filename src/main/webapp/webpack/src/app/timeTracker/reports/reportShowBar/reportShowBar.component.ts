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

  @Input() reportProjectList:ProjectDTO[];
  @Input() reportCreated:string;
  private showErrorMessage = false;

  constructor() {
  }

  public ngOnInit():void {
  }

  public ngDoCheck ():void {

    if (this.reportCreated == 'true') {
      if (this.reportProjectList.length != 0)
        this.showErrorMessage = false;
      else
        this.showErrorMessage = true;
    }
    else
      this.showErrorMessage = false;
  }

}
