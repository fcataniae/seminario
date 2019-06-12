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

    console.log(this.dashboards);
    console.log(this.dashboards.length);
    console.log(this.charts);
     if(this.dashboards){
        let index = 0;
        let container = document.getElementById("container");
        console.log(container);
        this.dashboards.forEach(d => {
          let div = document.createElement("div");
          let canvas = document.createElement("canvas");

          div.classList.add("grafico");div.classList.add("mat-h2");div.classList.add("mat-elevation-z2");
          canvas.id = "chart"+index;
          div.setAttribute("style", "display: inline-block; width: 40vw; heigth: 40vh; margin-left:5vw;");
          div.appendChild(canvas);
          this.charts[index] = new Chart(canvas,{
            type: d.type,
            data:{
              labels: d.data.labels,
              datasets:[{
                label: d.data.dataset.label,
                data:d.data.dataset.data,
                backgroundColor: d.data.dataset.backgroundColor
              }]
            },
            options: {
              title:{
                 text:d.data.dataset.label,
                 display:true,
                 responsive: true,
                 maintainAspectRatio: false,
                 fontSize: 18
              },
            }
          });
          container.appendChild(div);
          index++;
        });

      }
    }
}
