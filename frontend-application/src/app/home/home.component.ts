import { Component, OnInit } from '@angular/core';
import { UserService } from './../services/user.service';
import { Router } from '@angular/router';
import {LoginService} from "../services/login.service";


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(private loginService: LoginService,
              private router: Router,
              private userService: UserService) { }

  ngOnInit() {
    if(!this.userService.isUserLoggedIn()){
        this.router.navigate(['/']);
    }
  }

  logOut(event: Event) {
    event.preventDefault();
    this.userService.setLogOut();
    this.router.navigate(['/']);
  }
}
