import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { Usuario }  from '../model/abm/usuario.model';
import { HttpHeaders } from "@angular/common/http";
import {environment} from "../../environments/environment";



@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  constructor(private _http: HttpClient) {
      }

  getAllUsuarios(): Observable<Usuario[]>{
    return this._http.get<Usuario[]>( environment.serviceUrl + "listar-usuarios" );
  }

  deleteUser(usuario: Usuario): Observable<string>{
    return this._http.delete<string>( environment.serviceUrl + "delete-usuario/" + usuario.nombreUsuario );
  }
  updateUsuario(usuario: Usuario): Observable<Usuario>{
    return this._http.put<Usuario>( environment.serviceUrl + "update-usuario/" , usuario);
  }

  getUsuarioByName(nombre: string): Observable<Usuario>{
    return this._http.get<Usuario>(environment.serviceUrl + "get-usuario/" + nombre);
  }
  createUsuario(usuario: Usuario): Observable<Usuario>{
    console.log(usuario);
    return this._http.post<Usuario>(environment.serviceUrl + "alta-usuario", usuario);
  }
}
