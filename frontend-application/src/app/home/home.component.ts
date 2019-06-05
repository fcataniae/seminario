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

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})

export class HomeComponent implements OnInit {

  tiendasEstadisticas: tiendaEstadisticas[];
  locales: Agente[];
  fechaElegida: boolean;

  chart:Chart;

  constructor(private _loginService: LoginService,
              private _router: Router,
              private _sessionService: SessionService,
              private _permisoService: PermisoService,
              private _movimientoService: MovimientoService,
              private _stockbienlocalService: StockBienLocalService) { }

  ngOnInit() {

    this.fechaElegida = false;

    this.tiendasEstadisticas = [];
    this.locales = [];
    let consultaEstadisticas = this._movimientoService.getTiendasEstadisticas();
    let consultaAgentes = this._movimientoService.getAllAgentes();

    forkJoin(consultaEstadisticas, consultaAgentes)
    .subscribe(res=>{
        console.log(res);
        this.tiendasEstadisticas = res[0];
        this.locales = res[1].filter( a => a.tipoAgente.id === 1);
      },
      error => console.log(error)
    );

    this.generarGraficos();

  }//END OnInit

  onChangeFecha(){
    this.fechaElegida = true;
    this.generarGraficos();
  }

  generarGraficos(){
      let tienda1 = this.tiendas[0].id;
      let tienda2 = this.tiendas[1].id;
      let tienda3 = this.tiendas[2].id;
      let tienda4 = this.tiendas[3].id;
      let tienda5 = this.tiendas[4].id;

      let enviadoTienda1 = this.tiendas[0].enviado;
      let enviadoTienda2 = this.tiendas[1].enviado;
      let enviadoTienda3 = this.tiendas[2].enviado;
      let enviadoTienda4 = this.tiendas[3].enviado;
      let enviadoTienda5 = this.tiendas[4].enviado;

      // bar chart:
      this.chart = new Chart('barChart', {
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
     }
  }

}
