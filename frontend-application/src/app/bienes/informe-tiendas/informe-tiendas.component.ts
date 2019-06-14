import { Component, OnInit, ViewChild } from '@angular/core';
import { Permiso }  from '../../model/abm/permiso.model';
import { PermisoService } from '../../services/permiso.service';
import { Token } from '../../model/token.model';
import { BehaviorSubject} from 'rxjs';
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
import { Dashboard } from '../../model/bienes/dashboard.model';

export class Tienda{
  nro: number;
  nombre: string;
}

@Component({
  selector: 'app-informe-tiendas',
  templateUrl: './informe-tiendas.component.html',
  styleUrls: ['./informe-tiendas.component.css']
})
export class InformeTiendasComponent implements OnInit {

  @ViewChild("sortEnvios") sortEnvios: MatSort;
  @ViewChild("paginatorEnvios") paginatorEnvios: MatPaginator;
  datosTablaEnvios = new MatTableDataSource<Tienda>();

  @ViewChild("sortRecepciones") sortRecepciones: MatSort;
  @ViewChild("paginatorRecepciones") paginatorRecepciones: MatPaginator;
  datosTablaRecepciones = new MatTableDataSource<Tienda>();

  selectedIndex: number ;

  columnsToDisplayTiendas: String[] = ['id','nombre'];


  fechaInicio :Date = new Date();
  fechaFin :Date = new Date();
  totalEnviado: number;
  totalRecibido: number;

  dashboardRecepcion: Dashboard;
  dashboardEnvio: Dashboard;

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
  this.selectedIndex = 0;
  this.fechaFin = null;
  this.fechaInicio = null;
  this.getDashboardData();
  this.totalEnviado = 0;
  this.totalRecibido = 0;

  }//END OnInit

  getDashboardData(){
    this._movimientoService.getDashboardTiendas(this.fechaInicio, this.fechaFin).subscribe(r=>{

      console.log(r);
      this.dashboardRecepcion = r[0];
      this.dashboardEnvio = r[1];
      this.fillTables();
      this.generarGraficos();

    });


  }


  fillTables(){
    let envis = [];
    this.dashboardEnvio.data.labels.forEach(l => {
      var a : Tienda = new Tienda();
      a.nombre = l.split("-")[1];
      a.nro = parseInt( l.split("-")[0]);
      envis.push(a);
    });
    console.log(envis);
    this.datosTablaEnvios.data = envis;
    let receps = [];
    this.dashboardRecepcion.data.labels.forEach(l => {
      var a : Tienda = new Tienda();
      a.nombre = l.split("-")[1];
      a.nro = parseInt( l.split("-")[0]);
      receps.push(a);
    });
    console.log(receps);
    this.datosTablaRecepciones.data = receps;
  }

  generarGraficos(){

      console.log(this.selectedIndex);
       let topTiendasEnvios: TiendaEstadisticas[];
       let topTiendasRecibidos: TiendaEstadisticas[];
       if(this.selectedIndex == 0 && this.dashboardEnvio){//Hago una chanchada por aca (?)

          let divTab = document.getElementsByTagName("mat-tab-body")[0].firstChild;
          let canvas = document.getElementById("canvasEnvios");
          let div;
          if(canvas == null){
            canvas = document.createElement("canvas");
            canvas.id="canvasEnvios";
            div = document.createElement("div");
            div.setAttribute("style", "display: inline-block; width: 35vw; height: 40vh; margin-left:3vw;");
            div.appendChild(canvas);
            divTab.appendChild(div);
          }




          let canvasAux = <HTMLElement> canvas;
          let labels= [];
          this.dashboardEnvio.data.labels.forEach(l => labels.push( l.split("-")[0]));
          this.chartEnviado = new Chart(canvasAux, {
              type: this.dashboardEnvio.type,
            data: {
             labels: labels,
             datasets: [{
                 label:"Envios por tienda",
                 data: this.dashboardEnvio.data.dataset.data,
                 backgroundColor: this.dashboardEnvio.data.dataset.backgroundColor
             }]
            },
            options: {
              title:{
                 text: this.dashboardEnvio.data.dataset.label,
                 display:true
              },
            }
          });
        }else if(this.dashboardRecepcion){
          //Ordeno array de tiendas segun más recibos
          let divTab = document.getElementsByTagName("mat-tab-body")[1].firstChild;
          // bar chart:
          let canvas = document.getElementById("canvasRecibos");
          let div;
          if(canvas == null){
            canvas = document.createElement("canvas");
            canvas.id="canvasRecibos";
            div = document.createElement("div");
            div.setAttribute("style", "display: inline-block; width: 35vw; height: 40vh; margin-left:3vw;");
            div.appendChild(canvas);
            divTab.appendChild(div);
          }

          let canvasAux = <HTMLElement> canvas;

          let labels= [];
          this.dashboardRecepcion.data.labels.forEach(l => labels.push( l.split("-")[0]));
          this.chartRecibido = new Chart(canvasAux, {
              type: this.dashboardRecepcion.type,
            data: {
             labels: labels,
             datasets: [{
                 label:"Recepciones por tienda",
                 data: this.dashboardRecepcion.data.dataset.data,
                 backgroundColor: this.dashboardRecepcion.data.dataset.backgroundColor
             }]
            },
            options: {
              title:{
                 text: this.dashboardRecepcion.data.dataset.label,
                 display:true
              }
            }
          });
      }
  }


  setDataSource(indexNumber) {
    this.selectedIndex = this.selectedIndex == 0 ? 1:0;
    console.log("onchange"+ this.selectedIndex);
    setTimeout(() => {
      switch (this.selectedIndex) {
        case 0:
          !this.datosTablaEnvios.paginator ? this.datosTablaEnvios.paginator = this.paginatorEnvios : null;
          this.generarGraficos();
          break;
        case 1:
          !this.datosTablaRecepciones.paginator ? this.datosTablaRecepciones.paginator = this.paginatorRecepciones : null;
          this.generarGraficos();

      }
    });
  }


}
