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
import { Transportista } from '../model/bienes/transportista.model';

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
  getAllEstadosBien(nro: number) : Observable<Estado[]>{
    return this._http.get<Estado[]>(environment.serviceUrl.replace('service','bienes') + 'estados-bien?nro=' + nro);
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
  setModificacionMovimiento(movimiento: Movimiento): Observable<string>{

    return this._http.put<string>(environment.serviceUrl.replace('service','bienes') +'update-movimiento', movimiento);
  }
  setConfirmacionEnvio(idmov: number, comentario: string, estado: string): Observable<string>{
    return this._http.put<string>(environment.serviceUrl.replace('service','bienes') + "confirmar-movimiento/" + idmov + "/" + estado,comentario);
  }
  getAllEstadosViaje(): Observable<Estado[]>{
    return this._http.get<Estado[]>(environment.serviceUrl.replace('service','bienes') +'estado-viaje');
  }
  getTiendasEstadisticas(fechaDesde: Date, fechaHasta: Date): Observable<TiendaEstadisticas[]>{
    console.log('cantidades-totales-por-tienda?fechadesde='+(fechaDesde ? fechaDesde: '') +'&fechahasta='+(fechaHasta? fechaHasta: ''));
    return this._http.get<TiendaEstadisticas[]>(environment.serviceUrl.replace('service','bienes')
    +'cantidades-totales-por-tienda?fechadesde='+(fechaDesde ? fechaDesde: '') +'&fechahasta='+(fechaHasta? fechaHasta: ''));
  }
  getMovimientoByNro(nro : string): Observable<Movimiento>{
    return this._http.get<Movimiento>(environment.serviceUrl.replace('service','bienes') + 'get-movimiento/' + nro);
  }
  getAllTransportistas():Observable<Transportista[]>{
    return this._http.get<Transportista[]>(environment.serviceUrl.replace('service','bienes') + 'listar-transportistas');
    }
  getAllLocales(): Observable<Agente[]>{
    return this._http.get<Agente[]>(environment.serviceUrl.replace('service','bienes') + 'listar-locales');
  }
}
