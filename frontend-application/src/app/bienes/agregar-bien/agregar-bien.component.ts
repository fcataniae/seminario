import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material';
import { Bien } from '../../model/bienes/bien.model';
import { ItemMovimiento } from '../../model/bienes/itemmovimiento.model';
import { Vale } from '../../model/bienes/vale.model';
import { MovimientoService } from '../../services/movimiento.service';

@Component({
  selector: 'app-agregar-bien',
  templateUrl: './agregar-bien.component.html',
  styleUrls: ['./agregar-bien.component.css']
})
export class AgregarBienComponent implements OnInit {

  bienes: Bien[];
  selectedBien: Bien;
  itemMovimiento: ItemMovimiento;


  constructor(private dialogRef: MatDialogRef<AgregarBienComponent>,
              private _movimientoService: MovimientoService) {

   }

  ngOnInit() {
    this._movimientoService.getAllBienes().subscribe(
      res => {
        console.log(res);
        this.bienes = res;
      },
      error => console.log(error)
    );
  }

  onChangeBien(){ //TODO IMPLEMENTAR LA FUNCIONALIDAD
    //cargar los documentos sacados del bien seleccionado
    //cargar datos para completar
    this.itemMovimiento = new ItemMovimiento();
    this.itemMovimiento.vacio = false //Default (sino es null y aparece vacio)
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
