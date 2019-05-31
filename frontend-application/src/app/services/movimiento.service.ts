import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { TipoMovimiento } from '../model/bienes/tipomovimiento.model';
import { Bien } from '../model/bienes/bien.model';
import { environment } from '../../environments/environment';
import {Agente} from "../model/bienes/agente.model";
import { Recurso } from '../model/bienes/recurso.model';
import { Movimiento } from '../model/bienes/movimiento.model';
import { HttpHeaders } from '@angular/common/http';
import { Estado } from '../model/bienes/estado.model';

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
  getEnviosPendientesByTienda(nroDestino: number) : Observable<Movimiento[]>{
    return this._http.get<Movimiento[]>(environment.serviceUrl.replace('service','bienes') + 'listar-envios-pendientes/'+nroDestino);
  }
  setRegistroMovimiento(movimiento: Movimiento): Observable<string>{

    return this._http.post<string>(environment.serviceUrl.replace('service','bienes') +'alta-movimiento', movimiento);
  }
  setConfirmacionEnvio(idmov: number, comentario: string): Observable<string>{
    return this._http.put<string>(environment.serviceUrl.replace('service','bienes') + "confirmar-movimiento/" + idmov,comentario);
  }
  getAllEstadosViaje(): Observable<Estado[]>{
    return this._http.get<Estado[]>(environment.serviceUrl.replace('service','bienes') +'estado-viaje');
  }
}
