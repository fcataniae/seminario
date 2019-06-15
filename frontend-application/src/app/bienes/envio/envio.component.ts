import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource, MatPaginator, MatSort, MatDialog } from '@angular/material';
import { Location } from '@angular/common';
import { AgregarBienComponent } from '../agregar-bien/agregar-bien.component';
import { AgregarRecursoComponent } from '../agregar-recurso/agregar-recurso.component';
import { ModificarBienComponent } from '../modificar-bien/modificar-bien.component';
import { ItemMovimiento } from '../../model/bienes/itemmovimiento.model';
import { Recurso } from '../../model/bienes/recurso.model';
import { Movimiento } from '../../model/bienes/movimiento.model';
import { ActivatedRoute } from '@angular/router';
import { MovimientoService } from '../../services/movimiento.service';
import {  Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ConfirmacionPopupComponent } from '../../adm-usuarios/confirmacion-popup/confirmacion-popup.component';
import { FormControl } from '@angular/forms';
import { Transportista } from '../../model/bienes/transportista.model';
import { startWith } from 'rxjs/internal/operators/startWith';

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

  transpForm = new FormControl();
  transpFilter = new Observable<Transportista[]>();
  transportistas: Transportista[];
  selectedTransp: string;

  columnsRecepcionBien = ['bien','tipoDoc','nroDoc','cantidad','estado','modificar','eliminar'];
  columnsRecepcionGenerico = ['bien','cantidad','estado','modificar','eliminar'];
  columnsRecursoEnvio = ['tipoRecurso','idRecurso','modificar','eliminar'];


  columnsToDisplayBien: String[];

  movimiento: Movimiento = new Movimiento();
  modificacion: boolean = false;
  constructor(private location: Location,
              private _dialog: MatDialog,
              private route: ActivatedRoute,
              private _movimientoService: MovimientoService,
              )
  {
  }

  ngOnInit() {

    this._movimientoService.getAllTransportistas().subscribe(
        res =>{
          console.log(res);
          this.transportistas = res;

          this.modificacion = window.location.href.includes("modificar");
          this.movimiento = JSON.parse(atob(this.route.snapshot.paramMap.get('mov')));

          if(this.modificacion){
            this.transportistas.forEach(t => {
              if(t.id === this.movimiento.idTransportista){
                this.selectedTransp = t.nombre;
              }
            })
          }
          this.transpFilter = this.transpForm.valueChanges
          .pipe(
            startWith(this.modificacion? this.selectedTransp : ''),
            map(value => this.filterTransp(value))
          );


          this.datosTablaBienes.data = this.movimiento.itemMovimientos;
          this.datosTablaBienes.sort = this.sortBienes;
          this.datosTablaBienes.paginator = this.paginatorBienes;

          if(!this.modificacion){
            this.movimiento.fechaSalida = new Date();
            this.movimiento.fechaAlta = new Date();
          }
          this.datosTablaRecursos.data = this.movimiento.recursosAsignados;
          this.datosTablaRecursos.sort = this.sortRecursos;
          this.datosTablaRecursos.paginator = this.paginatorRecursos;

          this.movimiento.tipoMovimiento.tipoDocumentos.forEach( d => this.movimiento.tipoDocumento = d);

          if(this.movimiento.tipoMovimiento.tipo === 'RECEPCION' && this.movimiento.tipoMovimiento.tipoAgenteOrigen.nombre === 'PROVEEDOR'){
            this.columnsToDisplayBien = this.columnsRecepcionBien;
          }else {
            this.columnsToDisplayBien = this.columnsRecepcionGenerico;
          }
        }
    )
  }//END OnInit

  filterTransp(value : string): Transportista[] {
    return this.transportistas.filter(t => t.nombre.toLocaleLowerCase().includes(value.toLocaleLowerCase()) );
  }

  goBack(): void {
    let dialog = this._dialog.open(ConfirmacionPopupComponent,{
      data: {mensaje:"Desea volver atras?", titulo: "Confirmar accion", error: false},
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
      data: { tipoMovimiento: this.movimiento.tipoMovimiento,
              origen: this.movimiento.origen,
              destino: this.movimiento.destino}
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
    let observer = this.modificacion ? this._movimientoService.setModificacionMovimiento(this.movimiento) : this._movimientoService.setRegistroMovimiento(this.movimiento);
    observer.subscribe(
      res =>{
        this.location.back();
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

  setDataSource(indexNumber: number) {
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

  updateBien(itemMov: ItemMovimiento){
    const dialogRef = this._dialog.open(ModificarBienComponent,{
      width: '50%',
      data: { tipo: this.movimiento.tipoMovimiento, item: itemMov }
    });
    let index = this.movimiento.itemMovimientos.indexOf(itemMov);
    dialogRef.afterClosed().subscribe(
      res=> {
        console.log(res instanceof ItemMovimiento);
        if(res instanceof ItemMovimiento){

          this.movimiento.itemMovimientos[index] = res;
          console.log(this.movimiento.itemMovimientos);
          this.datosTablaBienes.data = this.movimiento.itemMovimientos;
        }
      }
    );
  }

  refreshT(){
    this.transportistas.forEach(t =>
      {
        if(t.nombre === this.selectedTransp)
          this.movimiento.idTransportista = t.id;
      }
    );
  }
}
