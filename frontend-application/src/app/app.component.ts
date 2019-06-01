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
         }else{
           this.username = this._sessionService.getUserLoggedIn().username;
         }
    });
  }

}
