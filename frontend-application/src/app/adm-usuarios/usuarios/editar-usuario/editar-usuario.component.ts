import { Component, OnInit } from '@angular/core';
import { Usuario } from '../../../model/usuario.model';
import { UsuarioService } from './../../../services/usuario.service';

@Component({
  selector: 'app-editar-usuario',
  templateUrl: './editar-usuario.component.html',
  styleUrls: ['./editar-usuario.component.css']
})
export class EditarUsuarioComponent implements OnInit {

  constructor(private _usuarioService: UsuarioService) { }

  usuarios: Usuario[];
  usuarioSelected: Usuario = null;
  ngOnInit( ) {

      //cargo todos los usuarios para mostrar un listado
      this._usuarioService.getAllUsuarios().subscribe(
        res => {
          this.usuarios = res;
        },
        error => {
          console.log(error);
        }
      );

  }

  deleteUser(usuario: Usuario){
    this._usuarioService.deleteUser(usuario).subscribe(
      res => {console.log(res)},
      error => {console.log(error)}
    );
  }

  modificarUser(usuario: Usuario){
    console.log(usuario);
    this.usuarioSelected = usuario;
  }

}
