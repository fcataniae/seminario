import { Component, OnInit, Input } from '@angular/core';
import { Usuario } from '../../../model/usuario.model';
import { ActivatedRoute } from '@angular/router';
import { UsuarioService } from './../../../services/usuario.service';

@Component({
  selector: 'app-modificar-usuario',
  templateUrl: './modificar-usuario.component.html',
  styleUrls: ['./modificar-usuario.component.css']
})
export class ModificarUsuarioComponent implements OnInit {

  constructor(private _route: ActivatedRoute,
              private _usuarioService: UsuarioService) {
    }

  usuario : Usuario;
  passwordCheck: string;

  ngOnInit() {
    this._route.paramMap.subscribe(params => {
      let id = params.get("id");
      this._usuarioService.getUsuarioByName(id).subscribe(
        res => {
          this.usuario = res;
          this.usuario.password = "";
        },
        error => {console.log(error)}
      )
    })
  }


}
