import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders } from '@angular/common/http';
import { StockBienEnLocal } from '../model/bienes/stockbienlocal.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class StockBienLocalService {

  constructor(private _http: HttpClient) { }

  getStockLocal(nroLocal: number): Observable<StockBienEnLocal[]>{
    return this._http.get<StockBienEnLocal[]>(environment.serviceUrl.replace('service','bienes') + 'stock-local/' + nroLocal);
  }
}
