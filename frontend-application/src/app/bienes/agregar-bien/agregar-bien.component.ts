import { Component, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Bien } from '../../model/bienes/bien.model';
import { ItemMovimiento } from '../../model/bienes/itemmovimiento.model';
import { MovimientoService } from '../../services/movimiento.service';
import { TipoMovimiento } from '../../model/bienes/tipomovimiento.model';
import { Inject } from '@angular/core';
import { Estado } from '../../model/bienes/estado.model';
import { forkJoin } from 'rxjs';
import { StockBienEnLocal } from '../../model/bienes/stockbienlocal.model';
import { StockBienLocalService } from '../../services/stockbienlocal.service';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { map } from 'rxjs/operators';
import { IntercambioProv } from '../../model/bienes/intercambioprovedor.model';


export interface Data{
  tipoMovimiento: TipoMovimiento;
  origen: number;
  destino: number;
}
@Component({
  selector: 'app-agregar-bien',
  templateUrl: './agregar-bien.component.html',
  styleUrls: ['./agregar-bien.component.css']
})
export class AgregarBienComponent implements OnInit {

  bienes: Bien[];
  selectedBien: Bien;
  selectedIntercambio: IntercambioProv;
  itemMovimiento: ItemMovimiento;
  tipoMovimiento: TipoMovimiento;
  estados : Estado[];
  origen: number;
  cantidadBI: number;
  stockBienLocal: StockBienEnLocal[];
  rateControl: FormGroup;
  esProveedor: boolean;//De serlo no controlo limite cantidad
  eligioEstado:boolean;
  destino: number;
  intercambios: IntercambioProv[];
  descripcionIntercambio: string;
  constructor(private dialogRef: MatDialogRef<AgregarBienComponent>,
              private _movimientoService: MovimientoService,
              @Inject(MAT_DIALOG_DATA) private data: Data,
              private _stockbienlocalService: StockBienLocalService)
   {
      this.tipoMovimiento = this.data.tipoMovimiento;
      this.origen = this.data.origen;
      this.destino = this.data.destino;
   }

  ngOnInit() {

    let consultaBienesI = this._movimientoService.getIntercambioProveedorByNroP(this.destino);

    let consultaBienes = this._movimientoService.getAllBienes();
    let consultaEstados = this._movimientoService.getAllEstadosBien(this.tipoMovimiento.id);
    this.estados = [];
    this.bienes = [];
    forkJoin(consultaBienes,consultaEstados)
      .pipe(
        map(([res1,res2])=>{
        console.log();
          if(this.tipoMovimiento.tipo === 'ENVIOINTERCAMBIO'){
            consultaBienesI.subscribe(res3=>{
              console.log(res3);
              this.intercambios = res3;
              let bienes = [];
              this.intercambios.forEach(i =>  bienes.push(i.bienIntercambiableEntregado));
              this.bienes = bienes;
            });

          }else {
            this.bienes = res1;
          }
          this.estados = res2;
        })
    ).subscribe();

    this.esUnProveedor();

  }

  onChangeBien(){ //TODO IMPLEMENTAR LA FUNCIONALIDAD
    //cargar los documentos sacados del bien seleccionado
    //cargar datos para completar
    if(this.tipoMovimiento.tipo === 'ENVIOINTERCAMBIO'){
      this.intercambios.forEach(i =>
        {
          if(JSON.stringify(this.selectedBien) === JSON.stringify(i.bienIntercambiableEntregado)){
            this.descripcionIntercambio = 'Intercambio de bienes en relacion ' + i.cantidadEntregada + 'x' + i.cantidadRecibida;
          }
        }
      );
    }
    this.itemMovimiento = new ItemMovimiento();
    this.selectedIntercambio = new IntercambioProv();
    this.itemMovimiento.estadoRecurso = new Estado();
    this.eligioEstado = false;
    this.itemMovimiento.bienIntercambiable = this.selectedBien;
    this.selectedBien.tipoDocumento.forEach(d =>
      this.itemMovimiento.itemMovimientoTipoDoc.push({nroDocumento : '',tipoDocumento:d})
    );
    this.selectedIntercambio = this.intercambios.filter((item) => item.bienIntercambiableEntregado === this.selectedBien)[0];
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

  esUnProveedor(){

    this.esProveedor = this.tipoMovimiento.tipoAgenteOrigen.id == 3;

    if(this.esProveedor){
      this.cantidadBI = 100000;
    }else{
      this.cantidadBI = 0;

      this.stockBienLocal = [];
      this._stockbienlocalService.getStockLocal(this.origen)
      .subscribe(res=>{
          console.log(res);
          this.stockBienLocal = res;
        },
        error => console.log(error)
      );

    }
    this.rateControl = new FormGroup({
      'common': new FormControl("", [Validators.max(this.cantidadBI), Validators.min(0)])
    });
  }

  mostrarCantidad(){

    if(!this.esProveedor){
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

        this.rateControl = new FormGroup({
          'common': new FormControl("", [Validators.max(this.cantidadBI), Validators.min(0)]),
          'intercambio': new FormControl("", [Validators.max(this.cantidadBI), Validators.min(this.selectedIntercambio.cantidadEntregada)])
        });
      }
    }
  }

  onChangeEstado(){
    this.eligioEstado = true;
    this.mostrarCantidad();
  }

  get commonValidator() { return this.rateControl.get('common'); }

  get intercambioValidator() { return this.rateControl.get('intercambio'); }

}
