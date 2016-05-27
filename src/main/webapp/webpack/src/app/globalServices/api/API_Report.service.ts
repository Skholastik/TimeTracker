import {Injectable} from "@angular/core";
import {Http, Headers,URLSearchParams} from "@angular/http";
import "rxjs/add/operator/map";

@Injectable()
export class API_Report {
  constructor(private http:Http) {
  }

  private getJsonHeader():Headers {
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    return headers
  }

  private getUrlencodedHeader():Headers {
    var headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');
    return headers
  }

  private correctOffset(offset:string):string {
    return offset.replace('+', '%2B')
  }

  public createReport(report:string, workTime:string, workDate:string, taskId:string, utcOffset:string):any {
    let headers:Headers = this.getJsonHeader();

    let params:URLSearchParams = new URLSearchParams();
    params.set('utcOffset', this.correctOffset(utcOffset));
    params.set('taskId', taskId);

    let transferData:any = JSON.stringify({
      report: report,
      workTime: workTime,
      workDate: workDate,
    });

    return this.http.post('/api/report/createReport', transferData, {
      headers: headers, search: params
    }).map(res => res.json());
  }

  public getTaskAllReportList(taskId:string, utcOffset:string):any {
    let headers:Headers = this.getJsonHeader();

    let params:URLSearchParams = new URLSearchParams();
    params.set('utcOffset', this.correctOffset(utcOffset));
    params.set('taskId', taskId);

    return this.http.get('/api/report/getTaskAllReportList', {
      headers: headers, search: params
    }).map(res => res.json());
  }

  public getProjectReportList(utcOffset:string, projectId:string, userId:string,
                              startDate:string, endDate:string):any {
    let headers:Headers = this.getJsonHeader();

    let params:URLSearchParams = new URLSearchParams();
    params.set('utcOffset', this.correctOffset(utcOffset));
    if (projectId.length!=0)
      params.set('projectId', projectId);
    if (userId.length!=0)
      params.set('creatorId', userId);
    if (startDate.length!=0)
      params.set('startDate', startDate);
    if (endDate.length!=0)
      params.set('endDate', endDate);

    return this.http.get('/api/report/getProjectReportList', {
      headers: headers, search: params
    }).map(res => res.json());
  }

  public getTaskReportList(utcOffset:string, projectId:string, userId:string,
                              startDate:string, endDate:string):any {
    let headers:Headers = this.getJsonHeader();

    let params:URLSearchParams = new URLSearchParams();
    params.set('utcOffset', this.correctOffset(utcOffset));
    if (projectId.length!=0)
      params.set('taskId', projectId);
    if (userId.length!=0)
      params.set('creatorId', userId);
    if (startDate.length!=0)
      params.set('startDate', startDate);
    if (endDate.length!=0)
      params.set('endDate', endDate);

    return this.http.get('/api/report/getTaskReportList', {
      headers: headers, search: params
    }).map(res => res.json());
  }


}
