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
  private reportList:any[];
  private reportProjectList:ProjectDTO[] = [];

  reportForm:ControlGroup;

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

  public createReport(value:any):void {
    if (value.reportType == this.reportTypeList[0]) {
      if (this.participantList.length == 0)
        console.log('Отчеты по проектам отсутствуют, т.к нету исполнителей');
      else
        this.createProjectReport(value);
    }

    if (value.reportType == this.reportTypeList[1]) {
      if (this.participantList.length == 0)
        console.log('Отчеты по задачам отсутствуют, т.к нету исполнителей');
      else
        this.createTaskReport(value);

    }
  }

  public createProjectReport(value:any):void {
    this.api_Report.getProjectReportList(this.dateFormatter.getUtcOffset(), value.projectOrTaskId,
      value.userId, value.startDate, value.endDate).subscribe(
      data => {
        console.log(data);
        this.reportList = data.responseObjects.reportProjectList;
        this.fillDetailReportProjectList(data.responseObjects.reportProjectList);
        console.log(this.reportProjectList);
      },
      error => {
        console.log(error);
      }
    );
  }

  public createTaskReport(value:any):void {
    this.api_Report.getTaskReportList(this.dateFormatter.getUtcOffset(), value.projectOrTaskId,
      value.userId, value.startDate, value.endDate).subscribe(
      data => {
        console.log(data);
        this.reportList = data.responseObjects.reportList;
      },
      error => {
        console.log(error);
      }
    );
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
        console.log(data);
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
        console.log(data);
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
      console.log(newProjectDTO);

      for (let x = 0; x < data[i].taskList.length; x++) {
        let newTaskDTO:TaskDTO = new TaskDTO();
        newTaskDTO.id = data[i].taskList[x].id;
        newTaskDTO.name = data[i].taskList[x].name;
        console.log(newTaskDTO);

        for (let z = 0; z < data[i].taskList[x].reporterList.length; z++) {
          let newReporterDTO:ReporterDTO = new ReporterDTO();
          newReporterDTO.id = data[z].taskList[x].reporterList[z].id;
          newReporterDTO.name = data[i].taskList[x].reporterList[z].name;
          newReporterDTO.taskElapsedTime = data[i].taskList[x].reporterList[z].taskElapsedTime;
          console.log(newReporterDTO);

          for (let y = 0; y < data[i].taskList[x].reporterList[z].reportList.length; z++) {
            let newReportDTO:ReportDTO = new ReportDTO();
            newReportDTO.fillFromJSON(JSON.stringify(data[i].taskList[x].reporterList[z].reportList[y]));
            newReportDTO.creationDateTime = this.dateFormatter
              .changeDateTimeToRuWithPattern(newReportDTO.creationDateTime, "Do MMMM YYYY");
            console.log(newReportDTO);

            newReporterDTO.reportList.push(newReportDTO);
            console.log('ОПа');
          }
          newTaskDTO.reporterList.push(newReporterDTO);
        }
        newProjectDTO.taskList.push(newTaskDTO);
      }
      this.reportProjectList.push(newProjectDTO);
    }
  }

}
