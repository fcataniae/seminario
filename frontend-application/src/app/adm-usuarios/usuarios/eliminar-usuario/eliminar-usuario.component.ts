import { Component, OnInit } from '@angular/core';
import { User } from './../../../model/user.model';
import { UsuarioService } from './../../../services/usuario.service';

@Component({
  selector: 'app-eliminar-usuario',
  templateUrl: './eliminar-usuario.component.html',
  styleUrls: ['./eliminar-usuario.component.css']
})
export class EliminarUsuarioComponent implements OnInit {

  constructor(private _usuarioService: UsuarioService) { }

  usuarios: User[];

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



}
