import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { TipoMovimiento } from '../model/bienes/tipomovimiento.model';
import { Bien } from '../model/bienes/bien.model';
import { environment } from '../../environments/environment';
import {Agente} from "../model/bienes/agente.model";
import { Recurso } from '../model/bienes/recurso.model';
import { TipoMovDoc } from '../model/bienes/tipodocmov.model';
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
  getAllTipoDocMovimientos() : Observable<TipoMovDoc[]>{
  return this._http.get<TipoMovDoc[]>(environment.serviceUrl.replace('service','bienes') + 'listar-tipomovtipodoc');
  }
  getEnviosPendientesByTienda(nroDestino: number) : Observable<Movimiento[]>{
    return this._http.get<Movimiento[]>(environment.serviceUrl.replace('service','bienes') + 'listar-envios-pendientes'+nroDestino);
  }
  setRegistroMovimiento(movimiento: Movimiento): Observable<string>{

    return this._http.post<string>(environment.serviceUrl.replace('service','bienes') +'alta-movimiento', movimiento);
  }
}
