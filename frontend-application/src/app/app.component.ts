import { Component } from '@angular/core';
import { SessionService } from './services/session.service';
import { Router } from '@angular/router';
import { NavigationStart } from '@angular/router';
import { NavigationEnd } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  username : string = '';
  loggedIn = false;

  permisoInformeTablaDeudas = false;
  permisoInformeTablaStock = false;
  permisoInforme = true;
  permisoInformeTiendas = true;
  permisoMovimiento = false;
  permisoRol = false;
  permisoUsuario = false;
  permisoPersona = false;

  constructor (private _sessionService: SessionService,
               private _router: Router){
    this._router.events.subscribe( e => {
      if(e instanceof NavigationEnd)
        if(!this._router.url.includes('login') && this._router.url !== '/')
          if(!this._sessionService.isUserLoggedIn()){
            this._router.navigate(['login']);
          }
       if(e instanceof NavigationStart)
         if(!this._sessionService.isUserLoggedIn()){
           this.username = '';
           this.loggedIn = false;
         }else{
           let token = this._sessionService.getUserLoggedIn();

           this.username = token.username;
           console.log(token);
           this.loggedIn = true;
           token.permisos.forEach( p => {
             if(p.nombre.includes('MODI-ROL')){
               this.permisoRol = true;
             }
             if(p.nombre.includes('MODI-PERSONA')){
               this.permisoPersona = true;
             }
             if(p.nombre.includes('MODI-USUARIO')){
               this.permisoUsuario = true;
             }
             if(p.nombre.includes('MODI-MOVIMIENTO')){
               this.permisoMovimiento = true;
             }
             if(p.nombre.includes('CONS-LISTADO-STOCK')){
               this.permisoInformeTablaStock = true;
             }
             if(p.nombre.includes('CONS-LISTADO-DEUDA')){
               this.permisoInformeTablaDeudas = true;
             }
             this.permisoInforme = true;
             this.permisoInformeTiendas = true;
           });
         }
    });
  }
  logOut(event: Event) {
    event.preventDefault();
    this.permisoInformeTablaDeudas = false;
    this.permisoInformeTablaStock = false;
    this.permisoInforme = true;
    this.permisoInformeTiendas = true;
    this.permisoMovimiento = false;
    this.permisoRol = false;
    this.permisoUsuario = false;
    this.permisoPersona = false;
    this._sessionService.setLogOut();
    this._router.navigate(['/login']);
  }

}
