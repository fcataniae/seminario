import { Component, OnInit } from '@angular/core';
import { SessionService } from './../services/session.service';
import { Router } from '@angular/router';
import { LoginService } from "../services/login.service";


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(private _loginService: LoginService,
              private _router: Router,
              private _sessionService: SessionService) { }

  ngOnInit() {
    console.log(!this._sessionService.isUserLoggedIn());
    if(!this._sessionService.isUserLoggedIn()){
        this._router.navigate(['/login']);
    }
  }

  logOut(event: Event) {
    event.preventDefault();
    this._sessionService.setLogOut();
    this._router.navigate(['/login']);
  }
}
