import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { TipoMovimiento } from '../model/bienes/tipomovimiento.model';
import { Bien } from '../model/bienes/bien.model';
import { environment } from '../../environments/environment';
import {Agente} from "../model/bienes/agente.model";
import { Recurso } from '../model/bienes/recurso.model';
import { TipoDocumento } from '../model/bienes/tipodocumento.model';
import { Movimiento } from '../model/bienes/movimiento.model';
import { HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class MovimientoService {

  constructor(private _http: HttpClient) { }

  getAllTipoMovimientos() : Observable<TipoMovimiento[]>{
    return this._http.get<TipoMovimiento[]>(environment.serviceUrl.replace('service','bienes') + 'listar-movimientos');
  }

  getAllAgentes() : Observable<Agente[]>{
    return this._http.get<Agente[]>(environment.serviceUrl.replace('service','bienes') + 'listar-agentes');
  }
  getAllBienes(): Observable<Bien[]>{
    return this._http.get<Bien[]>(environment.serviceUrl.replace('service','bienes') + 'listar-bi');
  }
  getAllRecursos() : Observable<Recurso[]>{
    return this._http.get<Recurso[]>(environment.serviceUrl.replace('service','bienes') + 'listar-recursos');
  }
  getAllTipoDocMovimientos() : Observable<TipoDocumento[]>{
  return this._http.get<TipoDocumento[]>(environment.serviceUrl.replace('service','bienes') + 'listar-tipomovtipodoc');
  }
  setRegistroMovimiento(movimiento: Movimiento): Observable<string>{
    let body = JSON.stringify( movimiento);
    let httpHeaders = new HttpHeaders({
      	'Content-Type' : 'application/json'
     });
     let options = {
	      headers: httpHeaders
     };
    return this._http.post<string>(environment.serviceUrl.replace('service','bienes') +'alta-movimiento', body,options);
  }
}
