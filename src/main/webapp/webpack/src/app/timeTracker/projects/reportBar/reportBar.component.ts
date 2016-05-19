import {Component,Input,Output,EventEmitter} from '@angular/core';

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

  private inputHours:string = '';
  private inputMinutes:string = '';
  private inputReport:string = '';
  private inputDate:string;
  private reportDate:string;
  private errorMessage:string = '';


  public constructor(private api_Report:API_Report,
                     private dateFormatter:DateFormatter) {
  }

  public ngOnInit():void {
    this.inputDate = this.dateFormatter.getCurrentDate();
    this.reportDate = this.dateFormatter.getCurrentMonth();
  }

  public createReport() {

    if (this.validateData()) {
      this.inputHours = this.inputHours.length == 1 ? '0' + this.inputHours : this.inputHours;
      this.inputMinutes = this.inputMinutes.length == 1 ? '0' + this.inputMinutes : this.inputMinutes;

      let workTime = this.inputHours + ':' + this.inputMinutes;

      this.api_Report.createReport(this.inputReport, workTime, this.inputDate,
        this.task.id, this.dateFormatter.getUtcOffset()).subscribe(
        data => {
          this.createdReport.emit({
            report: data.responseObjects.report
          })
        },
        error => {
          console.log(error)
        }
      );
    }
  }

  public validateData():boolean {
    if (this.inputHours.length == 0 || this.inputMinutes.length == 0) {
      this.errorMessage = 'Заполните время работы';
      return false;
    }

    let hours:number = +this.inputHours;
    let inputMinutes:number = +this.inputMinutes;

    if (hours > 24) {
      this.errorMessage = 'Часов не может быть больше 24';
      return false;
    }

    if (inputMinutes > 60) {
      this.errorMessage = 'Минут не может быть больше 60';
      return false;
    }

    if (this.inputReport.trim().length == 0) {
      this.errorMessage = 'Необходимо заполнить отчет';
      return false;
    }

    return true;
  }


}
