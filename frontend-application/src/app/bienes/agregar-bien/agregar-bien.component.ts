import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material';
import { Bien } from '../../model/bienes/bien.model';
import { Vale } from '../../model/bienes/vale.model';

@Component({
  selector: 'app-agregar-bien',
  templateUrl: './agregar-bien.component.html',
  styleUrls: ['./agregar-bien.component.css']
})
export class AgregarBienComponent implements OnInit {

  bien: Bien;

  constructor(public dialogRef: MatDialogRef<AgregarBienComponent>) {
    this.bien = new Bien();
   }

  ngOnInit() {
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  onSubmit(){
    this.dialogRef.close(this.bien);
  }

  /*BORRAR*/
  tipoDocumentos: String[] = ['Remito','Factura','Recibo'];
  tipoRecursos: String[] = ['Termógrafo'];
  IDRecursos: String[] = ['1','2','3'];

  tipoBienes: String[] = ['Termógrafo','Pallet','Cajón','Envase'];
  bienes: String[] = ['ARLOG','CHEP','Descartable','IFCO'];

  tipoDocBienes: String[] = ['Remito IFCO'];

  isRecepcion: boolean = false;
  isDevolucion: boolean = true;

  vales: Vale[] = [{nro: 1, cantidad: 50, pendiente: true},{nro: 2, cantidad: 20, pendiente: true}];
}
