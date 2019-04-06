import { Component, OnInit } from '@angular/core';
import { Rol } from './../../../model/rol.model';
import { Router } from '@angular/router';
import { RolService } from './../../../services/rol.service';

@Component({
  selector: 'app-alta-rol',
  templateUrl: './alta-rol.component.html',
  styleUrls: ['./alta-rol.component.css']
})
export class AltaRolComponent implements OnInit {

  constructor( private _rolService: RolService,
               private _router: Router) { }

  rol: Rol;

  ngOnInit() {
      this.rol = new Rol();
      this.rol.permisos = null;
  }
  onSubmit(){
    this._rolService.createRol(this.rol).subscribe(
      res => {
        console.log(res);
        alert("Se dio de alta correctamente el rol");
        this._router.navigate(['/home/gestion/roles']);
      },
      error => {
        console.log(error);
        alert("Error al dar de alta el rol");
      }
    );
  }
}
