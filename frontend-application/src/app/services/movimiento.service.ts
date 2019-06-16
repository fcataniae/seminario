import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { TipoMovimiento } from '../model/bienes/tipomovimiento.model';
import { Bien } from '../model/bienes/bien.model';
import { environment } from '../../environments/environment';
import {Agente} from "../model/bienes/agente.model";
import { Recurso } from '../model/bienes/recurso.model';
import { Movimiento } from '../model/bienes/movimiento.model';
import { Estado } from '../model/bienes/estado.model';
import { Transportista } from '../model/bienes/transportista.model';
import { Dashboard } from '../model/bienes/dashboard.model';
import { IntercambioProv } from '../model/bienes/intercambioprovedor.model';

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
  getDashboardTiendas(fechaDesde: Date, fechaHasta: Date): Observable<Dashboard[]>{
    return this._http.get<Dashboard[]>(environment.serviceUrl.replace('service','bienes')
    +'cantidades-totales-por-tienda?fechadesde='+(fechaDesde ? this.formatDate(fechaDesde) : '') +'&fechahasta='+(fechaHasta? this.formatDate(fechaHasta): ''));
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
  getDashboard():Observable<Dashboard[]>{
    return this._http.get<Dashboard[]>(environment.serviceUrl.replace('service','bienes') + 'get-dashboards');
  }

  getAllIntercambioProveedor(): Observable<Agente[]>{
    return this._http.get<Agente[]>(environment.serviceUrl.replace('service','bienes') + 'intercambios-proveedor');
  }
  getIntercambioProveedorByNroP(nro: number): Observable<IntercambioProv[]>{
    return this._http.get<IntercambioProv[]>(environment.serviceUrl.replace('service','bienes') + 'intercambios-proveedor/' + nro);
  }
  getInformeMovimientos( origen : Agente,
                         destino: Agente,
                         bien : Bien,
                         recurso: Recurso,
                         transportista : Transportista,
                         tipo : TipoMovimiento,
                         estado : Estado,
                         fechaDesde : Date,
                         fechaHasta : Date,
                         cantidadBi : number,
                         usuarioAlta : string) : Observable<Movimiento[]>{
    let params = new HttpParams();

    fechaHasta? (params = params.append('fechaSalidaHasta', this.formatDate(fechaHasta))): null;
    fechaDesde? (params = params.append('fechaSalidaDesde', this.formatDate(fechaDesde))): null;
    origen? (params = params.append('origen', origen.nro.toString())): null;
    destino? (params = params.append('destino',destino.nro.toString())): null;
    tipo? (params = params.append('tipoMovimientoTipo',tipo.id.toString() ))  : null;
    origen?  (params = params.append('tipoAgenteOrigenNombre',origen.tipoAgente.nombre)): null;
    destino? (params = params.append('tipoAgenteDestinoNombre',destino.tipoAgente.nombre)): null;
    (usuarioAlta && usuarioAlta.trim() !== '')? (params = params.append('usuario',usuarioAlta )): null;
    estado ? ( params = params.append('EstadoViajeDescrip', estado.descrip)): null;
    transportista? (params = params.append('idTransportista',  transportista.id )): null;
    recurso ? (params = params.append('nroRecurso', recurso.nroRecurso.toString())): null;
    (cantidadBi && cantidadBi !== 0) ? (params = params.append('cantidadItemMovimiento', cantidadBi.toString())): null;
    bien? (params = params.append('bienIntercambiableId',bien.id.toString())): null;
    console.log(params);
    return this._http.get<Movimiento[]>(environment.serviceUrl.replace('service','bienes')+ 'informe-movimiento', {params : params});
  }
  private formatDate(date: Date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');
}
}
