import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource, MatPaginator, MatSort, MatDialog, MatDialogRef } from '@angular/material';
import { Location } from '@angular/common';
import { AgregarBienComponent } from '../agregar-bien/agregar-bien.component';
import { AgregarRecursoComponent } from '../agregar-recurso/agregar-recurso.component';
import { Bien } from '../../model/bienes/bien.model';
import { Recurso } from '../../model/bienes/recurso.model';

@Component({
  selector: 'app-envio',
  templateUrl: './envio.component.html',
  styleUrls: ['./envio.component.css']
})
export class EnvioComponent implements OnInit {

  @ViewChild(MatSort) sortBienes: MatSort;
  @ViewChild(MatPaginator) paginatorBienes: MatPaginator;
  datosTablaBienes = new MatTableDataSource<Bien>();

  @ViewChild(MatSort) sortRecursos: MatSort;
  @ViewChild(MatPaginator) paginatorRecursos: MatPaginator;
  datosTablaRecursos = new MatTableDataSource<Recurso>();

  columnsToDisplayBien = ['tipoBien','bien','tipoDoc','nroDoc','cantidad','vacio'];
  columnsToDisplayRecurso = ['tipoRecurso','idRecurso'];

  constructor(private location: Location,
              private dialogAgregarBien: MatDialog,
              private dialogAgregarRecurso: MatDialog,
              ) { }

  ngOnInit() {

  let bienesAgregados: Bien[] = [{tipoBien: 'Pallet', bien: 'ARLOG', tipoDoc: 'Recibo', nroDoc: 0, cantidad: 5, vacio: false}];

  this.datosTablaBienes.data = bienesAgregados;
  this.datosTablaBienes.sort = this.sortBienes;
  this.datosTablaBienes.paginator = this.paginatorBienes;

  let recursosAgregados: Recurso[] = [{ posicion: 0, tipoRecurso: 'Termógrafo', idRecurso: 0}];

  this.datosTablaRecursos.data = recursosAgregados;
  this.datosTablaRecursos.sort = this.sortRecursos;
  this.datosTablaRecursos.paginator = this.paginatorRecursos;

  }//END OnInit

  goBack(): void {
    this.location.back();
  }

  onAgregarBien() {
    const dialogRef = this.dialogAgregarBien.open(AgregarBienComponent,{
      width: '50%'
    });
  }

  onAgregarRecurso() {
    const dialogRef = this.dialogAgregarRecurso.open(AgregarRecursoComponent,{
      width: '50%'
    });
  }

}
