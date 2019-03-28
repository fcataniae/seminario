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

    return this._http.get<User>( environment.serviceUrl + 'login' );
  }
}
