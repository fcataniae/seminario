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

  constructor(private loginService: LoginService,
              private router: Router,
              private sessionService: SessionService) { }

  ngOnInit() {
    if(this.sessionService.isUserLoggedIn()){
        this.router.navigate(['/']);
    }
  }

  logOut(event: Event) {
    event.preventDefault();
    this.sessionService.setLogOut();
    this.router.navigate(['/']);
  }
}
