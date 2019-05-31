import { Component, OnInit } from '@angular/core';
import { MovimientoService } from '../../services/movimiento.service';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Inject } from '@angular/core';
import { Movimiento } from '../../model/bienes/movimiento.model';
import { Estado } from '../../model/bienes/estado.model';

export interface Data{
  movimiento :Movimiento;
}
@Component({
  selector: 'app-confirmar-movimiento',
  templateUrl: './confirmar-movimiento.component.html',
  styleUrls: ['./confirmar-movimiento.component.css']
})
export class ConfirmarMovimientoComponent implements OnInit {

  constructor(private _movimientoService: MovimientoService,
              private dialogRef: MatDialogRef<ConfirmarMovimientoComponent>,
                @Inject(MAT_DIALOG_DATA) private data: Data)
  {
   }

  movimiento: Movimiento;
  estados: Estado[];
  estado:Estado;
  comentario: string;
  ngOnInit() {
    this.movimiento = this.data.movimiento;

    this._movimientoService.getAllEstadosViaje()
        .subscribe(
          res => {
            console.log(res);
            this.estados = res;
          },
          error => console.log(error)
        );
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  onSubmit(){
    this._movimientoService.setConfirmacionEnvio(this.movimiento.id, this.comentario)
      .subscribe(
        res => {
          alert("Se registro correctamente la recepcion");
          this.dialogRef.close(this.movimiento);
        },
        error => {
          alert("Ocurrio un error al confimar el movimiento");
          this.dialogRef.close();
        }
      );

  }


}
