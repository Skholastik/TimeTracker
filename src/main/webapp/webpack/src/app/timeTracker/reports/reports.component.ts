import {Component,ViewEncapsulation} from '@angular/core';

import {API_Project} from '../../globalServices/api/API_Project.service';
import {API_Task} from '../../globalServices/api/API_Task.service';
import {ProjectDTO} from '../../globalServices/entitiesDTO/projectDTO.class';
import {TaskDTO} from '../../globalServices/entitiesDTO/taskDTO.class';
import {ReportDTO} from '../../globalServices/entitiesDTO/reportDTO.class';
import {UserDTO} from '../../globalServices/entitiesDTO/userDTO.class';
import {DateFormatter} from '../../globalServices/dateFromatter/dateFormatter.service';


@Component({
  selector: 'reports',
  providers: [API_Project,API_Task],
  directives: [],
  pipes: [],
  encapsulation: ViewEncapsulation.Native,
  styles: [require('./reports.css')],
  template: require('./reports.html')
})
export class Reports {

  private reportTypeList:string[]=['Проект','Задача'];
  private selectedType:string;
  private projectList:ProjectDTO[]=[];


  constructor(private api_Project:API_Project,
              private api_Task:API_Task,
              private dateFormatter:DateFormatter) {

  }

  public ngOnInit():void{
      this.getCurrentUserProjects();
  }

  public reportTypeSelected(){
     if(this.selectedType==this.reportTypeList[0])
       this.getCurrentUserProjects();
    if(this.selectedType==this.reportTypeList[1])
      this.getCurrentUserTaskList();
  }


  public getCurrentUserProjects():void{
    this.api_Project.getCreatedProjectList(this.dateFormatter.getUtcOffset()).subscribe(
      data => {
        console.log(data);
        this.pushTransferProjectListToProject(data.responseObjects.projectList);
      },
      error => {

      }
    );
  }

  public pushTransferProjectListToProject(data:any[]):void {
    for (let i = 0; i < data.length; i++) {
      let newProjectDTO = new ProjectDTO();
      newProjectDTO.fillFromJSON(JSON.stringify(data[i]));
      newProjectDTO.creationDateTime = this.dateFormatter
        .changeDateTimeToRuWithPattern(newProjectDTO.creationDateTime, "Do MMMM YYYY");
      this.projectList.push(newProjectDTO);
    }
  }


  public getCurrentUserTaskList():void{
    this.api_Task.getCreatedTaskList(this.dateFormatter.getUtcOffset()).subscribe(
      data => {
        console.log(data);
        this.pushTransferProjectListToProject(data.responseObjects.projectList);
      },
      error => {
        console.log(error);
      }
    );
  }

}
