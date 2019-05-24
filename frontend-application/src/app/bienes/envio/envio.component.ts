import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource, MatPaginator, MatSort, MatDialog, MatDialogRef } from '@angular/material';
import { Location } from '@angular/common';
import { AgregarBienComponent } from '../agregar-bien/agregar-bien.component';
import { AgregarRecursoComponent } from '../agregar-recurso/agregar-recurso.component';
import { ItemMovimiento } from '../../model/bienes/itemmovimiento.model';
import { Recurso } from '../../model/bienes/recurso.model';
import { Movimiento } from '../../model/bienes/movimiento.model';

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

  columnsToDisplayBien = ['bien','tipoDoc','nroDoc','cantidad','vacio','eliminar'];
  columnsToDisplayRecurso = ['tipoRecurso','idRecurso','eliminar'];
  movimiento: Movimiento;

  constructor(private location: Location,
              private dialogAgregarBien: MatDialog,
              private dialogAgregarRecurso: MatDialog,
              ) { }

  ngOnInit() {

  this.movimiento = new Movimiento();


  this.datosTablaBienes.data = this.movimiento.items;
  this.datosTablaBienes.sort = this.sortBienes;
  this.datosTablaBienes.paginator = this.paginatorBienes;

  let recursosAgregados: Recurso[] = [{ tipoRecurso: 'Termógrafo', idRecurso: 0}];

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

    dialogRef.afterClosed().subscribe(
      res=> {
        console.log(res instanceof ItemMovimiento);
        if(res instanceof ItemMovimiento){
          this.movimiento.items.push(res);
          console.log(this.movimiento.items);
          this.datosTablaBienes.data = this.movimiento.items;
        }
      }
    );
  }

  onAgregarRecurso() {
    const dialogRef = this.dialogAgregarRecurso.open(AgregarRecursoComponent,{
      width: '50%'
    });
  }

  deleteBien() {

  }

  deleteRecurso() {

  }

  registrar() {

  }

  /*BORRAR*/
  isEnvio: boolean = true;

}
