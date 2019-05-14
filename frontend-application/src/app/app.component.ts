import { Component } from '@angular/core';
import { SessionService } from './services/session.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  username : string = '';

  constructor (private _sessionService: SessionService,
               private _router: Router){
    this.username = "";
    _router.events.subscribe( e => {
      if(_sessionService.isUserLoggedIn){
        this.username = _sessionService.getUserLoggedIn().username;
      }else{
        this.username = "";
      }
    });
  }

}
