import { Injectable } from '@angular/core';
import { Token } from '../model/token.model'

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  private userLoggedIn;

  constructor() {
  	this.userLoggedIn = false;
  }

  isUserLoggedIn(){
    return this.userLoggedIn;
  }
  setUserLoggedIn(token: Token) {
    this.userLoggedIn = true;
    sessionStorage.setItem('currentUser', JSON.stringify(token));
    sessionStorage.setItem('Auth', 'Bearer ' + token.token);

  }

  getUserLoggedIn(): Token {
  	return JSON.parse(sessionStorage.getItem('currentUser'));
  }

  setLogOut() {
    this.userLoggedIn = false;
    sessionStorage.setItem('currentUser', '');
  }
}
