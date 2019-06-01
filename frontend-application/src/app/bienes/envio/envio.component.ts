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
import { ConfirmacionPopupComponent } from '../../adm-usuarios/confirmacion-popup/confirmacion-popup.component';

export class Movi{
  nro: number;
  estado: string;
  origen: string;
  tipoDoc: string;
  nroDoc: number;
  cantBienes: number;
}

@Component({
  selector: 'app-envio',
  templateUrl: './envio.component.html',
  styleUrls: ['./envio.component.css']
})
export class EnvioComponent implements OnInit {

  @ViewChild("sortBienes") sortBienes: MatSort;
  @ViewChild("paginatorBienes") paginatorBienes: MatPaginator;
  datosTablaBienes = new MatTableDataSource<ItemMovimiento>();

  @ViewChild("sortRecursos") sortRecursos: MatSort;
  @ViewChild("paginatorRecursos") paginatorRecursos: MatPaginator;
  datosTablaRecursos = new MatTableDataSource<Recurso>();

  @ViewChild("sortEnvio") sortEnviosPendientes: MatSort;
  @ViewChild("paginatorEnvio") paginatorEnvios: MatPaginator;
  datosTablaEnvios = new MatTableDataSource<Movi>();



  columnsRecepcionBien = ['bien','tipoDoc','nroDoc','cantidad','estado','eliminar'];
  columnsRecepcionGenerico = ['bien','cantidad','estado','eliminar'];
  columnsRecursoEnvio = ['tipoRecurso','idRecurso','eliminar'];
  columnsMovimientosEnvio = ['nro','estado','tipoDoc','nroDoc','cantidadItems','origen','confirmar'];


  columnsToDisplayBien: String[];

  movimiento: Movimiento = new Movimiento();
  movimientosEnvio: Movimiento[] = [];

  constructor(private location: Location,
              private _dialog: MatDialog,
              private route: ActivatedRoute,
              private _movimientoService: MovimientoService,
              )
  {
  }

  ngOnInit() {

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
            console.log(resEnvio);
            this.movimientosEnvio = resEnvio;
            this.datosTablaEnvios.data = this.movimientoToMovi();
            this.datosTablaEnvios.sort = this.sortEnviosPendientes;
            this.datosTablaEnvios.paginator = this.paginatorEnvios;
            console.log(this.datosTablaEnvios);
          },
          error => console.log(error)
        );
    }


  }//END OnInit

  goBack(): void {
    let dialog = this._dialog.open(ConfirmacionPopupComponent,{
      data: {mensaje:"Desea volver atras?"},
      width: '50%'
    });
    dialog.afterClosed().subscribe(
      result =>{
        if (result && result == "true")
            this.location.back();

      }
    );


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
  confirmarMovimientoEnvio(element : Movi){
    let movimiento = this.movimientosEnvio.filter(m => m.id = element.nro)[0];
    let dialog = this._dialog.open(ConfirmarMovimientoComponent,{
      width: '50%',
      data : { movimiento : movimiento}
    });

    dialog.afterClosed()
      .subscribe(
        res => {
          console.log('instance '+ (res != null));
          if (res != null){
            this.movimientosEnvio = this.movimientosEnvio.filter(m => m.id != res.id);
            this.datosTablaEnvios.data = this.movimientoToMovi();
          }
        }
      );

  }

  doFilterBienes (value: string)  {
      console.log(value);
      this.datosTablaBienes.filter = value.trim().toLocaleLowerCase();
  }
  doFilterRecursos (value: string)  {
      console.log(value);
      this.datosTablaRecursos.filter = value.trim().toLocaleLowerCase();
  }
  doFilterEnvios (value: string)  {
      console.log(value);
      this.datosTablaEnvios.filter = value.trim().toLocaleLowerCase();

      console.log(this.datosTablaEnvios);
  }
  setDataSource(indexNumber) {
    setTimeout(() => {
      switch (indexNumber) {
        case 0:
          !this.datosTablaBienes.paginator ? this.datosTablaBienes.paginator = this.paginatorBienes : null;
          break;
        case 1:
          !this.datosTablaRecursos.paginator ? this.datosTablaRecursos.paginator = this.paginatorRecursos : null;
      }
    });
  }


  movimientoToMovi() : Movi[]{

    let movis:Movi[] = [];
    console.log(this.movimientosEnvio);
    this.movimientosEnvio.forEach(m => {
       let movi: Movi = new Movi();
       movi.nro = m.id;
       movi.tipoDoc = m.tipoDocumento.descripcion;
       movi.estado = m.estado.descrip;
       movi.nroDoc = m.nroDocumento;
       movi.origen = m.tipoMovimiento.tipoAgenteOrigen.nombre + " - " + m.origen;
       movi.cantBienes = m.itemMovimientos.length;
       movis.push(movi);
    });

    return movis;
  }
}
