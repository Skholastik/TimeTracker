import {Component,Input} from '@angular/core';

import {TaskDTO} from '../../../globalServices/entitiesDTO/taskDTO.class';
import {ReportDTO} from '../../../globalServices/entitiesDTO/reportDTO.class';
import {UserDTO} from '../../../globalServices/entitiesDTO/userDTO.class';
import {ReportBar} from '../reportBar/reportBar.component'
import {API_Task} from '../../../globalServices/api/API_Task.service';
import {API_Report} from '../../../globalServices/api/API_Report.service';
import {API_User} from '../../../globalServices/api/API_User.service';
import {DateFormatter} from '../../../globalServices/dateFromatter/dateFormatter.service';


@Component({
  selector: 'task-management',
  providers: [API_Task, API_Report, API_User],
  directives: [ReportBar],
  pipes: [],
  styles: [require('./taskManagement.css')],
  template: require('./taskManagement.html')
})
export class TaskManagement {

  @Input() task:TaskDTO;
  private userList:UserDTO[] = [];

  private selectedUserName:string;
  private selectedUser:UserDTO;

  private statusBarValue;
  private isStatusBarOpen:boolean = false;
  private showTaskNameInput:boolean = false;
  private showSubTaskNameInput:boolean = false;
  private showTaskDescriptionTextArea:boolean = false;
  private showReportBar:boolean = false;
  private accessToChangeTask:boolean = false;

  private statusList:string[] = ['Active', 'Frozen', 'New', 'Complete'];


  constructor(private api_Task:API_Task,
              private api_Report:API_Report,
              private api_User:API_User,
              private dateFormatter:DateFormatter) {
  }

  ngOnInit() {
    this.getTaskAllReportList();
    this.getUserList();
  }


  public changeName(name:string):void {
    var formattedName = name.trim();

    if (formattedName.length != 0 &&
      formattedName != this.task.name) {
      this.api_Task.changeName(this.task.id, name).subscribe(
        data => {
          this.task.name = name;
        },
        error=> {
          console.log(error);
        }
      );
    }
    this.showTaskNameInput = false;
  }


  public checkAccessToChangeName():void {

    if (this.accessToChangeTask)
      this.showTaskNameInput = true;

    else {
      this.api_Task.checkLowLevelAuthorities(this.task.id).subscribe(
        data=> {
          console.log(data);
          this.showTaskNameInput = true;
          this.accessToChangeTask = true;

        },
        error=> {
          console.log(error);
          this.showTaskNameInput = false;

        }
      );
    }
  }

  public changeStatus(status:string):void {
    if (this.task.status != status) {
      this.api_Task.changeStatus(this.task.id, status).subscribe(
        data => {
          this.task.status = status;
        },
        error => {
          console.log(error);
        }
      );
    }
    this.isStatusBarOpen = this.isStatusBarOpen == false;
  }

  public checkAccessToCreateSubTask():void {

    if (this.accessToChangeTask) {
      this.showSubTaskNameInput = true;
    }
    else {
      this.api_Task.checkLowLevelAuthorities(this.task.id).subscribe(
        data=> {
          console.log(data);
          this.showSubTaskNameInput = true;
          this.accessToChangeTask = true;
        },
        error=> {
          console.log(error);
          this.showSubTaskNameInput = false;

        }
      );
    }
  }

  public changeDescription(description:string):void {
    var formattedDescription = description.trim();

    if (formattedDescription.length != 0 &&
      formattedDescription != this.task.description) {
      this.api_Task.changeDescription(this.task.id, description).subscribe(
        data => {
          this.task.description = description;
        },
        error=> {
          console.log(error);
        }
      );
    }
    this.showTaskDescriptionTextArea = false;
  }

  public checkAccessToChangeDescription():void {

    if (this.accessToChangeTask)
      this.showTaskDescriptionTextArea = true;

    else {
      this.api_Task.checkLowLevelAuthorities(this.task.id).subscribe(
        data=> {
          this.showTaskDescriptionTextArea = true;
          this.accessToChangeTask = true;

        },
        error=> {
          console.log(error);
          this.showTaskDescriptionTextArea = false;

        }
      );
    }
  }


  public openCloseStatusBar(status:string):void {
    this.statusBarValue = status;
    this.isStatusBarOpen = this.isStatusBarOpen == false;
  }

  public openCloseReportBar():void {

    if (this.accessToChangeTask) {
      this.showReportBar = this.showReportBar == false;
    }

    else {
      this.api_Task.checkLowLevelAuthorities(this.task.id).subscribe(
        data=> {
          this.showReportBar = true;
          this.accessToChangeTask = true;
        },
        error=> {
          console.log(error);
          this.showReportBar = false;
        }
      );
    }
  }

  getTaskAllReportList() {
    this.api_Report.getTaskAllReportList(this.task.id, this.dateFormatter.getUtcOffset()).subscribe(
      data=> {
        console.log(data);
        this.pushTransferReportListToProject(data.responseObjects.reportList);
      },
      error=> {
        console.log(error);
      }
    );
  }

  public reportCreated(event) {
    this.showReportBar = false;
    console.log(event);
  }


  /** Трансформирует входящий массив отчетов в DTO объект, с изменением формата времени*/
  pushTransferReportListToProject(data:any[]):void {
    for (let i = 0; i < data.length; i++) {
      let newReportDTO = new ReportDTO();
      newReportDTO.fillFromJSON(JSON.stringify(data[i]));
      newReportDTO.workTime = this.dateFormatter.transformTime(newReportDTO.workTime);
      newReportDTO.creationDateTime = this.dateFormatter
        .changeDateTimeToRuWithPattern(newReportDTO.creationDateTime, "Do MMMM YYYY");
      this.task.reportList.push(newReportDTO);
    }
  }

  public getUserList():void {
    if(this.userList.length==0){
      this.api_User.getUserList().subscribe(
        data=> {
          this.pushTransferUserListToListDTO(data.responseObjects.userList);
        },
        error=> {
          console.log(error);
        }
      );
    }
  }

  public addTaskExecutor():void {
    if (this.selectedUser!= undefined) {
        this.api_Task.addTaskExecutor(this.task.id,this.selectedUser.id).subscribe(
          data=> {
            console.log(data);
          },
          error=> {
            console.log(error);
          }
        );
    }
    else
      console.log('Необходимо выбрать исполнителя перед отправкой');
  }

  userSelected():void {
    this.selectedUser = this.userList.find(user => user.name === this.selectedUserName);
  }

  pushTransferUserListToListDTO(data:any[]):void {
    for (let i = 0; i < data.length; i++) {
      let newUserDTO = new UserDTO();
      newUserDTO.fillFromJSON(JSON.stringify(data[i]));
      this.userList.push(newUserDTO);
    }
  }


}