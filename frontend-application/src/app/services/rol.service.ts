import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { Rol }  from '../model/rol.model';
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class RolService {

  constructor(private _http : HttpClient) { }

  deleteRol(rol: Rol):Observable<string>{
    return this._http.delete<string>(environment.serviceUrl + "deleterol/" + rol.nombre);
  }
  getAllRoles(): Observable<Rol[]>{
    return this._http.get<Rol[]>( environment.serviceUrl + "listar-roles" );
  }
  getRolByName(name: string): Observable<Rol>{
    return this._http.get<Rol>(environment.serviceUrl + "rol/"+name);
  }
  createRol(rol : Rol): Observable<string>{
    console.log(rol);
    return this._http.post<string>(environment.serviceUrl + "alta-rol", rol);
  }
}
