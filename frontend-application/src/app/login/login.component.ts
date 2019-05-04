import { Component, OnInit } from '@angular/core';
import { Token }  from '../model/token.model';
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
  error: string;
  constructor(private _loginService: LoginService,
              private _router: Router,
              private _sessionService: SessionService) { }

  ngOnInit() {
    console.log(this._sessionService.isUserLoggedIn());
    if(this._sessionService.isUserLoggedIn()){
        this._router.navigate(['/home']);
    }
  }

  logIn(username: string, password: string, event: Event) {
      event.preventDefault();
      if(username != "" && password != "" ){
        this._loginService.login(username, password).subscribe(

          res => {
           console.log(res);
           this.isLoggedIn = true;
           this._sessionService.setUserLoggedIn(res);
           this._router.navigate(['/home']);
          },
          error => {
            console.error(error);
            this.error = "Usuario o password incorrectos.";
            this.isLoggedIn = false;
          }
        );

      }else{
        this.error = "Debe ingresar los datos solicitados.";
        this.isLoggedIn = false;
    }

  }
}
