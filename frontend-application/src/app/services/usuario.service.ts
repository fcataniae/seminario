import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { Usuario }  from '../model/usuario.model';
import { HttpHeaders } from "@angular/common/http";
import {environment} from "../../environments/environment";



@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  constructor(private _http: HttpClient) {
      }

  getAllUsuarios(): Observable<Usuario[]>{
    return this._http.get<Usuario[]>( environment.serviceUrl + "get/usuarios" );
  }

  deleteUser(usuario: Usuario): Observable<boolean>{
    return this._http.delete<boolean>( environment.serviceUrl + "delete/user", usuario );
  }

  getUsuarioByName(nombre: string): Observable<Usuario>{
    return this._http.get<Usuario>(environment.serviceUrl + "usuario/" + nombre);
  }
}
