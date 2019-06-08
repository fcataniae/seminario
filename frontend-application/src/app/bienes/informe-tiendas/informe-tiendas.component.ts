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
        this.generarGraficos();
        
      },
      error => console.log(error)
    );

    this.totalEnviado = 0;
    this.totalRecibido = 0;

  }//END OnInit

  onChangeFecha(){

    this._movimientoService.getTiendasEstadisticas(this.fechaInicio, this.fechaFin)
    .subscribe(res=>{
        console.log(res);
        this.tiendasEstadisticas = res;
        this.generarGraficos();

      },
      error => console.log(error)
    );


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
        let labels = [];
        let data = [];

        topTiendasEnvios.forEach( t => {
          labels.push(t.tiendaId.toString());
          data.push(t.cantEnviada.toString());
        });
        this.chartEnviado = new Chart(contextoEnviado, {
            type: 'bar',
          data: {
           labels: labels,
           datasets: [{
               label:"Envios por tienda",
               data: data,
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
        let labels2 = [];
        let data2 = [];
        topTiendasRecibidos.forEach( t => {
          labels2.push(t.tiendaId.toString());
          data2.push(t.cantRecibida.toString());
        });
        this.chartRecibido = new Chart(contextoRecibido, {
            type: 'bar',
          data: {
           labels: labels2,
           datasets: [{
               label:"Recepciones por tienda",
               data: data2,
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

    for(let j=0; j<recibos.length; j++){
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
