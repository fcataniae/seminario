import { Component, OnInit } from '@angular/core';
import { Usuario } from '../../../model/usuario.model';
import { UsuarioService } from './../../../services/usuario.service';

@Component({
  selector: 'app-alta-usuario',
  templateUrl: './alta-usuario.component.html',
  styleUrls: ['./alta-usuario.component.css']
})
export class AltaUsuarioComponent implements OnInit {

  constructor(private _usuarioService : UsuarioService) { }

  user: Usuario;
  passwordCheck: string;

  ngOnInit() {
    this.user = new Usuario();
  }

  onSubmit(){
  }

}
