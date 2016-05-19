import {Injectable} from "@angular/core";
import {Http, Headers,URLSearchParams} from "@angular/http";
import "rxjs/add/operator/map";


@Injectable()
export class API_LogIn {
  constructor(private http:Http) {
  };

  getJsonHeader():Headers {
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    return headers
  }

  getUrlencodedHeader():Headers {
    var headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');
    return headers
  }

  authenticate(username:string,password:string):any{
    let tData:string='username='+username+'&password='+password;

    let headers:Headers = this.getUrlencodedHeader();
    return this.http.post('/login', tData, {
      headers: headers
    }).map(res => res.status);
  }
}
