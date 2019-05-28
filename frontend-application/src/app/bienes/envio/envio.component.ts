import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource, MatPaginator, MatSort, MatDialog, MatDialogRef } from '@angular/material';
import { Location } from '@angular/common';
import { AgregarBienComponent } from '../agregar-bien/agregar-bien.component';
import { AgregarRecursoComponent } from '../agregar-recurso/agregar-recurso.component';
import { ItemMovimiento } from '../../model/bienes/itemmovimiento.model';
import { Recurso } from '../../model/bienes/recurso.model';
import { Movimiento } from '../../model/bienes/movimiento.model';
import { ActivatedRoute } from '@angular/router';
import { MovimientoService } from '../../services/movimiento.service';
import { of, forkJoin } from 'rxjs';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-envio',
  templateUrl: './envio.component.html',
  styleUrls: ['./envio.component.css']
})
export class EnvioComponent implements OnInit {

  @ViewChild(MatSort) sortBienes: MatSort;
  @ViewChild(MatPaginator) paginatorBienes: MatPaginator;
  datosTablaBienes = new MatTableDataSource<ItemMovimiento>();

  @ViewChild(MatSort) sortRecursos: MatSort;
  @ViewChild(MatPaginator) paginatorRecursos: MatPaginator;
  datosTablaRecursos = new MatTableDataSource<Recurso>();

  @ViewChild(MatSort) sortEnviosPendientes: MatSort;
  @ViewChild(MatPaginator) paginatorEnvios: MatPaginator;
  datosTablaEnvios = new MatTableDataSource<Movimiento>();

  columnsToDisplayBien = ['bien','tipoDoc','nroDoc','cantidad','vacio','eliminar'];
  columnsToDisplayRecurso = ['tipoRecurso','idRecurso','eliminar'];
  movimiento: Movimiento = new Movimiento();


  constructor(private location: Location,
              private dialogAgregarBien: MatDialog,
              private dialogAgregarRecurso: MatDialog,
              private route: ActivatedRoute,
              private _movimientoService: MovimientoService
              )
  {
  }

  ngOnInit() {


    console.log(JSON.parse(atob(this.route.snapshot.paramMap.get('mov'))));
    this.movimiento = JSON.parse(atob(this.route.snapshot.paramMap.get('mov')));
    this.datosTablaBienes.data = this.movimiento.itemMovimientos;
    this.datosTablaBienes.sort = this.sortBienes;
    this.datosTablaBienes.paginator = this.paginatorBienes;
    this.datosTablaRecursos.data = this.movimiento.recursosAsignados;
    this.datosTablaRecursos.sort = this.sortRecursos;
    this.datosTablaRecursos.paginator = this.paginatorRecursos;

    let consDocs = this._movimientoService.getAllTipoDocMovimientos();
    let consEnvios = of([]);
    if(this.movimiento.tipoMovimiento.tipo === 'ENVIO'){
      consEnvios = this._movimientoService.getEnviosPendientesByTienda(this.movimiento.destino);
    }

    forkJoin(consDocs,consEnvios)
      .pipe(
        map( ([resDoc,resEnvio]) =>
          {
              let tip = resDoc.filter(d => d.tipoMovimiento.id === this.movimiento.tipoMovimiento.id);

              tip[0].tipoDocumento.forEach( d => this.movimiento.tipoDocumento = d);

              if(this.movimiento.tipoMovimiento.tipo === 'ENVIO'){
                  this.datosTablaEnvios.data = resEnvio;
                  this.datosTablaEnvios.sort = this.sortEnviosPendientes;
                  this.datosTablaEnvios.paginator = this.paginatorEnvios;
              }

          }
        )
      );
    
  }//END OnInit

  goBack(): void {
    this.location.back();
  }

  onAgregarBien() {
    const dialogRef = this.dialogAgregarBien.open(AgregarBienComponent,{
      width: '50%'
    });

    dialogRef.afterClosed().subscribe(
      res=> {
        console.log(res instanceof ItemMovimiento);
        if(res instanceof ItemMovimiento){

          this.movimiento.itemMovimientos.push(res);
          console.log(this.movimiento.itemMovimientos);
          this.datosTablaBienes.data = this.movimiento.itemMovimientos;
        }
      }
    );
  }

  onAgregarRecurso() {
    const dialogRef = this.dialogAgregarRecurso.open(AgregarRecursoComponent,{
      width: '50%'
    });

    dialogRef.afterClosed().subscribe(
      res=>{
        console.log(res instanceof Recurso);
        if(res instanceof Recurso){

          this.movimiento.recursosAsignados.push(res);
          console.log(this.movimiento.recursosAsignados);
          this.datosTablaRecursos.data = this.movimiento.recursosAsignados;
        }
      }
    );
  }

  deleteBien(item: ItemMovimiento) {

    this.movimiento.itemMovimientos = this.movimiento.itemMovimientos.filter(
      i => JSON.stringify(i) != JSON.stringify(item)
    );
    console.log(this.movimiento);
    this.datosTablaBienes.data = this.movimiento.itemMovimientos;
  }

  deleteRecurso(recurso : Recurso) {

    this.movimiento.recursosAsignados = this.movimiento.recursosAsignados.filter(
      r => r.nroRecurso != recurso.nroRecurso
    );

    console.log(this.movimiento);
    this.datosTablaRecursos.data = this.movimiento.recursosAsignados;
  }

  registrar() {
    console.log(this.movimiento);
    this._movimientoService.setRegistroMovimiento(this.movimiento).subscribe(
      res =>{
        alert('Se registro correctamente el movimiento ' + this.movimiento.tipoMovimiento.nombre);
      },
      error => alert('Error al registrar el movimiento ' + this.movimiento.tipoMovimiento.nombre)
    );
  }

}
