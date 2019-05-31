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

    this.permisoRol = false;
    this.permisoUsuario = false;
    this.permisoPersona = false;

    console.log(!this._sessionService.isUserLoggedIn());
    if(!this._sessionService.isUserLoggedIn()){
        this._router.navigate(['/login']);
    }else{
      this.token = this._sessionService.getUserLoggedIn();
      this.username = this.token.username;

      this._permisoService.getUserAllPermisos(this.username, this.token.token)
      .subscribe(
             res => {
               console.log(res);
               this.permisos = res;

               for(let i=0; i<this.permisos.length; i++){

                if (this.permisos[i].nombre.includes('ROL')) {
                  this.permisoRol = true;
                }
                if (this.permisos[i].nombre.includes('USUARIO')) {
                  this.permisoUsuario = true;
                }
                if (this.permisos[i].nombre.includes('PERSONA')) {
                  this.permisoPersona = true;
                }
              }

             },
             error => console.log(error)
       );
    }
  }

permisoRol: boolean;
permisoUsuario: boolean;
permisoPersona: boolean;
token: Token;
username: string;
permisos: Permiso[];

  logOut(event: Event) {
    event.preventDefault();
    this._sessionService.setLogOut();
    this._router.navigate(['/login']);
  }
}
