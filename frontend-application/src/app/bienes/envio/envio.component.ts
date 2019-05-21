import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource, MatPaginator, MatSort } from '@angular/material';

export class Bien{
  tipoBien: string;
  bien: string;
  tipoDoc: string;
  nroDoc: number;
  cantidad: number;
  vacio: boolean;
}

export class Recurso{
  tipoRecurso: string;
  idRecurso: number;
}

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

  constructor() { }

  ngOnInit() {

  let bienesAgregados: Bien[] = [{tipoBien: 'Pallet', bien: 'ARLOG', tipoDoc: 'Recibo', nroDoc: 0, cantidad: 5, vacio: false}];

  this.datosTablaBienes.data = bienesAgregados;
  this.datosTablaBienes.sort = this.sortBienes;
  this.datosTablaBienes.paginator = this.paginatorBienes;

  let recursosAgregados: Recurso[] = [{tipoRecurso: 'Termógrafo', idRecurso: 0}];

  this.datosTablaRecursos.data = recursosAgregados;
  this.datosTablaRecursos.sort = this.sortRecursos;
  this.datosTablaRecursos.paginator = this.paginatorRecursos;

  }

}
