import { Component, OnInit } from '@angular/core';
import { MovimientoService } from '../../services/movimiento.service';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Inject } from '@angular/core';
import { Estado } from '../../model/bienes/estado.model';

export interface Data{
  nro: number;
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

  nro: number;
  estados: Estado[];
  estado:Estado;
  comentario: string;
  ngOnInit() {
    this.nro = this.data.nro;
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
    this._movimientoService.setConfirmacionEnvio(this.nro, this.comentario,this.estado.descrip)
      .subscribe(
        res => {
          this.dialogRef.close(true);
        },
        error => {
          this.dialogRef.close(false);
        }
      );

  }


}
