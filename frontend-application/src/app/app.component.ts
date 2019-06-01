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
  loggedIn: boolean = false;

  permisoMovimiento: boolean = false;
  permisoRol: boolean = false;
  permisoUsuario: boolean = false;
  permisoPersona: boolean = false;

  constructor (private _sessionService: SessionService,
               private _router: Router){
    this._router.events.subscribe( e => {
      if(e instanceof NavigationEnd)
        if(!this._router.url.includes('login') && this._router.url !== '/')
          if(!this._sessionService.isUserLoggedIn())
            this._router.navigate(['login']);

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
    });
  }
  logOut(event: Event) {
    event.preventDefault();
    this._sessionService.setLogOut();
    this._router.navigate(['/login']);
  }

}
