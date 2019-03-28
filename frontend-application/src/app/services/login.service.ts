import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';


@Injectable()
export class LoginService {

  constructor(private http: HttpClient) {
  }

  login(username:string, password:string): Observable<string> {
    console.log(username + '   ' + password);
    return this.http.get<string>('https://api.github.com/users/fcataniae');
    // return this.http.post('https://reqres.in/api/login', {
    //   email: username,
    //   password: password,
    // });
  }
}
