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
import { Dashboard } from '../model/bienes/dashboard.model';
import { Data } from '../model/bienes/data.model';
import { Dataset } from '../model/bienes/dataset.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})

export class HomeComponent implements OnInit {

  chart1:Chart;
  chart2:Chart;
  chart3:Chart;
  chart4:Chart;
  charts:Chart[];
  dashboards: Dashboard[];

  constructor(private _movimientoService: MovimientoService) { }

  ngOnInit() {
    this.charts = [];
    this.charts.push(this.chart1);
    this.charts.push(this.chart2);
    this.charts.push(this.chart3);
    this.charts.push(this.chart4);

    this.dashboards = [];
    this._movimientoService.getDashboard()
    .subscribe(res=>{
        console.log(res);
        this.dashboards = res;
        this.generarGraficos();

      },
      error => console.log(error)
    );

   }//END OnInit

  generarGraficos(){

     if(this.dashboards.length){

        // bar chart:
        for(let i=0; i<this.dashboards.length; i++){
          let chart = document.getElementById('chart'+i);
          let contexto = <HTMLCanvasElement> chart;
          let labels = [];
          let data = [];
          let type: string;
          let label: string;
          let backgroundColor = [];

          type = this.dashboards[i].type;
          labels = this.dashboards[i].data.labels;
          data = this.dashboards[i].data.dataset.data;
          label = this.dashboards[i].data.dataset.label;
          backgroundColor = this.dashboards[i].data.dataset.backgroundColor;

          this.charts[i] = new Chart(contexto, {
              type: type,
            data: {
             labels: labels,
             datasets: [{
                 label: label,
                 data: data,
                 backgroundColor: backgroundColor
             }]
            },
            options: {
              title:{
                 text:"Dashboard",
                 display:true
              },
            }
          });
        }
      }
    }
}
