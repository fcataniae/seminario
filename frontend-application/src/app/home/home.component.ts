import { Component, OnInit, ViewChild } from '@angular/core';
import { SessionService } from './../services/session.service';
import { Router } from '@angular/router';
import { LoginService } from "../services/login.service";
import {FormControl} from '@angular/forms';

import { Permiso }  from '../model/abm/permiso.model';
import { PermisoService } from './../services/permiso.service';
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

  myControlLocales = new FormControl();
  myControlBienes = new FormControl();
  filteredLocales: Observable<Agente[]>;
  filteredBienes: Observable<Bien[]>;
  bienes: Bien[];
  locales: Agente[];
  stockBienLocal: StockBienEnLocal[];
  selectedBien: string;
  selectedLocal: string;
  bienElegido: boolean;
  localElegido: boolean;
  columnsToDisplayBien = ['id', 'tipo', 'subtipo', 'descripcion'];

  @ViewChild("sortBienes") sortBienes: MatSort;
  datosTablaBienes = new MatTableDataSource<Bien>();

  chart:Chart;

  constructor(private _loginService: LoginService,
              private _router: Router,
              private _sessionService: SessionService,
              private _permisoService: PermisoService,
              private _movimientoService: MovimientoService,
              private _stockbienlocalService: StockBienLocalService) { }

  ngOnInit() {

    //TODO hacer un forkjoin, porque sino asumis que todo va a ser sincrono! y ver el scope
    this.bienes = [];
    this._movimientoService.getAllBienes()
    .subscribe(res=>{
        console.log(res);
        this.bienes = res;
      },
      error => console.log(error)
    );

    this.locales = [];
    this._movimientoService.getAllAgentes()
    .subscribe(res=>{
        this.locales = res.filter( a => a.tipoAgente.id !== 3);
        console.log(this.locales);
      },
      error => console.log(error)
    );

    this.filteredLocales = this.myControlLocales.valueChanges
    .pipe(
      startWith(''),
      map(value => this._filterLocal(value))
    );

    this.filteredBienes = this.myControlBienes.valueChanges
    .pipe(
      startWith(''),
      map(value => this._filterBien(value))
    );

    this.bienElegido = false;

  }//END OnInit

  private _filterLocal(value: string): Agente[] {
    const filterValue = value.toLowerCase();
    return this.locales.filter(local => local.nombre.toLowerCase().includes(filterValue));
  }

  private _filterBien(value: string): Bien[] {
    const filterValue = value.toLowerCase();
    return this.bienes.filter(bien => bien.descripcion.toLowerCase().includes(filterValue));
  }

  onChangeLocal(){
    //cargar los datos stock bien en local segun local
    let local = this.locales.filter(local => local.nombre === this.selectedLocal)[0];
    if(local){
      this.localElegido = true;
      this.stockBienLocal = [];
      this._stockbienlocalService.getStockLocal(local.nro)
      .subscribe(res=>{
          console.log(res);
          this.stockBienLocal = res;
        },
        error => console.log(error)
      );
    }
  }

  onChangeBien(){
    //crea grafico con stock del bien en el local antes elegido
    let bien = this.bienes.filter(bien => bien.descripcion === this.selectedBien);
    if(bien.length && this.localElegido){

      //Buscar en el array de stockBienLocal el stock del bien elegido (selectedBien)
      let bienElegido = this.bienes.filter(b => b.descripcion === this.selectedBien)[0];
      let stockBienElegido = this.stockBienLocal.filter(stockbien => stockbien.idBI === bienElegido.id)[0];

      // doughnut chart:
      this.chart = new Chart('doughnutChart', {
        type: 'pie',
      data: {
       labels: ["Ocupado", "Libre", "Reservado", "Destruido"],
       datasets: [{
           label: '# of Votes',
           data: [stockBienElegido.stock_ocupado, stockBienElegido.stock_libre,
                  stockBienElegido.stock_reservado, stockBienElegido.stock_destruido],
           backgroundColor: [
               'rgba(255, 206, 86, 1)',
               'rgba(75, 192, 192, 1)',
               'rgba(54, 162, 235, 1)',
               'rgba(255, 99, 132, 1)'
           ]
       }]
      },
      options: {
       title:{
           text:"Stock "+this.selectedBien+" en "+this.selectedLocal,
           display:true
       },
       cutoutPercentage: 50
      }
      });

      this.datosTablaBienes.data = bien;
      this.datosTablaBienes.sort = this.sortBienes;
      this.bienElegido = true;
    }
  }

}
