import {Component,Input,Output,EventEmitter,ViewEncapsulation} from '@angular/core';

import {ProjectDTO} from '../../../globalServices/entitiesDTO/projectDTO.class';
import {TaskDTO} from '../../../globalServices/entitiesDTO/taskDTO.class';
import {API_Project} from '../../../globalServices/api/API_Project.service';
import {API_Task} from '../../../globalServices/api/API_Task.service';
import {DateFormatter} from '../../../globalServices/dateFromatter/dateFormatter.service';


@Component({
  selector: 'project-management',
  providers: [API_Project, API_Task],
  directives: [],
  pipes: [],
  encapsulation: ViewEncapsulation.Native,
  styles: [require('./projectManagement.css')],
  template: require('./projectManagement.html')
})
export class ProjectManagement {

  @Input() project:ProjectDTO;
  @Output() taskSelected = new EventEmitter();


  private statusBarValue;
  private isStatusBarOpen:boolean = false;
  private statusList:string[] = ['Active', 'Frozen', 'Complete'];
  private accessToChangeProject:boolean = false;
  private showProjectNameInput:boolean = false;
  private showProjectDescriptionTextArea:boolean = false;
  private showTaskNameInput:boolean = false;

  constructor(private api_Project:API_Project,
              private api_Task:API_Task,
              private dateFormatter:DateFormatter) {
  }

  ngOnInit() {
  }

  public openCloseStatusBar(status:string):void {
    this.statusBarValue = status;
    this.isStatusBarOpen = this.isStatusBarOpen == false;
  }

  public changeStatus(status:string):void {
    if(this.project.status!=status){
      this.api_Project.changeStatus(this.project.id, status).subscribe(
        data => {
          this.project.status = status;
        },
        error => {
          console.log(error);
        }
      );
    }
    this.isStatusBarOpen = this.isStatusBarOpen == false;
  }


  public checkAccessToCreateTask():void {

    if (this.accessToChangeProject) {
      this.showTaskNameInput = true;
    }
    else {
      this.api_Project.checkAccessToChangeProject(this.project.id).subscribe(
        data=> {
          console.log(data);
          this.showTaskNameInput = true;
          this.accessToChangeProject = true;
        },
        error=> {
          console.log(error);
          this.showTaskNameInput = false;

        }
      );
    }
  }

  public createTask(taskName:string):void {
    console.log(taskName);
    this.api_Task.createTask(taskName, this.project.id, this.dateFormatter.getUtcOffset()).subscribe(
      data=> {
        let newTask:TaskDTO=new TaskDTO();
        newTask.fillFromJSON(JSON.stringify(data.responseObjects.task));

        newTask.creationDateTime = this.dateFormatter
          .changeDateTimeToRuWithPattern(newTask.creationDateTime, "Do MMMM YYYY");
        this.project.taskList.push(newTask);

        this.showTaskNameInput = false;
      },
      error=> {
        console.log(error);

      }
    );
  }

  public checkAccessToChangeName():void {

    if (this.accessToChangeProject)
      this.showProjectNameInput = true;

    else {
      this.api_Project.checkAccessToChangeProject(this.project.id).subscribe(
        data=> {
          this.showProjectNameInput = true;
          this.accessToChangeProject = true;

        },
        error=> {
          console.log(error);
          this.showProjectNameInput = false;

        }
      );
    }
  }


  public changeName(name:string):void {
    var formattedName = name.trim();

    if (formattedName.length != 0 &&
      formattedName != this.project.name) {
      this.api_Project.changeName(this.project.id, name).subscribe(
        data => {
          this.project.name = name;
        },
        error=> {
          console.log(error);
        }
      );
    }
    this.showProjectNameInput = false;
  }

  public checkAccessToChangeDescription():void {

    if (this.accessToChangeProject)
      this.showProjectDescriptionTextArea = true;

    else {
      this.api_Project.checkAccessToChangeProject(this.project.id).subscribe(
        data=> {
          this.showProjectDescriptionTextArea = true;
          this.accessToChangeProject = true;

        },
        error=> {
          console.log(error);
          this.showProjectDescriptionTextArea = false;

        }
      );
    }
  }

  public changeDescription(description:string):void {
    var formattedDescription = description.trim();

    if (formattedDescription.length != 0 &&
      formattedDescription != this.project.description) {
      this.api_Project.changeDescription(this.project.id, description).subscribe(
        data => {
          this.project.description = description;
        },
        error=> {
          console.log(error);
        }
      );
    }
    this.showProjectDescriptionTextArea = false;
  }

  moveToTask(task:TaskDTO){
    this.taskSelected.emit({
      task:task
    })
  }

}
