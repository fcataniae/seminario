import { Component } from '@angular/core';
import { Rol } from './../../../model/abm/rol.model';
import { RolService } from './../../../services/rol.service';
import { MatDialogRef } from '@angular/material';

@Component({
  selector: 'app-alta-rol',
  templateUrl: './alta-rol.component.html',
  styleUrls: ['./alta-rol.component.css']
})
export class AltaRolComponent {

  constructor( public dialogRef: MatDialogRef<AltaRolComponent>,
               private _rolService: RolService)
 {
   this.rol = new Rol();
  }

  rol: Rol;



  onCancel(): void {
    this.dialogRef.close();
  }
  onSubmit(): void {
    console.log(this.rol);
    this.dialogRef.close(this.rol);
  }
}
