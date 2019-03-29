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
    if(!this.userService.isUserLogged()){
        this.router.navigate(['/']);
    }
  }

}
