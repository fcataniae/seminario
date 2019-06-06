import { Component, OnInit, ViewChild } from '@angular/core';
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

  @ViewChild("sortEnvios") sortEnvios: MatSort;
  @ViewChild("paginatorEnvios") paginatorEnvios: MatPaginator;
  datosTablaEnvios = new MatTableDataSource<Agente>();

  @ViewChild("sortRecepciones") sortRecepciones: MatSort;
  @ViewChild("paginatorRecepciones") paginatorRecepciones: MatPaginator;
  datosTablaRecepciones = new MatTableDataSource<Agente>();

  columnsToDisplayTiendas: String[] = ['id','nombre'];

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

  this.datosTablaEnvios.sort = this.sortEnvios;
  this.datosTablaEnvios.paginator = this.paginatorEnvios;
  this.datosTablaRecepciones.sort = this.sortRecepciones;
  this.datosTablaRecepciones.paginator = this.paginatorRecepciones;

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
        console.log("top envios");
        console.log(topTiendasEnvios);
        // bar chart:
        let barEnviadoChart = document.getElementById('barEnviadoChart');
        let contextoEnviado = <HTMLCanvasElement> barEnviadoChart;
        this.chartEnviado = new Chart(contextoEnviado, {
            type: 'bar',
          data: {
           labels: [""+topTiendasEnvios[0].tiendaId, ""+topTiendasEnvios[1].tiendaId, ""+topTiendasEnvios[2].tiendaId,
                    ""+topTiendasEnvios[3].tiendaId, ""+topTiendasEnvios[4].tiendaId],
           datasets: [{
               label:"Envios por tienda",
               data: [topTiendasEnvios[0].cantEnviada, topTiendasEnvios[1].cantEnviada, topTiendasEnvios[2].cantEnviada,
                      topTiendasEnvios[3].cantEnviada, topTiendasEnvios[4].cantEnviada],
               backgroundColor: 'rgba(75, 192, 192, 1)'
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
        console.log("top recibidos");
        console.log(topTiendasRecibidos);
        // bar chart:
        let barRecibidoChart = document.getElementById('barRecibidoChart');
        let contextoRecibido = <HTMLCanvasElement> barRecibidoChart;
        this.chartRecibido = new Chart(contextoRecibido, {
            type: 'bar',
          data: {
           labels: [""+topTiendasRecibidos[0].tiendaId, ""+topTiendasRecibidos[1].tiendaId, ""+topTiendasRecibidos[2].tiendaId,
                    ""+topTiendasRecibidos[3].tiendaId, ""+topTiendasRecibidos[4].tiendaId],
           datasets: [{
               label:"Recepciones por tienda",
               data: [topTiendasRecibidos[0].cantRecibida, topTiendasRecibidos[1].cantRecibida, topTiendasRecibidos[2].cantRecibida,
                      topTiendasRecibidos[3].cantRecibida, topTiendasRecibidos[4].cantRecibida],
               backgroundColor: 'rgba(54, 162, 235, 1)'
           }]
          },
          options: {
            title:{
               text:"Top 5 locales con más recepciones",
               display:true
            }
          }
        });

        this.calcularTotalEnviadoYRecibido();
        this.generarTablas(topTiendasEnvios, topTiendasRecibidos);
     }
  }

  calcularTotalEnviadoYRecibido(){
    this.totalEnviado = this.sumaEnviado();
    this.totalRecibido = this.sumaRecibido();
  }

  sumaEnviado(){
    let suma = 0;
    for(let i=0; i<this.tiendasEstadisticas.length; i++){
      suma += this.tiendasEstadisticas[i].cantEnviada;
    }
    return suma;
  }

  sumaRecibido(){
    let suma = 0;
    for(let i=0; i<this.tiendasEstadisticas.length; i++){
      suma += this.tiendasEstadisticas[i].cantRecibida;
    }
    return suma;
  }

  generarTablas(envios: TiendaEstadisticas[], recibos: TiendaEstadisticas[]){
    let topLocalEnvios: Agente[] = [];
    let topLocalRecibidos: Agente[] = [];
    let agente: Agente;

    for(let i=0; i<envios.length; i++){
      agente = this.locales.filter(local => local.nro === envios[i].tiendaId)[0];
      topLocalEnvios.push(agente);
    }

    for(let j=0; j<envios.length; j++){
      agente = this.locales.filter(local => local.nro === recibos[j].tiendaId)[0];
      topLocalRecibidos.push(agente);
    }

    this.datosTablaEnvios.data = topLocalEnvios;
    this.datosTablaRecepciones.data = topLocalRecibidos;
  }

  setDataSource(indexNumber) {
    setTimeout(() => {
      switch (indexNumber) {
        case 0:
          !this.datosTablaEnvios.paginator ? this.datosTablaEnvios.paginator = this.paginatorEnvios : null;
          break;
        case 1:
          !this.datosTablaRecepciones.paginator ? this.datosTablaRecepciones.paginator = this.paginatorRecepciones : null;
      }
    });
  }


}
