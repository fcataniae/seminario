import { Component, OnInit } from '@angular/core';
import { Permiso }  from '../../model/abm/permiso.model';
import { PermisoService } from '../../services/permiso.service';
import { Token } from '../../model/token.model';
import {Observable} from 'rxjs';
import {map, startWith} from 'rxjs/operators';
import { Bien } from '../../model/bienes/bien.model';
import { MovimientoService } from '../../services/movimiento.service';
import { StockBienLocalService } from '../../services/stockbienlocal.service';
import { StockBienEnLocal } from '../../model/bienes/stockbienlocal.model';
import { Agente } from '../../model/bienes/agente.model';
import { MatTableDataSource, MatPaginator, MatSort, MatDialog, MatDialogRef } from '@angular/material';
import { Chart } from 'chart.js';
import { TiendaEstadisticas } from '../../model/bienes/tiendaEstadisticas.model';
import { SessionService } from '../../services/session.service';
import { Router } from '@angular/router';
import { LoginService } from '../../services/login.service';
import { FormControl } from '@angular/forms';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-informe-tiendas',
  templateUrl: './informe-tiendas.component.html',
  styleUrls: ['./informe-tiendas.component.css']
})
export class InformeTiendasComponent implements OnInit {

  tiendasEstadisticas: TiendaEstadisticas[];
  locales: Agente[];
  fechaInicio: Date;
  fechaFin: Date;
  totalEnviado: number;
  totalRecibido: number;

  chartEnviado:Chart;
  chartRecibido:Chart;

  constructor(private _loginService: LoginService,
              private _router: Router,
              private _sessionService: SessionService,
              private _permisoService: PermisoService,
              private _movimientoService: MovimientoService,
              private _stockbienlocalService: StockBienLocalService) { }

  ngOnInit() {

    this.tiendasEstadisticas = [];
    this.locales = [];
    this.fechaInicio = null;
    this.fechaFin = null;
    let consultaEstadisticas = this._movimientoService.getTiendasEstadisticas(this.fechaInicio, this.fechaFin);
    let consultaAgentes = this._movimientoService.getAllAgentes();

    forkJoin(consultaEstadisticas, consultaAgentes)
    .subscribe(res=>{
        console.log(res);
        this.tiendasEstadisticas = res[0];
        this.locales = res[1].filter( a => a.tipoAgente.id === 1);
      },
      error => console.log(error)
    );

    this.totalEnviado = 0;
    this.totalRecibido = 0;
    this.generarGraficos();

  }//END OnInit

  onChangeFecha(){

    this._movimientoService.getTiendasEstadisticas(this.fechaInicio, this.fechaFin)
    .subscribe(res=>{
        console.log(res);
        this.tiendasEstadisticas = res;
      },
      error => console.log(error)
    );

    this.generarGraficos();
  }

  generarGraficos(){

     if(this.tiendasEstadisticas.length){
        //Ordeno array de tiendas segun más envios
        let topTiendasEnvios: TiendaEstadisticas[] = this.tiendasEstadisticas.sort((obj1, obj2) => {
            if (obj1.cantEnviada > obj2.cantEnviada) {return 1;}
            if (obj1.cantEnviada < obj2.cantEnviada) {return -1;}
            return 0;
        });
        console.log(topTiendasEnvios);
        // bar chart:
        this.chartEnviado = new Chart('barEnviadoChart', {
            type: 'bar',
          data: {
           labels: [""+topTiendasEnvios[0].tiendaId, ""+topTiendasEnvios[1].tiendaId, ""+topTiendasEnvios[2].tiendaId,
                    ""+topTiendasEnvios[3].tiendaId, ""+topTiendasEnvios[4].tiendaId],
           datasets: [{
               data: [topTiendasEnvios[0].cantEnviada, topTiendasEnvios[1].cantEnviada, topTiendasEnvios[2].cantEnviada,
                      topTiendasEnvios[3].cantEnviada, topTiendasEnvios[4].cantEnviada],
               backgroundColor: [
                   'rgba(255, 206, 86, 1)',
                   'rgba(75, 192, 192, 1)',
                   'rgba(54, 162, 235, 1)',
                   'rgba(255, 99, 132, 1)',
                   'rgba(170, 70, 246, 1)',
               ]
           }]
          },
          options: {
            title:{
               text:"Top 5 locales con más envíos",
               display:true
            },
          }
        });

        //Ordeno array de tiendas segun más recibos
        let topTiendasRecibidos: TiendaEstadisticas[] = this.tiendasEstadisticas.sort((obj1, obj2) => {
            if (obj1.cantRecibida > obj2.cantRecibida) {return 1;}
            if (obj1.cantRecibida < obj2.cantRecibida) {return -1;}
            return 0;
        });

        // bar chart:
        this.chartRecibido = new Chart('barRecibidoChart', {
            type: 'bar',
          data: {
           labels: [""+topTiendasRecibidos[0].tiendaId, ""+topTiendasRecibidos[1].tiendaId, ""+topTiendasRecibidos[2].tiendaId,
                    ""+topTiendasRecibidos[3].tiendaId, ""+topTiendasRecibidos[4].tiendaId],
           datasets: [{
               data: [topTiendasRecibidos[0].cantRecibida, topTiendasRecibidos[1].cantRecibida, topTiendasRecibidos[2].cantRecibida,
                      topTiendasRecibidos[3].cantRecibida, topTiendasRecibidos[4].cantRecibida],
               backgroundColor: [
                   'rgba(255, 206, 86, 1)',
                   'rgba(75, 192, 192, 1)',
                   'rgba(54, 162, 235, 1)',
                   'rgba(255, 99, 132, 1)',
                   'rgba(170, 70, 246, 1)',
               ]
           }]
          },
          options: {
            title:{
               text:"Top 5 locales con más recepciones",
               display:true
            }
          }
        });
     }
  }


}
