import {Component,ViewEncapsulation} from '@angular/core';

import {ProjectManagement} from './projectManagement/projectManagement.component';
import {TaskManagement} from './taskManagement/taskManagement.component';
import {ProjectDTO} from '../../globalServices/entitiesDTO/projectDTO.class';
import {TaskDTO} from '../../globalServices/entitiesDTO/taskDTO.class';
import {API_Project} from '../../globalServices/api/API_Project.service';
import {API_Task} from '../../globalServices/api/API_Task.service';
import {DateFormatter} from '../../globalServices/dateFromatter/dateFormatter.service';

/*import {Calendar} from '../globalServices/datePicker/calendar.component'*/


@Component({
  selector: 'projects',
  providers: [API_Project, API_Task],
  directives: [ProjectManagement,TaskManagement],
  pipes: [],
  encapsulation: ViewEncapsulation.Native,
  styles: [require('./projects.css')],
  template: require('./projects.html')
})
export class Projects {

  private targetProject:ProjectDTO = new ProjectDTO();
  private targetTask:TaskDTO;
  private projectList:ProjectDTO[] = [];
  private projectName:string;
  private openProjectBar:boolean = false;
  private openTaskBar:boolean = false;
  private openProjectTaskList:boolean=false;
  private openCreateProject:boolean = false;


  ngOnInit() {
    this.getUserActiveProjectList();

  }

  constructor(private api_Project:API_Project,
              private api_Task:API_Task,
              private dateFormatter:DateFormatter) {
  }

  /** Возвращает активные проекты пользователя*/
  getUserActiveProjectList():void {
    this.api_Project.getUserActiveProjectList(this.dateFormatter.getUtcOffset()).subscribe(
      data => {
        if (data.responseObjects.projectList.length != 0)
          this.projectList = this.transferProjectListToDTO(data.responseObjects.projectList);
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


  createProject() {
    this.api_Project.createProject(this.projectName, this.dateFormatter.getUtcOffset()).subscribe(
      data => {
        this.targetProject.fillFromJSON(JSON.stringify(data.responseObjects.project));
        this.targetProject.creationDateTime = this.dateFormatter
          .changeDateTimeToRuWithPattern(this.targetProject.creationDateTime, "Do MMMM YYYY");
        this.projectList.push(this.targetProject);
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
  transferProjectListToDTO(data:any[]):ProjectDTO[] {
    let projectDTOArray:ProjectDTO[] = [];
    for (let i = 0; i < data.length; i++) {
      let newProjectDTO = new ProjectDTO();
      newProjectDTO.fillFromJSON(JSON.stringify(data[i]));
      newProjectDTO.creationDateTime = this.dateFormatter
        .changeDateTimeToRuWithPattern(newProjectDTO.creationDateTime, "Do MMMM YYYY");
      projectDTOArray.push(newProjectDTO);
    }
    return projectDTOArray;
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
    this.openTaskBar=false;
    this.openProjectTaskList=false;
    this.targetProject = project;

    if (this.targetProject.taskList.length == 0)
      this.getProjectHighTaskList();
  }

  public openCreateProjectSwitch():void {
    this.openCreateProject = this.openCreateProject == false;
  }

  public changeProjectToTaskBar(event):void {
    this.openProjectTaskList=true;
    this.openProjectBar=false;
    this.openTaskBar=true;
    this.targetTask=event.task;
  }


}
