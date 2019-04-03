import { Component, OnInit } from '@angular/core';

import { LoginService} from './../services/login.service';
import { User } from './../model/user.model';
import { Router } from '@angular/router';
import { SessionService } from './../services/session.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  isLoggedIn: Boolean = true;
  constructor(private loginService: LoginService,
              private router: Router,
              private sessionService: SessionService) { }

  ngOnInit() {
  }

  logIn(username: string, password: string, event: Event) {
      event.preventDefault();

      this.loginService.login(username, password).subscribe(

        res => {
         console.log(res);
         this.isLoggedIn = true;
         this.sessionService.setUserLoggedIn(res);
         this.router.navigate(['/home']);
        },
        error => {
          console.error(error);
          this.isLoggedIn = false;
                }

      //  () => this.navigate()
      );

    }
}
