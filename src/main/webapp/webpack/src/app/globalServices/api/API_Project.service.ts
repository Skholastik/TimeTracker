import {Injectable} from "@angular/core";
import {Http, Headers,URLSearchParams} from "@angular/http";
import "rxjs/add/operator/map";


@Injectable()
export class API_Project {
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

  public getUserActiveProjectList(utcOffset:string):any {
    let headers:Headers = this.getUrlencodedHeader();
    let params:URLSearchParams = new URLSearchParams();
    params.set('utcOffset', this.correctOffset(utcOffset));
    return this.http.get('/api/project/getUserActiveProjectList', {
      headers: headers, search: params
    }).map(res => res.json());
  }

  public createProject(projectName:string, utcOffset:string):any {

    let headers:Headers = this.getJsonHeader();

    let params:URLSearchParams = new URLSearchParams();
    params.set('utcOffset', this.correctOffset(utcOffset));

    let transferData:any = JSON.stringify({name: projectName});
    return this.http.post('/api/project/createProject', transferData, {
      headers: headers, search: params
    }).map(res => res.json());
  }

  public changeName(projectId:string, projectName:string):any {
    let transferData:any = JSON.stringify({id: projectId, name: projectName});
    let headers:Headers = this.getJsonHeader();
    return this.http.put('/api/project/setName', transferData, {
      headers: headers
    }).map(res => res.json());

  }


  public changeDescription(projectId:string, description:string):any {
    let transferData:any = JSON.stringify({id: projectId, description: description});
    let headers:Headers = this.getJsonHeader();
    return this.http.put('/api/project/setDescription', transferData, {
      headers: headers
    }).map(res => res.json());

  }

  public changeStatus(projectId:string, status:string):any {
    let transferData:any = JSON.stringify({id: projectId, status: status});
    let headers:Headers = this.getJsonHeader();
    return this.http.put('/api/project/setStatus', transferData, {
      headers: headers
    }).map(res => res.json());

  }

  public checkAccessToChangeProject(projectId:string):any {
    let headers:Headers = this.getUrlencodedHeader();
    let params:URLSearchParams = new URLSearchParams();
    params.set('id', projectId);
    return this.http.get('/api/project/checkAccessToChangeProject', {
      headers: headers, search: params
    }).map(res => res.json());

  }
}
