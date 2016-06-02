import {Injectable} from "@angular/core";
import {Http, Headers,URLSearchParams} from "@angular/http";
import "rxjs/add/operator/map";

@Injectable()
export class API_User {
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

  public checkAccess() {
    return this.http.get('/api/user/checkAccess').map(res => res.json());
  }

  public getUserList():any {
    let headers:Headers = this.getUrlencodedHeader();
    return this.http.get('/api/user/getUserList').map(res => res.json());
  }

  public getParticipantProjectUserList(projectId?:string):any {
    let headers:Headers = this.getUrlencodedHeader();
    let params:URLSearchParams = new URLSearchParams();

    if (projectId != undefined)
      params.set('projectId', projectId);

    return this.http.get('/api/user/getParticipantProjectUserList', {
      headers: headers, search: params
    }).map(res => res.json());
  }

  public getParticipantTaskUserList(taskId:string):any {
    let headers:Headers = this.getUrlencodedHeader();
    let params:URLSearchParams = new URLSearchParams();

    params.set('taskId', taskId);

    return this.http.get('/api/user/getParticipantTaskUserList', {
      headers: headers, search: params
    }).map(res => res.json());
  }

  public signUp(userName:string, email:string, password:string) {
    let headers:Headers = this.getJsonHeader();
    let transferData:any = JSON.stringify({userName: userName, email: email, password: password});

    return this.http.post('/api/user/signUp', transferData, {
      headers: headers
    }).map(res => res.json());
  }

  authenticate(username:string,password:string):any{
    let tData:string='username='+username+'&password='+password;

    let headers:Headers = this.getUrlencodedHeader();
    return this.http.post('/login', tData, {
      headers: headers
    }).map(res => res.status);
  }


  logout():any{
    return this.http.get('/logout').map(res => res.status);
  }


}



