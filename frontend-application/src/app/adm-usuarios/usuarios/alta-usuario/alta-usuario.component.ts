import { Component, OnInit } from '@angular/core';
import { User } from './../../../model/user.model';

@Component({
  selector: 'app-alta-usuario',
  templateUrl: './alta-usuario.component.html',
  styleUrls: ['./alta-usuario.component.css']
})
export class AltaUsuarioComponent implements OnInit {

  constructor() { }

  user: User;
  passwordCheck: string;

  ngOnInit() {
    this.user = new User();
  }

  onSubmit(){
  }

}
