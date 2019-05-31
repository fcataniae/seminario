import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { Permiso }  from '../model/abm/permiso.model';
import { HttpHeaders } from "@angular/common/http";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class PermisoService {

  constructor(private _http: HttpClient) {}

  getAllPermisos(): Observable<Permiso[]>{
    return this._http.get<Permiso[]>(environment.serviceUrl + 'listar-permisos');
  }

  getUserAllPermisos(nombreUsuario: string, passUsuario: string): Observable<Permiso[]>{
    return this._http.get<Permiso[]>(environment.serviceUrl + 'get-permisos/' + nombreUsuario, passUsuario);
  }

}
