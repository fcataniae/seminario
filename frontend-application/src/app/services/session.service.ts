import { Injectable } from '@angular/core';
import { Token } from '../model/token.model'

@Injectable({
  providedIn: 'root'
})
export class SessionService {



  constructor() {
  }

  isUserLoggedIn(): boolean{
    let logedin = sessionStorage.getItem('logedIn');
    return logedin === 'true';
  }
  setUserLoggedIn(token: Token) {
    sessionStorage.setItem('logedIn','true');
    sessionStorage.setItem('currentUser', JSON.stringify(token));
    sessionStorage.setItem('Auth', 'Bearer ' + token.token);

  }

  getUserLoggedIn(): Token {
  	return JSON.parse(sessionStorage.getItem('currentUser'));
  }

  setLogOut() {
    sessionStorage.setItem('logedIn','false');
    sessionStorage.setItem('currentUser', '');
  }
}
