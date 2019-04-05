import { Component, OnInit, Input } from '@angular/core';
import { Usuario } from '../../../model/usuario.model';
import { Rol } from '../../../model/rol.model';
import { ActivatedRoute } from '@angular/router';
import { UsuarioService } from './../../../services/usuario.service';
import { RolService } from './../../../services/rol.service';

@Component({
  selector: 'app-modificar-usuario',
  templateUrl: './modificar-usuario.component.html',
  styleUrls: ['./modificar-usuario.component.css']
})
export class ModificarUsuarioComponent implements OnInit {

  constructor(private _route: ActivatedRoute,
              private _usuarioService: UsuarioService,
              private _rolService: RolService) {
    }

  usuario : Usuario;
  passwordCheck: string;
  cambiaContrasenia: boolean;
  rolesNoAsignados: Rol[];

  ngOnInit() {
    this.cambiaContrasenia = false;
    this.usuario = new Usuario();
    this._route.paramMap.subscribe(params => {
      let id = params.get("id");
      this._usuarioService.getUsuarioByName(id).subscribe(
        res => {
          this.usuario = res;
          this.passwordCheck = this.usuario.password;
          this.cargarRoles();
        },
        error => {console.log(error);}
      )
    });
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
    this._usuarioService.updateUsuario(this.usuario).subscribe(
      res => { alert(res);},
      error => {alert(error);}
    );

  }
  cargarRoles(){

    this._rolService.getAllRoles().subscribe(

      res => {
          let roles = this.usuario.roles;
          this.rolesNoAsignados = res.filter(function(array){
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
  }
}
