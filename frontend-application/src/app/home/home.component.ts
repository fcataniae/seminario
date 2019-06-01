import { Component, OnInit } from '@angular/core';
import { SessionService } from './../services/session.service';
import { Router } from '@angular/router';
import { LoginService } from "../services/login.service";

import { Permiso }  from '../model/abm/permiso.model';
import { PermisoService } from './../services/permiso.service';
import { Token } from '../model/token.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(private _loginService: LoginService,
              private _router: Router,
              private _sessionService: SessionService,
              private _permisoService: PermisoService) { }

  ngOnInit() {

}

}
