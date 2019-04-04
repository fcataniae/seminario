import { Injectable } from '@angular/core';
import { Usuario } from '../model/usuario.model'

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  private userLoggedIn;
  private usserLogged: Usuario;

  constructor() {
  	this.userLoggedIn = false;
  }

  isUserLoggedIn(){
    return this.userLoggedIn;
  }
  setUserLoggedIn(user:Usuario) {
    this.userLoggedIn = true;
    this.usserLogged = user;
    sessionStorage.setItem('currentUser', JSON.stringify(user));

  }

  getUserLoggedIn(): Usuario {
  	return JSON.parse(sessionStorage.getItem('currentUser'));
  }

  setLogOut() {
    this.usserLogged = null;
    this.userLoggedIn = false;
    sessionStorage.setItem('currentUser', JSON.stringify(this.usserLogged));
  }
}
