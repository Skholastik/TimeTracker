import {Component,ViewEncapsulation,EventEmitter} from '@angular/core';
import {ControlGroup,FormBuilder,Validators} from '@angular/common';

import {API_Report} from '../../globalServices/api/API_Report.service';
import {API_Project} from '../../globalServices/api/API_Project.service';
import {API_Task} from '../../globalServices/api/API_Task.service';
import {API_User} from '../../globalServices/api/API_User.service';
import {ProjectDTO} from '../../globalServices/entitiesDTO/projectDTO.class';
import {TaskDTO} from '../../globalServices/entitiesDTO/taskDTO.class';
import {ReportDTO} from '../../globalServices/entitiesDTO/reportDTO.class';
import {ReporterDTO} from '../../globalServices/entitiesDTO/reporterDTO.class';
import {UserDTO} from '../../globalServices/entitiesDTO/userDTO.class';


import {DateFormatter} from '../../globalServices/dateFromatter/dateFormatter.service';
import {ReportShowBar} from './reportShowBar/reportShowBar.component';


@Component({
  selector: 'reports',
  providers: [API_Project, API_Task, API_User, API_Report],
  directives: [ReportShowBar],
  pipes: [],
  encapsulation: ViewEncapsulation.Native,
  styles: [require('./reports.css')],
  template: require('./reports.html')
})
export class Reports {

  private reportTypeList:string[] = ['Проект', 'Задача'];
  private projectList:ProjectDTO[] = [];
  private taskList:TaskDTO[] = [];
  private participantList:UserDTO[] = [];
  private reportProjectList:ProjectDTO[] = [];
  private reportCreated:string='false';

  private reportForm:ControlGroup;

  constructor(private api_Report:API_Report,
              private api_Project:API_Project,
              private api_Task:API_Task,
              private api_User:API_User,
              private dateFormatter:DateFormatter,
              private formBuilder:FormBuilder) {

  }

  public ngOnInit():void {
    this.reportForm = this.formBuilder.group({
      'reportType': ['', Validators.required],
      'projectOrTaskId': [''],
      'userId': [''],
      'startDate': [''],
      'endDate': [''],
    });
  }

  public getReport(value:any):void {

    if(this.reportProjectList.length!=0)
      this.reportProjectList.splice(0);

    if (this.participantList.length != 0){
      this.api_Report.getReportList(this.dateFormatter.getUtcOffset(),value.reportType, value.projectOrTaskId,
        value.userId, value.startDate, value.endDate).subscribe(
        data => {
          this.fillDetailReportProjectList(data.responseObjects.reportProjectList);
          this.reportCreated='true';
        },
        error => {
          console.log(error);
        }
      );
    }

    else{

      if (value.reportType == this.reportTypeList[0])
        console.log('Отчеты по проектам отсутствуют, т.к нету исполнителей');
      else
        console.log('Отчеты по задачам отсутствуют, т.к нету исполнителей');
    }
  }

  /** Загружает массив доступных проектов либо заданий для данного пользователя,
   * в зависимости от выбранного типа отчета. Так же получает список пользователей,
   * доступных для просмотра отчета*/
  public getProjectOrTaskList(reportType:string):void {
    this.participantList.splice(0);
    this.getProjectParticipant();

    if (reportType === this.reportTypeList[0]
      && this.projectList.length == 0)
      this.getProjectList();

    if (reportType === this.reportTypeList[1]
      && this.taskList.length == 0)
      this.getTaskList();
  }

  public getProjectList() {

    this.api_Project.getCreatedProjectList(this.dateFormatter.getUtcOffset()).subscribe(
      data => {
        this.pushTransferProjectListToDTO(data.responseObjects.projectList);
      },
      error => {
        console.log(error);
      }
    );
  }

  public getTaskList() {

    this.api_Task.getCreatedTaskList(this.dateFormatter.getUtcOffset()).subscribe(
      data => {
        this.pushTransferTaskListToDTO(data.responseObjects.taskList);
      },
      error => {
        console.log(error);
      }
    );

  }

