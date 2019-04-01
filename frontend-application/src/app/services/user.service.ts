import { Injectable } from '@angular/core';
import { User } from './../model/user.model'

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private isUserLoggedIn;
  public usserLogged: User;

  constructor() {
  	this.isUserLoggedIn = false;
  }

  setUserLoggedIn(user:User) {
    this.isUserLoggedIn = true;
    this.usserLogged = user;
    sessionStorage.setItem('currentUser', JSON.stringify(user));

  }

  getUserLoggedIn(): User {
  	return JSON.parse(sessionStorage.getItem('currentUser'));
  }

  setLogOut() {
    this.usserLogged = null;
    this.isUserLoggedIn = false;
    sessionStorage.setItem('currentUser', JSON.stringify(this.usserLogged));
  }
}
