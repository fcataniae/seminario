import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { Usuario }  from '../model/usuario.model';
import { HttpHeaders } from "@angular/common/http";
import {environment} from "../../environments/environment";

@Injectable()
export class LoginService {

  constructor(private _http: HttpClient) {
  }

  login(username:string, password:string): Observable<Usuario> {
    console.log(username + '   ' + password);
    let headers = new HttpHeaders();

    sessionStorage.setItem('Auth','Basic ' + btoa(username+':'+password))

    return this._http.get<Usuario>( environment.serviceUrl + 'login'  );
  }

  prueba(): Observable<string>{
    return this._http.get<string>(environment.serviceUrl + 'prueba');

  }
}
