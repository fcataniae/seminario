import { Component, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Bien } from '../../model/bienes/bien.model';
import { ItemMovimiento } from '../../model/bienes/itemmovimiento.model';
import { Vale } from '../../model/bienes/vale.model';
import { MovimientoService } from '../../services/movimiento.service';
import { TipoMovimiento } from '../../model/bienes/tipomovimiento.model';
import { Inject } from '@angular/core';
import { Estado } from '../../model/bienes/estado.model';
import { forkJoin } from 'rxjs';
import { map } from 'rxjs/operators';
import { Agente } from '../../model/bienes/agente.model';
import { StockBienEnLocal } from '../../model/bienes/stockbienlocal.model';
import { StockBienLocalService } from '../../services/stockbienlocal.service';
import { FormControl, Validators } from '@angular/forms';


export interface Data{
  tipoMovimiento: TipoMovimiento;
  origen: number;
  }
@Component({
  selector: 'app-agregar-bien',
  templateUrl: './agregar-bien.component.html',
  styleUrls: ['./agregar-bien.component.css']
})
export class AgregarBienComponent implements OnInit {

  bienes: Bien[];
  selectedBien: Bien;
  itemMovimiento: ItemMovimiento;
  tipoMovimiento: TipoMovimiento;
  estados : Estado[];
  origen: number;
  locales: Agente[];
  cantidadBI: number;
  stockBienLocal: StockBienEnLocal[];
  rateControl: FormControl;

  constructor(private dialogRef: MatDialogRef<AgregarBienComponent>,
              private _movimientoService: MovimientoService,
              @Inject(MAT_DIALOG_DATA) private data: Data,
              private _stockbienlocalService: StockBienLocalService)
   {
      this.tipoMovimiento = data.tipoMovimiento;
      this.origen = data.origen;
   }

  ngOnInit() {

    this.stockBienLocal = [];
    this._stockbienlocalService.getStockLocal(this.origen)
    .subscribe(res=>{
        console.log(res);
        this.stockBienLocal = res;
      },
      error => console.log(error)
    );

    this.cantidadBI = 0;
    let consultaBienes = this._movimientoService.getAllBienes();
    let consultaEstados = this._movimientoService.getAllEstadosBien();
    let consultaAgentes = this._movimientoService.getAllAgentes();
    this.estados = [];
    this.bienes = [];
    this.locales = [];
    forkJoin(consultaBienes,consultaEstados,consultaAgentes)
      .subscribe(results=>{
        console.log(results);
        this.bienes = results[0];
        this.estados = results[1];
        this.locales = results[2].filter( a => a.tipoAgente.id !== 3);
    },
      error => console.log(error)
    );

    this.rateControl = new FormControl("", [Validators.max(this.cantidadBI), Validators.min(0)])

  }

  onChangeBien(){ //TODO IMPLEMENTAR LA FUNCIONALIDAD
    //cargar los documentos sacados del bien seleccionado
    //cargar datos para completar
    this.itemMovimiento = new ItemMovimiento();
    this.itemMovimiento.estadoRecurso = new Estado();
    let estado = this.estados.find(c => c.id == 1);
    this.itemMovimiento.estadoRecurso = estado;
    this.itemMovimiento.bienIntercambiable = this.selectedBien;
    this.selectedBien.tipoDocumento.forEach(d =>
      this.itemMovimiento.itemMovimientoTipoDoc.push({nroDocumento : '',tipoDocumento:d})
    );
    this.mostrarCantidad();
  }
  onCancel(): void {
    this.dialogRef.close();
  }

  onSubmit(){
    if(this.cantidadBI >= this.itemMovimiento.cantidad){
      this.dialogRef.close(this.itemMovimiento);
    }
  }

  mostrarCantidad(){

    let stockBienElegido = this.stockBienLocal.filter
      (stockbien => stockbien.idBI === this.itemMovimiento.bienIntercambiable.id)[0];

    if(stockBienElegido){
      if(this.itemMovimiento.estadoRecurso.descrip == "LIBRE"){
        this.cantidadBI = stockBienElegido.stock_libre;
      }else if(this.itemMovimiento.estadoRecurso.descrip == "OCUPADO"){
        this.cantidadBI = stockBienElegido.stock_ocupado;
      }else if(this.itemMovimiento.estadoRecurso.descrip == "RESERVADO"){
        this.cantidadBI = stockBienElegido.stock_reservado;
      }else if(this.itemMovimiento.estadoRecurso.descrip == "DESTRUIDO"){
        this.cantidadBI = stockBienElegido.stock_destruido;
      }

      this.rateControl = new FormControl("", [Validators.max(this.cantidadBI), Validators.min(0)])

    }
  }

}
