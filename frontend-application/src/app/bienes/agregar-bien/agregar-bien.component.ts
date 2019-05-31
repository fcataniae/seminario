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


export interface Data{
  tipoMovimiento: TipoMovimiento;
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


  constructor(private dialogRef: MatDialogRef<AgregarBienComponent>,
              private _movimientoService: MovimientoService,
              @Inject(MAT_DIALOG_DATA) private data: Data)
   {
      this.tipoMovimiento = data.tipoMovimiento;
   }

  ngOnInit() {
    let consultaBienes = this._movimientoService.getAllBienes();
    let consultaEstados = this._movimientoService.getAllEstadosBien();
    this.estados = [];
    this.bienes = [];
    forkJoin(consultaBienes,consultaEstados)
      .subscribe(results=>{
        console.log(results);
        this.bienes = results[0];
        this.estados = results[1];
      },
      error => console.log(error)
    );
  }

  onChangeBien(){ //TODO IMPLEMENTAR LA FUNCIONALIDAD
    //cargar los documentos sacados del bien seleccionado
    //cargar datos para completar
    this.itemMovimiento = new ItemMovimiento();
    this.itemMovimiento.estadoRecurso = new Estado(); //Default (sino es null y aparece vacio)
    this.itemMovimiento.bienIntercambiable = this.selectedBien;
    this.selectedBien.tipoDocumento.forEach(d =>
      this.itemMovimiento.itemMovimientoTipoDoc.push({nroDocumento : '',tipoDocumento:d})
    );

  }
  onCancel(): void {
    this.dialogRef.close();
  }

  onSubmit(){
    this.dialogRef.close(this.itemMovimiento);
  }
}
