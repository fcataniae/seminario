import { Component, OnInit } from '@angular/core';
import { UserService } from './../services/user.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(private router: Router,
              private userService: UserService) { }

  ngOnInit() {
    console.log("object ");
    console.log( this.userService.getUserLoggedIn());
    if(this.userService.getUserLoggedIn() === null){
        this.router.navigate(['/']);
    }
  }

  logOut(event: Event) {
    event.preventDefault();
    this.userService.setLogOut();
    this.router.navigate(['/']);
  }
}