  public pushTransferProjectListToDTO(data:any[]):void {
    for (let i = 0; i < data.length; i++) {
      let newProjectDTO = new ProjectDTO();
      newProjectDTO.fillFromJSON(JSON.stringify(data[i]));
      newProjectDTO.creationDateTime = this.dateFormatter
        .changeDateTimeToRuWithPattern(newProjectDTO.creationDateTime, "Do MMMM YYYY");
      this.projectList.push(newProjectDTO);
    }
  }

  public pushTransferTaskListToDTO(data:any[]):void {
    for (let i = 0; i < data.length; i++) {
      let newTaskDTO = new TaskDTO();
      newTaskDTO.fillFromJSON(JSON.stringify(data[i]));
      newTaskDTO.creationDateTime = this.dateFormatter
        .changeDateTimeToRuWithPattern(newTaskDTO.creationDateTime, "Do MMMM YYYY");
      this.taskList.push(newTaskDTO);
    }
  }

  public getParticipantUserList(reportType:string, projectOrTaskId:string):void {
    this.participantList.splice(0);

    if (projectOrTaskId.length != 0) {
      if (reportType == this.reportTypeList[0])
        this.getProjectParticipant(projectOrTaskId);

      if (reportType == this.reportTypeList[1])
        this.getTaskParticipant(projectOrTaskId);
    }
    else
      this.getProjectParticipant();
  }

  public getProjectParticipant(projectId?:string):void {
    this.api_User.getParticipantProjectUserList(projectId).subscribe(
      data => {
        console.log(data);
        for (let i = 0; i < data.responseObjects.userList.length; i++) {
          let newUserDTO = new UserDTO();
          newUserDTO.fillFromJSON(JSON.stringify(data.responseObjects.userList[i]));
          this.participantList.push(newUserDTO);
        }

      },
      error => {
        console.log(error);
      }
    );
  }

  public getTaskParticipant(taskId:string):void {
    this.api_User.getParticipantTaskUserList(taskId).subscribe(
      data => {
        console.log(data);
        for (let i = 0; i < data.responseObjects.userList.length; i++) {
          let newUserDTO = new UserDTO();
          newUserDTO.fillFromJSON(JSON.stringify(data.responseObjects.userList[i]));
          this.participantList.push(newUserDTO);
        }
      },
      error => {
        console.log(error);
      }
    );
  }

  public fillDetailReportProjectList(data:any[]) {
    for (let i = 0; i < data.length; i++) {
      let newProjectDTO:ProjectDTO = new ProjectDTO();
      newProjectDTO.id = data[i].id;
      newProjectDTO.name = data[i].name;

      for (let x = 0; x < data[i].taskList.length; x++) {
        let newTaskDTO:TaskDTO = new TaskDTO();
        newTaskDTO.id = data[i].taskList[x].id;
        newTaskDTO.name = data[i].taskList[x].name;

        for (let z = 0; z < data[i].taskList[x].reporterList.length; z++) {
          let newReporterDTO:ReporterDTO = new ReporterDTO();
          newReporterDTO.id = data[i].taskList[x].reporterList[z].id;
          newReporterDTO.name = data[i].taskList[x].reporterList[z].name;
          newReporterDTO.taskElapsedTime = data[i].taskList[x].reporterList[z].taskElapsedTime;

          for (let y = 0; y < data[i].taskList[x].reporterList[z].reportList.length; y++) {
            let newReportDTO:ReportDTO = new ReportDTO();
            newReportDTO.fillFromJSON(JSON.stringify(data[i].taskList[x].reporterList[z].reportList[y]));
            newReportDTO.workTime=this.dateFormatter.transformTime(newReportDTO.workTime);
            newReportDTO.creationDateTime = this.dateFormatter
              .changeDateTimeToRuWithPattern(newReportDTO.creationDateTime, "Do MMMM YYYY");

            newReporterDTO.reportList.push(newReportDTO);
          }
          newTaskDTO.reporterList.push(newReporterDTO);
        }
        newProjectDTO.taskList.push(newTaskDTO);
      }
      this.reportProjectList.push(newProjectDTO);
    }
  }

}
