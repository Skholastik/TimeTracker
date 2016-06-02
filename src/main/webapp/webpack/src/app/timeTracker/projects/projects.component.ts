import {Component,ViewEncapsulation,EventEmitter,Output} from '@angular/core';
import {ControlGroup,FormBuilder,Validators} from '@angular/common';

import {ProjectManagement} from './projectManagement/projectManagement.component';
import {TaskManagement} from './taskManagement/taskManagement.component';
import {ProjectDTO} from '../../globalServices/entitiesDTO/projectDTO.class';
import {TaskDTO} from '../../globalServices/entitiesDTO/taskDTO.class';
import {API_Project} from '../../globalServices/api/API_Project.service';
import {API_Task} from '../../globalServices/api/API_Task.service';
import {DateFormatter} from '../../globalServices/dateFromatter/dateFormatter.service';


@Component({
  selector: 'projects',
  providers: [API_Project, API_Task],
  directives: [ProjectManagement, TaskManagement],
  pipes: [],
  encapsulation: ViewEncapsulation.Native,
  styles: [require('./projects.css')],
  template: require('./projects.html')
})
export class Projects {

  private targetProject:ProjectDTO = new ProjectDTO();
  private targetTask:TaskDTO;
  private projectList:ProjectDTO[] = [];
  private openProjectBar:boolean = false;
  private openTaskBar:boolean = false;
  private openProjectTaskList:boolean = false;
  private openCreateProject:boolean = false;
  private status:string = 'Active';

  private createProjectForm:ControlGroup;


  ngOnInit() {
    this.getUserProjectListByStatus();

    this.createProjectForm = this.formBuilder.group({
      'projectName': ['', Validators.required],
    });

  }

  constructor(private api_Project:API_Project,
              private api_Task:API_Task,
              private dateFormatter:DateFormatter,
              private formBuilder:FormBuilder) {
  }

  /** Возвращает активные проекты пользователя*/
  getUserProjectListByStatus():void {
    this.api_Project.getUserProjectListByStatus(this.status, this.dateFormatter.getUtcOffset()).subscribe(
      data => {
        if (data.responseObjects.projectList.length != 0)
          this.transferProjectListToDTO(data.responseObjects.projectList);
      },
      error => {
        console.log(error);
      }
    );
  }

  /** Возвращает задачи пользователя(Т.к есть разделения на подзадачи)*/
  public getProjectHighTaskList():void {
    this.api_Task.getProjectHighTaskList(this.targetProject.id, this.dateFormatter.getUtcOffset()).subscribe(
      data => {
        this.pushTransferTaskListToProject(data.responseObjects.taskList);
        console.log(data);
      },
      error => {
        console.log(error);
      }
    )
  }


  createProject(projectName:string) {
    console.log(projectName);
    this.api_Project.createProject(projectName, this.dateFormatter.getUtcOffset()).subscribe(
      data => {
        let newProject = new ProjectDTO();
        newProject.fillFromJSON(JSON.stringify(data.responseObjects.project));
        newProject.creationDateTime = this.dateFormatter
          .changeDateTimeToRuWithPattern(newProject.creationDateTime, "Do MMMM YYYY");
        this.targetProject = newProject;
        this.projectList.push(newProject);
        this.openProjectBar = true;
        this.openCreateProjectSwitch();
      },
      error => {
        console.log(error);
        console.log(error._body);
      }
    );
  }


  /** Трансформирует входящий массив проектов в DTO объект, с изменением формата времени*/
  transferProjectListToDTO(data:any[]):void {
    for (let i = 0; i < data.length; i++) {
      let newProjectDTO = new ProjectDTO();
      newProjectDTO.fillFromJSON(JSON.stringify(data[i]));
      newProjectDTO.creationDateTime = this.dateFormatter
        .changeDateTimeToRuWithPattern(newProjectDTO.creationDateTime, "Do MMMM YYYY");
      this.projectList.push(newProjectDTO);
    }
  }

  /** Трансформирует входящий массив задач в DTO объект, с изменением формата времени*/
  pushTransferTaskListToProject(data:any[]):void {
    for (let i = 0; i < data.length; i++) {
      let newTaskDTO = new TaskDTO();
      newTaskDTO.fillFromJSON(JSON.stringify(data[i]));
      newTaskDTO.creationDateTime = this.dateFormatter
        .changeDateTimeToRuWithPattern(newTaskDTO.creationDateTime, "Do MMMM YYYY");
      this.targetProject.taskList.push(newTaskDTO);
    }
  }


  public projectOpenCloseSwitch(project:ProjectDTO):void {
    this.openProjectBar = true;
    this.openTaskBar = false;
    this.openProjectTaskList = false;
    this.targetProject = project;

    if (this.targetProject.taskList.length == 0)
      this.getProjectHighTaskList();

  }

  public openCreateProjectSwitch():void {
    this.openCreateProject = this.openCreateProject == false;
  }

  public switchToTaskBar(task:TaskDTO):void {
    this.openProjectBar = false;
    this.openTaskBar = true;
    this.openProjectTaskList = true;
    this.targetTask = task;
  }


}
