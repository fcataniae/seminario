import { Component, OnInit } from '@angular/core';
import { SessionService } from './../services/session.service';
import { Router } from '@angular/router';
import { LoginService } from "../services/login.service";

import { Permiso }  from '../model/abm/permiso.model';
import { PermisoService } from './../services/permiso.service';
import { Token } from '../model/token.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(private _loginService: LoginService,
              private _router: Router,
              private _sessionService: SessionService,
              private _permisoService: PermisoService) { }

  ngOnInit() {

    this.permisoMovimiento = false;
    this.permisoRol = false;
    this.permisoUsuario = false;
    this.permisoPersona = false;

    console.log(!this._sessionService.isUserLoggedIn());
    if(!this._sessionService.isUserLoggedIn()){
        this._router.navigate(['/login']);
    }else{
      let token = this._sessionService.getUserLoggedIn();
      console.log(token);

      token.permisos.forEach( p => {
        if(p.nombre.includes('ROL')){
          this.permisoRol = true;
        }else if(p.nombre.includes('PERSONA')){
          this.permisoPersona = true;
        }else if(p.nombre.includes('USUARIO')){
          this.permisoUsuario = true;
        }else if(p.nombre.includes('MOVIMIENTO')){
          this.permisoMovimiento = true;
        }
      });
  }
}
permisoMovimiento: boolean;
permisoRol: boolean;
permisoUsuario: boolean;
permisoPersona: boolean;
username: string;
permisos: Permiso[];

  logOut(event: Event) {
    event.preventDefault();
    this._sessionService.setLogOut();
    this._router.navigate(['/login']);
  }
}
