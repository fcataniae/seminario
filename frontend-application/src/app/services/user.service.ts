import { Injectable } from '@angular/core';
import { User } from './../model/user.model'

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userLoggedIn;
  private usserLogged: User;

  constructor() {
  	this.userLoggedIn = false;
  }

  isUserLoggedIn(){
    return this.userLoggedIn;
  }
  setUserLoggedIn(user:User) {
    this.userLoggedIn = true;
    this.usserLogged = user;
    sessionStorage.setItem('currentUser', JSON.stringify(user));

  }

  getUserLoggedIn(): User {
  	return JSON.parse(sessionStorage.getItem('currentUser'));
  }

  setLogOut() {
    this.usserLogged = null;
    this.userLoggedIn = false;
    sessionStorage.setItem('currentUser', JSON.stringify(this.usserLogged));
  }
}
