import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { User }  from './../model/user.model';
import { HttpHeaders } from "@angular/common/http";
import {environment} from "../../environments/environment";



@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  constructor(private _http: HttpClient) {
      }

  getAllUsuarios(): Observable<User[]>{
    return this._http.get<User[]>( environment.serviceUrl + "usuarios" );
  }
}
