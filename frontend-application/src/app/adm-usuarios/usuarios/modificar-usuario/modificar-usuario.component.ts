import { Component, OnInit, Input } from '@angular/core';
import { Usuario } from '../../../model/usuario.model';
import { Rol } from '../../../model/rol.model';
import { Router,ActivatedRoute } from '@angular/router';
import { UsuarioService } from './../../../services/usuario.service';
import { RolService } from './../../../services/rol.service';
import {forkJoin} from 'rxjs';
import { map,pipe } from 'rxjs/operators';

@Component({
  selector: 'app-modificar-usuario',
  templateUrl: './modificar-usuario.component.html',
  styleUrls: ['./modificar-usuario.component.css']
})
export class ModificarUsuarioComponent implements OnInit {

  constructor(private _router: Router,
              private _route: ActivatedRoute;
              private _usuarioService: UsuarioService,
              private _rolService: RolService) {
    }

  usuario : Usuario;
  passwordCheck: string;
  cambiaContrasenia: boolean;

  asignarRoles: boolean;
  rolesNoAsignados: Rol[];
  rolesAsignados: Rol[];


  ngOnInit() {
    this.cambiaContrasenia = false;
    this.asignarRoles = false;
    this.usuario = new Usuario();
    this._route.paramMap.subscribe(params => {
        let id = params.get("id");
        const observable1 =this._usuarioService.getUsuarioByName(id).pipe(map(res => res));
        const observable2 = this._rolService.getAllRoles().pipe(map(res => res));
        forkJoin(observable1,
                 observable2)
          .subscribe(
          ([res1,res2])=>{
            console.log(res1);
            this.usuario = res1;
            this.passwordCheck = this.usuario.password;
            this.rolesAsignados = this.usuario.roles;
            let roles = this.usuario.roles;
            this.rolesNoAsignados = res2.filter(function(array){
              return roles.filter( function(array2){
                return array2.id == array.id;
              }).length == 0
            });
            console.log(this.rolesNoAsignados);
          },
          error => {
            console.log(error);
          }
        );
    },
    error => {console.log(error);}
  }

  onChangeContrasenia(){
    if(!this.cambiaContrasenia){
      this.usuario.password = "";
      this.passwordCheck = "";
      this.cambiaContrasenia = true;

    }
    console.log(this.cambiaContrasenia);
  }

  onSubmit(){
    if (this.asignarRoles){
      console.log("asigno roles");
      this.usuario.roles = this.rolesAsignados;
    }
    this._usuarioService.updateUsuario(this.usuario).subscribe(
      res => {
        alert("usuario modificado correctamente!");
        this._router.navigate(['/home/gestion/usuarios']);
      },
      error => {alert(error);}
    );

  }

}
