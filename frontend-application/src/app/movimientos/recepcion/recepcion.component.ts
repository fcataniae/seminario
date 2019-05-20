import { Component, OnInit, ViewChild } from '@angular/core';
import { Location } from '@angular/common';
import { MatTableDataSource, MatPaginator, MatSort } from '@angular/material';
import { RecursoAsignado } from '../../model/recursoAsignado';

@Component({
  selector: 'app-recepcion',
  templateUrl: './recepcion.component.html',
  styleUrls: ['./recepcion.component.css']
})
export class RecepcionComponent implements OnInit {

  tipoDocumentos: String[] = ['Remito','Factura','Recibo'];
  tipoRecursos: String[] = ['Termógrafo'];
  IDRecursos: String[] = ['1','2','3'];

  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  datosTablaRecursos: RecursoAsignado[] = [ {posicion: 0, tipoRecurso: 'Termografo', idRecurso: 0} ];;
  columnsToDisplay = ['No.', 'Tipo Recurso', 'ID Recurso'];

  tipoBienes: String[] = ['Termógrafo','Pallet','Cajón','Envase'];
  bienes: String[] = ['ARLOG','CHEP','Descartable','IFCO'];

  tipoDocBienes: String[] = ['Remito IFCO'];

  onAgregarRecurso(){

  }

  registrar(){

  }

  goBack(): void {
    this.location.back();
  }

  constructor(private location: Location) { }

  ngOnInit() {
  }

}
