import { Component, OnInit, Inject } from '@angular/core';
import { Movimiento } from '../../model/bienes/movimiento.model';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

export interface Data{
  movimiento: Movimiento;
}
@Component({
  selector: 'app-vista-movimiento',
  templateUrl: './vista-movimiento.component.html',
  styleUrls: ['./vista-movimiento.component.css']
})
export class VistaMovimientoComponent implements OnInit {

  movimiento: Movimiento;
  constructor(private dialogRef: MatDialogRef<VistaMovimientoComponent>,
              @Inject(MAT_DIALOG_DATA) private data: Data)
  {
      this.movimiento = this.data.movimiento;
  }

  ngOnInit() {
  }
  submit(){
    this.dialogRef.close()
  }

}
