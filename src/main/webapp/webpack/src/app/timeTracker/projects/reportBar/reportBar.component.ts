import {Component,Input,Output,EventEmitter} from '@angular/core';
import {ControlGroup,FormBuilder,Validators} from '@angular/common';

import {TaskDTO} from '../../../globalServices/entitiesDTO/taskDTO.class';
import {API_Report} from '../../../globalServices/api/API_Report.service';
import {DateFormatter} from '../../../globalServices/dateFromatter/dateFormatter.service';


@Component({
  selector: 'report-bar',
  providers: [API_Report],
  directives: [],
  pipes: [],
  styles: [require('./reportBar.css')],
  template: require('./reportBar.html')
})
export class ReportBar {

  @Input() task:TaskDTO;
  @Output() createdReport = new EventEmitter();

  private inputDate:string;
  private reportDate:string;
  private errorMessage:string = '';

  private createReportForm:ControlGroup;



  public constructor(private api_Report:API_Report,
                     private dateFormatter:DateFormatter,
                     private formBuilder:FormBuilder) {
  }

  public ngOnInit():void {
    this.inputDate = this.dateFormatter.getCurrentDate();
    this.reportDate = this.dateFormatter.getCurrentMonth();

    this.createReportForm = this.formBuilder.group({
      'hours': ['', Validators.required],
      'minutes': ['', Validators.required],
      'report': ['', Validators.required],
    });
  }

  public createReport(value:any) {

    if (this.validateData(value)) {
      value.hours = value.hours.length == 1 ? '0' + value.hours : value.hours;
      value.minutes = value.minutes.length == 1 ? '0' + value.minutes : value.minutes;

      let workTime = value.hours + ':' + value.minutes;

      this.api_Report.createReport(value.report, workTime, this.inputDate,
        this.task.id, this.dateFormatter.getUtcOffset()).subscribe(
        data => {
          this.createdReport.emit({
            reporter: data.responseObjects.reporter
          })
        },
        error => {
          console.log(error)
        }
      );
    }
  }

  public validateData(value:any):boolean {
    if (value.hours.length == 0 || value.minutes.length == 0) {
      this.errorMessage = 'Заполните время работы';
      return false;
    }

    let hours:number = +value.hours;
    let inputMinutes:number = +value.minutes;

    if (hours > 24) {
      this.errorMessage = 'Часов не может быть больше 24';
      return false;
    }

    if (inputMinutes > 60) {
      this.errorMessage = 'Минут не может быть больше 60';
      return false;
    }

    if (value.report.trim().length == 0) {
      this.errorMessage = 'Необходимо заполнить отчет';
      return false;
    }

    return true;
  }


}
