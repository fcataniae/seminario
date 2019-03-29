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
    localStorage.setItem('currentUser', JSON.stringify(user));

  }

  getUserLoggedIn(): User {
  	return JSON.parse(localStorage.getItem('currentUser'));
  }

  setLogOut() {
    this.usserLogged = null;
    this.isUserLoggedIn = false;
    localStorage.setItem('currentUser', JSON.stringify(this.usserLogged));
  }
}
