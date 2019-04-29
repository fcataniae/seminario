import { Component, Inject} from '@angular/core';
import { Usuario } from '../../../model/usuario.model';
import { Rol } from '../../../model/rol.model';
import { RolService } from './../../../services/rol.service';
import {forkJoin} from 'rxjs';
import { map } from 'rxjs/operators';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

export interface Data{
  usuario : Usuario;
}
export class ControlRol{
  rol: Rol;
  asignado: boolean;

  constructor(rol: Rol, asignado: boolean){
    this.rol = rol;
    this.asignado = asignado;
  }
}

@Component({
  selector: 'app-modificar-usuario',
  templateUrl: './modificar-usuario.component.html',
  styleUrls: ['./modificar-usuario.component.css']
})
export class ModificarUsuarioComponent{

  constructor(public dialogRef: MatDialogRef<ModificarUsuarioComponent>,
              @Inject(MAT_DIALOG_DATA) public data: Data,
              private _rolService: RolService) {
    this.cambiaContrasenia = false;
    this.usuario = data.usuario;
    this.roles = [];
    this.rolesSeleccionados = [];
    this._rolService.getAllRoles().subscribe(
      res => {
        this.usuario.roles.forEach(
          r => this.roles.push(new ControlRol( r, true ))
        );


        let roles = this.usuario.roles;
        res.filter(function(array){
          return roles.filter( function(array2){
            return array2.id == array.id;
          }).length == 0
        }).forEach( r => this.roles.push(new ControlRol( r, false )));

        this.roles.filter(r=> {
          if(r.asignado){
            this.rolesSeleccionados.push(r);
          }
        });
        console.log(this.rolesSeleccionados);
        console.log(this.roles);

      },
      error => {console.log(error)}
    );

    }


  usuario : Usuario;
  passwordCheck: string;
  cambiaContrasenia: boolean;
  roles: ControlRol[];
  rolesSeleccionados: ControlRol[];



  onChangeContrasenia(){
    if(!this.cambiaContrasenia){
      this.usuario.password = "";
      this.passwordCheck = "";
      this.cambiaContrasenia = true;

    }
    console.log(this.cambiaContrasenia);
  }

  onSubmit(){



    this.dialogRef.close(this.usuario);

  }

  onCancel(){
    this.dialogRef.close();
  }


}
