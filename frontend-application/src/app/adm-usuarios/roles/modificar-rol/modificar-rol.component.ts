import { Component, Inject, ViewChild  } from '@angular/core';
import { Rol } from '../../../model/abm/rol.model';
import { Permiso } from '../../../model/abm/permiso.model';
import { Router, ActivatedRoute } from '@angular/router';
import { RolService } from './../../../services/rol.service';
import { PermisoService } from './../../../services/permiso.service';
import {forkJoin} from 'rxjs';
import { map } from 'rxjs/operators';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

export interface Data{
  rol : Rol;
}
export class ControlPermiso{
  permiso: Permiso;
  asignado: boolean;

  constructor(permiso: Permiso, asignado: boolean){
    this.permiso = permiso;
    this.asignado = asignado;
  }
}

@Component({
  selector: 'app-modificar-rol',
  templateUrl: './modificar-rol.component.html',
  styleUrls: ['./modificar-rol.component.css']
})
export class ModificarRolComponent  {


  constructor(public dialogRef: MatDialogRef<ModificarRolComponent>,
              private _permisoService: PermisoService,
              @Inject(MAT_DIALOG_DATA) public data: Data)
  {

    this.rol = data.rol;
    this.permisos = [];
    this.permisosSelecionados = [];
    this._permisoService.getAllPermisos().subscribe(
      res => {

        this.rol.permisos.forEach(
          p => this.permisos.push(new ControlPermiso( p, true ))
        );


        let permisos = this.rol.permisos;
        res.filter(function(array){
          return permisos.filter( function(array2){
            return array2.id == array.id;
          }).length == 0
        }).forEach( p => this.permisos.push(new ControlPermiso( p, false )));

        this.permisos.filter(p=> {
          if(p.asignado){
            this.permisosSelecionados.push(p);
          }
        });
        console.log(this.permisosSelecionados);
      },
      error=> {console.log(error)}
    );

}


  rol : Rol;
  permisos: ControlPermiso[];
  permisosSelecionados: ControlPermiso[];

    onSubmit(){
      this.rol.permisos.splice(0,this.rol.permisos.length);
      console.log(this.permisos);
      this.permisos.filter(p => p.asignado).forEach(cp => this.rol.permisos.push(cp.permiso));
      console.log(this.rol);
      this.dialogRef.close(this.rol);


    }
    onCancel(){
      this.dialogRef.close();
    }


}
