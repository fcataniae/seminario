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

  loginError: Boolean = false;
  error: string;
  constructor(private _loginService: LoginService,
              private _router: Router,
              private _sessionService: SessionService) { }

  ngOnInit() {

  }

  logIn(username: string, password: string, event: Event) {
      event.preventDefault();
      if(username != "" && password != "" ){
        this._loginService.login(username, password).subscribe(

          res => {
           this.loginError = false;
           this._sessionService.setUserLoggedIn(res);
           this._router.navigate(['/home']);
          },
          error => {
            console.error(error);
            this.error = "Usuario o password incorrectos.";
            this.loginError= true;
          }
        );

      }else{
        this.error = "Debe ingresar los datos de inicio de session.";
        this.loginError= true;
    }

  }
}
