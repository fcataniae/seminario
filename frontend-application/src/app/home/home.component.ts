import { Component, OnInit } from '@angular/core';
import { Permiso }  from '../model/abm/permiso.model';
import { PermisoService } from '../services/permiso.service';
import { Token } from '../model/token.model';
import {Observable} from 'rxjs';
import {map, startWith} from 'rxjs/operators';
import { Bien } from '../model/bienes/bien.model';
import { MovimientoService } from '../services/movimiento.service';
import { StockBienLocalService } from '../services/stockbienlocal.service';
import { StockBienEnLocal } from '../model/bienes/stockbienlocal.model';
import { Agente } from '../model/bienes/agente.model';
import { MatTableDataSource, MatPaginator, MatSort, MatDialog, MatDialogRef } from '@angular/material';
import { Chart } from 'chart.js';
import { TiendaEstadisticas } from '../model/bienes/tiendaEstadisticas.model';
import { SessionService } from '../services/session.service';
import { Router } from '@angular/router';
import { LoginService } from '../services/login.service';
import { FormControl } from '@angular/forms';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})

export class HomeComponent implements OnInit {

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

    this.fechaInicio = null;
    this.fechaFin = null;
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

      //Ordeno array de tiendas segun más envios
      let topTiendasEnvios: TiendaEstadisticas[] = this.tiendasEstadisticas.sort((obj1, obj2) => {
          if (obj1.enviado > obj2.enviado) {return 1;}
          if (obj1.enviado < obj2.enviado) {return -1;}
          return 0;
      });

      let tienda1 = ""+topTiendasEnvios[0].id;
      let tienda2 = ""+topTiendasEnvios[1].id;
      let tienda3 = ""+topTiendasEnvios[2].id;
      let tienda4 = ""+topTiendasEnvios[3].id;
      let tienda5 = ""+topTiendasEnvios[4].id;

      let enviadoTienda1 = topTiendasEnvios[0].enviado;
      let enviadoTienda2 = topTiendasEnvios[1].enviado;
      let enviadoTienda3 = topTiendasEnvios[2].enviado;
      let enviadoTienda4 = topTiendasEnvios[3].enviado;
      let enviadoTienda5 = topTiendasEnvios[4].enviado;

      // bar chart:
      this.chartEnviado = new Chart('barEnviadoChart', {
          type: 'bar',
        data: {
         labels: [tienda1, tienda2, tienda3, tienda4, tienda5],
         datasets: [{
             data: [enviadoTienda1, enviadoTienda2, enviadoTienda3, enviadoTienda4, enviadoTienda5],
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
             text:"Top 10 locales con más envíos",
             display:true
          },
          cutoutPercentage: 50,
          tooltips: {enabled: false},
          hover: {mode: null},
        }
      });

      //Ordeno array de tiendas segun más recibos
      let topTiendasRecibidos: TiendaEstadisticas[] = this.tiendasEstadisticas.sort((obj1, obj2) => {
          if (obj1.recibido > obj2.recibido) {return 1;}
          if (obj1.recibido < obj2.recibido) {return -1;}
          return 0;
      });

      let tienda1 = ""+topTiendasRecibidos[0].id;
      let tienda2 = ""+topTiendasRecibidos[1].id;
      let tienda3 = ""+topTiendasRecibidos[2].id;
      let tienda4 = ""+topTiendasRecibidos[3].id;
      let tienda5 = ""+topTiendasRecibidos[4].id;

      let recibidoTienda1 = topTiendasRecibidos[0].recibido;
      let recibidoTienda2 = topTiendasRecibidos[1].recibido;
      let recibidoTienda3 = topTiendasRecibidos[2].recibido;
      let recibidoTienda4 = topTiendasRecibidos[3].recibido;
      let recibidoTienda5 = topTiendasRecibidos[4].recibido;

      // bar chart:
      this.chartRecibido = new Chart('barRecibidoChart', {
          type: 'bar',
        data: {
         labels: [tienda1, tienda2, tienda3, tienda4, tienda5],
         datasets: [{
             data: [recibidoTienda1, recibidoTienda2, recibidoTienda3, recibidoTienda4, recibidoTienda5],
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
             text:"Top 10 locales con más recepciones",
             display:true
          },
          cutoutPercentage: 50,
          tooltips: {enabled: false},
          hover: {mode: null},
        }
      });
  }


}
