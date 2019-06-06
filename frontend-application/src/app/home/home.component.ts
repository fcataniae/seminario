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
    this.fechaInicio = null;
    this.fechaFin = null;
    let fechas: Date[] = [this.fechaInicio, this.fechaFin];
    let consultaEstadisticas = this._movimientoService.getTiendasEstadisticas(fechas);
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
    //this.generarGraficos();

  }//END OnInit

  onChangeFecha(){

    let fechas: Date[] = [this.fechaInicio, this.fechaFin];

    this._movimientoService.getTiendasEstadisticas(fechas)
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

      // bar chart:
      this.chartEnviado = new Chart('barEnviadoChart', {
          type: 'bar',
        data: {
         labels: [""+topTiendasEnvios[0].id, ""+topTiendasEnvios[1].id, ""+topTiendasEnvios[2].id,
                  ""+topTiendasEnvios[3].id, ""+topTiendasEnvios[4].id],
         datasets: [{
             data: [topTiendasEnvios[0].enviado, topTiendasEnvios[1].enviado, topTiendasEnvios[2].enviado,
                    topTiendasEnvios[3].enviado, topTiendasEnvios[4].enviado],
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

      // bar chart:
      this.chartRecibido = new Chart('barRecibidoChart', {
          type: 'bar',
        data: {
         labels: [""+topTiendasRecibidos[0].id, ""+topTiendasRecibidos[1].id, ""+topTiendasRecibidos[2].id,
                  ""+topTiendasRecibidos[3].id, ""+topTiendasRecibidos[4].id],
         datasets: [{
             data: [topTiendasRecibidos[0].recibido, topTiendasRecibidos[1].recibido, topTiendasRecibidos[2].recibido,
                    topTiendasRecibidos[3].recibido, topTiendasRecibidos[4].recibido],
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
