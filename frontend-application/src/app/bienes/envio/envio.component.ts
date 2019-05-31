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
import { ConfirmarMovimientoComponent } from '../confirmar-movimiento/confirmar-movimiento.component';

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



  columnsRecepcionBien = ['bien','tipoDoc','nroDoc','cantidad','estado','eliminar'];
  columnsRecepcionGenerico = ['bien','cantidad','estado','eliminar'];
  columnsRecursoEnvio = ['tipoRecurso','idRecurso','eliminar'];
  columnsMovimientosEnvio = ['nro','estado','tipoDoc','nroDoc','cantidadItems','origen','confirmar'];


  columnsToDisplayBien: String[];

  movimiento: Movimiento = new Movimiento();


  constructor(private location: Location,
              private _dialog: MatDialog,
              private route: ActivatedRoute,
              private _movimientoService: MovimientoService,
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

    this.movimiento.tipoMovimiento.tipoDocumentos.forEach( d => this.movimiento.tipoDocumento = d);

    if(this.movimiento.tipoMovimiento.tipo === 'RECEPCION' && this.movimiento.tipoMovimiento.tipoAgenteOrigen.nombre === 'PROVEEDOR'){
      this.columnsToDisplayBien = this.columnsRecepcionBien;
    }else {
      this.columnsToDisplayBien = this.columnsRecepcionGenerico;
    }

    if(this.movimiento.tipoMovimiento.tipo === 'CONFIRMARRECEP'){
      this._movimientoService.getEnviosPendientesByTienda(this.movimiento.destino)
        .subscribe(
          resEnvio => {
            this.datosTablaEnvios.data = resEnvio;
            this.datosTablaEnvios.sort = this.sortEnviosPendientes;
            this.datosTablaEnvios.paginator = this.paginatorEnvios;
          },
          error => console.log(error)
        );
    }


  }//END OnInit

  goBack(): void {
    this.location.back();
  }

  onAgregarBien() {
    const dialogRef = this._dialog.open(AgregarBienComponent,{
      width: '50%',
      data: { tipoMovimiento: this.movimiento.tipoMovimiento }
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
    const dialogRef = this._dialog.open(AgregarRecursoComponent,{
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
  confirmarMovimientoEnvio(element : Movimiento){
    let dialog = this._dialog.open(ConfirmarMovimientoComponent,{
      width: '50%',
      data : { movimiento : element}
    });

    dialog.afterClosed()
      .subscribe(
        res => {
          console.log('instance '+ (res != null));
          if (res != null){
            this.datosTablaEnvios.data = this.datosTablaEnvios.data.filter(m => m.id !== res.id);
          }
        }
      );

  }
}
