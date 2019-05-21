import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { TipoMovimiento } from '../model/bienes/tipomovimiento.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MovimientoService {

  constructor(private _http: HttpClient) { }

  getAllTipoMovimientos() : Observable<TipoMovimiento[]>{
    return this._http.get<TipoMovimiento[]>(environment.serviceUrl.replace('service','bienes') + 'listar-movimientos');
  }
  
}
