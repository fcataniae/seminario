import { Component, OnInit } from '@angular/core';
import { Rol } from '../../../model/rol.model';
import { Router } from '@angular/router';
import { RolService } from './../../../services/rol.service';

@Component({
  selector: 'app-modificar-rol',
  templateUrl: './modificar-rol.component.html',
  styleUrls: ['./modificar-rol.component.css']
})
export class ModificarRolComponent implements OnInit {


  constructor(private _route: Router,
              private _rolService: RolService) { }



  rol : Rol;


  ngOnInit() {
    this.rol = new Rol();
    this._route.paramMap.subscribe(params => {
      let id = params.get("id");
      this._rolService.getRolByName(id).subscribe(
        res => {
          this.rol = res;
        },
        error => {
          console.log(error);
        }
      );
    }
  }



}
