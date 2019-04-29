import { Component, OnInit } from '@angular/core';
import { Usuario } from '../../../model/usuario.model';
import { MatDialogRef } from '@angular/material';

@Component({
  selector: 'app-alta-usuario',
  templateUrl: './alta-usuario.component.html',
  styleUrls: ['./alta-usuario.component.css']
})
export class AltaUsuarioComponent {

  constructor(public dialogRef: MatDialogRef<AltaUsuarioComponent>) {
    this.user = new Usuario();
  }

  user: Usuario;
  passwordCheck: string;


  onCancel(): void {
    this.dialogRef.close();
  }

  onSubmit(){
    this.dialogRef.close(this.user);
    
  }

}
