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
import { TiendaEstadisticas } from '../model/bienes/tiendaEstadisticas.model';

@Injectable({
  providedIn: 'root'
})
export class MovimientoService {

  constructor(private _http: HttpClient) { }

  getAllMovimientos(): Observable<Movimiento[]>{
    return this._http.get<Movimiento[]>(environment.serviceUrl.replace('service','bienes') + 'listar-movimientos');
  }
  getAllTipoMovimientos() : Observable<TipoMovimiento[]>{
    return this._http.get<TipoMovimiento[]>(environment.serviceUrl.replace('service','bienes') + 'listar-tipomovimientos');
  }
  getAllEstadosBien() : Observable<Estado[]>{
    return this._http.get<Estado[]>(environment.serviceUrl.replace('service','bienes') + 'estado-bien');
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
  setConfirmacionEnvio(idmov: number, comentario: string, estado: string): Observable<string>{
    return this._http.put<string>(environment.serviceUrl.replace('service','bienes') + "confirmar-movimiento/" + idmov + "/" + estado,comentario);
  }
  getAllEstadosViaje(): Observable<Estado[]>{
    return this._http.get<Estado[]>(environment.serviceUrl.replace('service','bienes') +'estado-viaje');
  }
  getTiendasEstadisticas(fechaDesde: Date, fechaHasta: Date): Observable<TiendaEstadisticas[]>{
    return this._http.get<TiendaEstadisticas[]>(environment.serviceUrl.replace('service','bienes')
<<<<<<< HEAD
    +'cantidades-totales-por-tienda/'+fechas);
  }
  getMovimientoByNro(nro : string): Observable<Movimiento>{
    return this._http.get<Movimiento>(environment.serviceUrl.replace('service','bienes') + 'get-movimiento/' + nro);
=======
    +'cantidades-totales-por-tienda/'+ fechaDesde, fechaHasta);
>>>>>>> 85899636ab3276688bff19540725f9ccc0af5a4b
  }
}
