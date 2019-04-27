import { Component, OnInit } from '@angular/core';
import { Rol } from './../../../model/rol.model';
import { Router } from '@angular/router';
import { RolService } from './../../../services/rol.service';
import { MatDialogRef } from '@angular/material';

@Component({
  selector: 'app-alta-rol',
  templateUrl: './alta-rol.component.html',
  styleUrls: ['./alta-rol.component.css']
})
export class AltaRolComponent {

  constructor( public dialogRef: MatDialogRef<AltaRolComponent>,
                private _rolService: RolService,
               private _router: Router)
 {
   this.rol = new Rol;
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
