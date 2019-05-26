import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { DevolucionConfirmada } from '../../model/bienes/devolucionconfirmada.model';
import { ItemDevolucionConfirmada } from '../../model/bienes/itemdevolucionconfirmada.model';
import { MatTableDataSource, MatPaginator, MatSort, MatDialog, MatDialogRef } from '@angular/material';

@Component({
  selector: 'app-confirmar-devolucion',
  templateUrl: './confirmar-devolucion.component.html',
  styleUrls: ['./confirmar-devolucion.component.css']
})
export class ConfirmarDevolucionComponent implements OnInit {

  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  datosTabla = new MatTableDataSource<ItemDevolucionConfirmada>();

  columnsToDisplay = ['tipoDoc', 'idDoc', 'estado', 'fecha', 'eliminar'];

  itemDevolucion: ItemDevolucionConfirmada;
  devolucion: DevolucionConfirmada;

  constructor(private _router : Router,
              private dialogAgregar: MatDialog
              ) { }

  ngOnInit() {
    this.devolucion = new DevolucionConfirmada();
    this.itemDevolucion = new ItemDevolucionConfirmada();

    this.datosTabla.data = this.devolucion.items;
    this.datosTabla.sort = this.sort;
    this.datosTabla.paginator = this.paginator;
  }

  redirectToHome(){
    this._router.navigate(['home']);
  }

  onAgregarConfirmacion() {
    this.itemDevolucion.posicion = this.devolucion.items.length;
    this.devolucion.items.push(this.itemDevolucion);
    console.log(this.devolucion.items);
    this.datosTabla.data = this.devolucion.items;
    //this.itemDevolucion = new ItemDevolucionConfirmada();
  }

/*BORRAR*/
  estados: string[] = ['Devuelto','Cancelado'];
}
