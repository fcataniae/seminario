import { Component, OnInit } from '@angular/core';

import { LoginService} from './../services/login.service';
import { Router } from '@angular/router';
import { SessionService } from './../services/session.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  isLoggedIn: Boolean = true;
  constructor(private _loginService: LoginService,
              private _router: Router,
              private _sessionService: SessionService) { }

  ngOnInit() {
  }

  logIn(username: string, password: string, event: Event) {
      event.preventDefault();

      this._loginService.login(username, password).subscribe(

        res => {
         console.log(res);
         this.isLoggedIn = true;
         this._sessionService.setUserLoggedIn(res);
         this._router.navigate(['/home']);
        },
        error => {
          console.error(error);
          this.isLoggedIn = false;
                }

      //  () => this.navigate()
      );

    }
}
