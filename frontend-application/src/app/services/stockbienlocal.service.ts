import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders } from '@angular/common/http';
import { StockBienEnLocal } from '../model/bienes/stockbienlocal.model';
import { environment } from '../../environments/environment';
import { Local } from '../model/bienes/local.model';
import { Proveedor } from '../model/bienes/proveedor.model';
import { Bien } from '../model/bienes/bien.model';
import { Agente } from '../model/bienes/agente.model';

@Injectable({
  providedIn: 'root'
})
export class StockBienLocalService {

  constructor(private _http: HttpClient) { }

  getStockLocal(nroLocal: number): Observable<StockBienEnLocal[]>{
    return this._http.get<StockBienEnLocal[]>(environment.serviceUrl.replace('service','bienes') + 'stock-local/' + nroLocal);
  }
  getStockLocales(
    fechaActualizacionDesde : Date,
    fechaActualizacionHasta : Date,
    local : Agente,
    bi: Bien,
    cantMinStockLibre: number,
    cantMaxStockLibre: number,
    cantMinStockOcupado: number,
    cantMaxStockOcupado: number,
    cantMinStockReservado: number,
    cantMaxStockReservado: number,
    cantMinStockDestruido: number,
    cantMaxStockDestruido: number
  ): Observable<Local[]>{
    let params = new HttpParams();
    fechaActualizacionDesde? (params = params.append('fechaActualizacionDesde', this.formatDate(fechaActualizacionDesde))): null;
    fechaActualizacionHasta? (params = params.append('fechaActualizacionHasta', this.formatDate(fechaActualizacionHasta))): null;
    local? (params = params.append('localNro', local.nro.toString())): null;
    bi? (params = params.append('bienId', bi.id.toString())): null;
    cantMinStockLibre? (params = params.append('cantMinStockLibre', cantMinStockLibre.toString())): null;
    cantMaxStockLibre? (params = params.append('cantMaxStockLibre', cantMaxStockLibre.toString())): null;
    cantMinStockOcupado? (params = params.append('cantMinStockOcupado', cantMinStockOcupado.toString())): null;
    cantMaxStockOcupado? (params = params.append('cantMaxStockOcupado', cantMaxStockOcupado.toString())): null;
    cantMaxStockReservado? (params = params.append('cantMaxStockReservado', cantMaxStockReservado.toString())): null;
    cantMinStockReservado? (params = params.append('cantMinStockReservado', cantMinStockReservado.toString())): null;
    cantMinStockDestruido? (params = params.append('cantMinStockDestruido', cantMinStockDestruido.toString())): null;
    cantMaxStockDestruido? (params = params.append('cantMaxStockDestruido', cantMaxStockDestruido.toString())): null;
    console.log(params);
    return this._http.get<Local[]>(environment.serviceUrl.replace('service','bienes') + 'stock-locales', {params : params});
  }
  getDeudaProveedores(): Observable<Proveedor[]>{
    return this._http.get<Proveedor[]>(environment.serviceUrl.replace('service','bienes') + 'deuda-proveedores');
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
