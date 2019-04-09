import { Component, OnInit } from '@angular/core';
import { Rol } from '../../../model/rol.model';
import { RolService } from './../../../services/rol.service';

@Component({
  selector: 'app-editar-rol',
  templateUrl: './editar-rol.component.html',
  styleUrls: ['./editar-rol.component.css']
})
export class EditarRolComponent implements OnInit {

  constructor(private _rolService: RolService) { }

  roles: Rol[];

  ngOnInit() {

    this._rolService.getAllRoles().subscribe(
      res => {
        console.log(res);
        this.roles = res;
      },
      error =>
      {
        console.log(error);
      }
    )
  }

  deleteRol(rol: Rol){
    this._rolService.deleteRol(rol).subscribe();
  }
}
