import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { User }  from './../model/user.model';
import { HttpHeaders } from "@angular/common/http";
import {environment} from "../../environments/environment";

@Injectable()
export class LoginService {

  constructor(private _http: HttpClient) {
  }

  login(username:string, password:string): Observable<User> {
    console.log(username + '   ' + password);
    let headers = new HttpHeaders();
    headers = headers.append("Authorization", "Basic " + btoa(username+':'+password));
    headers = headers.append("Content-Type", "application/json");
    return this._http.get<User>( environment.serviceUrl + 'login' , {headers: headers});
  }
}
