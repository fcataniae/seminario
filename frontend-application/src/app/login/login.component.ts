import { Component, OnInit } from '@angular/core';

import { LoginService} from './../services/login.service';
import { User } from './../model/user.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  isLoggedIn: Boolean = null;
  constructor(private loginService: LoginService) { }

  ngOnInit() {
  }

  logIn(username: string, password: string, event: Event) {
      event.preventDefault();

      this.loginService.login(username, password).subscribe(

        res => {
         console.log(res);
         this.isLoggedIn = true;

        },
        error => {
          console.error(error);
          this.isLoggedIn = false;
        }

      //  () => this.navigate()
      );

    }
}
