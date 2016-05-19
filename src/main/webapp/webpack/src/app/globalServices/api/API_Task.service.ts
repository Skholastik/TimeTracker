import {Injectable} from "@angular/core";
import {Http, Headers,URLSearchParams} from "@angular/http";
import "rxjs/add/operator/map";

@Injectable()
export class API_Task {
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


  public getProjectHighTaskList(projectId:string, utcOffset:string):any {
    let headers:Headers = this.getUrlencodedHeader();
    let params:URLSearchParams = new URLSearchParams();
    params.set('projectId', projectId);
    params.set('utcOffset', this.correctOffset(utcOffset));
    return this.http.get('/api/task/getProjectHighTaskList', {
      headers: headers, search: params
    }).map(res => res.json());
  }

  public createTask(taskName:string, projectName:string, utcOffset:string):any {
    let headers:Headers = this.getJsonHeader();

    let params:URLSearchParams = new URLSearchParams();
    params.set('utcOffset', this.correctOffset(utcOffset));

    let transferData:any = JSON.stringify({name: taskName, ancestorProjectId: projectName});

    return this.http.post('/api/task/createTask', transferData, {
      headers: headers, search: params
    }).map(res => res.json());
  }

  public addTaskExecutor(taskId:string, userId:string):any {
    let transferData:any = JSON.stringify({id: userId});
    let headers:Headers = this.getJsonHeader();

    let params:URLSearchParams = new URLSearchParams();
    params.set('taskId', taskId);

    return this.http.put('/api/task/addTaskExecutor', transferData, {
      headers: headers, search: params
    }).map(res => res.json());

  }

  public changeName(taskId:string, name:string):any {
    let transferData:any = JSON.stringify({id: taskId, name: name});
    let headers:Headers = this.getJsonHeader();
    return this.http.put('/api/task/setName', transferData, {
      headers: headers
    }).map(res => res.json());

  }

  public changeStatus(taskId:string, status:string):any {
    let transferData:any = JSON.stringify({id: taskId, status: status});
    let headers:Headers = this.getJsonHeader();
    return this.http.put('/api/task/setStatus', transferData, {
      headers: headers
    }).map(res => res.json());

  }

  public changeDescription(taskId:string, description:string):any {
    let transferData:any = JSON.stringify({id: taskId, description: description});
    let headers:Headers = this.getJsonHeader();
    return this.http.put('/api/task/setDescription', transferData, {
      headers: headers
    }).map(res => res.json());

  }

  public checkLowLevelAuthorities(taskId:string):any {
    let headers:Headers = this.getUrlencodedHeader();
    let params:URLSearchParams = new URLSearchParams();
    params.set('id', taskId);
    return this.http.get('/api/task/checkLowLevelAuthorities', {
      headers: headers, search: params
    }).map(res => res.json());

  }
}
