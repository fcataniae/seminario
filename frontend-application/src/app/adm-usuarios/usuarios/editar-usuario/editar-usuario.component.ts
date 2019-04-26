import { Component, OnInit } from '@angular/core';
import { Usuario } from '../../../model/usuario.model';
import { UsuarioService } from './../../../services/usuario.service';
import { Router } from '@angular/router';
import {MatDialog, MatDialogRef} from '@angular/material';
import { ConfirmacionPopupComponent } from '../../confirmacion-popup/confirmacion-popup.component';


@Component({
  selector: 'app-editar-usuario',
  templateUrl: './editar-usuario.component.html',
  styleUrls: ['./editar-usuario.component.css']
})
export class EditarUsuarioComponent implements OnInit {

  constructor(private _router: Router,
              private _usuarioService: UsuarioService,
              private dialog: MatDialog) { }

  usuarios: Usuario[];
  usuarioSelected: Usuario = null;
  ngOnInit( ) {
      //cargo todos los usuarios para mostrar un listado
      this._usuarioService.getAllUsuarios().subscribe(
        res => {
          this.usuarios = res;
        },
        error => {
          console.log(error);
        }
      );

  }

  deleteUser(usuario: Usuario){
    const dialogRef = this.dialog.open(ConfirmacionPopupComponent,{
      width: '40%',
      data: { mensaje: "Desea eliminar el usuario?"}
    });

    dialogRef.afterClosed().subscribe(result => {

        if (result && result == "true"){
          this._usuarioService.deleteUser(usuario).subscribe(
            res => {
              console.log(res);
              alert("Se elimino el usuario correctamente!");
              this._router.navigate(['/home/gestion/usuarios']);
            },
            error => {
              console.log(error);
            }
          );
        }
    });
  }

  modificarUser(usuario: Usuario){
    console.log(usuario);
    this.usuarioSelected = usuario;
  }

}
