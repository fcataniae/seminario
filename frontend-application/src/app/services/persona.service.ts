import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { Persona }  from '../model/persona.model';
import { HttpHeaders } from "@angular/common/http";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class PersonaService {


  constructor(private _http: HttpClient) {
    }

  getPersonaByDocumento(documento: number): Observable<Persona>{
    return this._http.get<Persona>(environment.serviceUrl + 'persona/' + documento);
  }

  deletePersona(persona: Persona): Observable<string>{
    return this._http.delete<string>(environment.serviceUrl + 'persona/' , persona);
  }
  modificarPersona(persona: Persona): Observable<string>{
    return this._http.put<string>(environment.serviceUrl + 'persona/', persona);
  }

}
