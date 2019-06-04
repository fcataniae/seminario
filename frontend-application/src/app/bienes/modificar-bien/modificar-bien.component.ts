import { Component, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { ItemMovimiento } from '../../model/bienes/itemmovimiento.model';
import { Inject } from '@angular/core';
import { TipoMovimiento } from '../../model/bienes/tipomovimiento.model';

export class Data{
  item: ItemMovimiento;
  tipo: TipoMovimiento;
}
@Component({
  selector: 'app-modificar-bien',
  templateUrl: './modificar-bien.component.html',
  styleUrls: ['./modificar-bien.component.css']
})
export class ModificarBienComponent implements OnInit {

  item: ItemMovimiento;
  tipo: TipoMovimiento;
  ngOnInit(): void {
  }


  constructor(private dialogRef: MatDialogRef<ModificarBienComponent>,
              @Inject(MAT_DIALOG_DATA) private data: Data)
   {
      this.item = data.item;
      this.tipo = data.tipo;
   }

   onCancel(){
     this.dialogRef.close();
   }
   onSubmit(){
     this.dialogRef.close(this.item);
   }
}
