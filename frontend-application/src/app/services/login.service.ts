import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { Token }  from '../model/token.model';
import { HttpHeaders } from "@angular/common/http";
import {environment} from "../../environments/environment";

@Injectable()
export class LoginService {

  constructor(private _http: HttpClient) {
  }

  login(username:string, password:string): Observable<Token> {
    let httpHeaders = new HttpHeaders({
      	'Content-Type' : 'application/json'
     });
     let options = {
	      headers: httpHeaders
     };
     let body = JSON.stringify({"username": username , "password": password});
     console.log(body);
    return this._http.post<Token>( environment.serviceUrl.replace('service', 'auth/login'), body , options  );
  }

}
