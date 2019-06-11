import { Component, OnInit, ViewChild } from '@angular/core';
import { Usuario } from '../../../model/abm/usuario.model';
import { UsuarioService } from './../../../services/usuario.service';
import { Router } from '@angular/router';
import { MatDialog, MatDialogRef, MatTableDataSource } from '@angular/material';
import { MatSort, MatPaginator } from '@angular/material';
import { ConfirmacionPopupComponent } from '../../confirmacion-popup/confirmacion-popup.component';
import { AltaUsuarioComponent } from '../alta-usuario/alta-usuario.component';
import { ModificarUsuarioComponent } from '../modificar-usuario/modificar-usuario.component';


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
  public dataSource = new MatTableDataSource<Usuario>();
  public displayedColumns = ['nombreUsuario', 'nombre', 'apellido','email', /*'nroDoc',*/ 'modificar', 'eliminar'];
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  ngOnInit( ) {
      //cargo todos los usuarios para mostrar un listado
      this._usuarioService.getAllUsuarios().subscribe(
        res => {
          this.dataSource.data = res;
          this.dataSource.sort = this.sort;
          this.dataSource.paginator = this.paginator;
        },
        error => {
          console.log(error);
        }
      );

  }

  deleteUsuario(usuario: Usuario){
    const dialogRef = this.dialog.open(ConfirmacionPopupComponent,{
      width: '40%',
      data: { mensaje: "Desea eliminar el usuario?", titulo:"Confirmar accion", error: false}
    });

    dialogRef.afterClosed().subscribe(result => {

        if (result && result == "true"){
          this._usuarioService.deleteUser(usuario).subscribe(
            res => {
              this.dataSource.data = this.dataSource.data.filter (e => e.nombreUsuario !== usuario.nombreUsuario);
            },
            error => {
              console.log(error);
            }
          );
        }
    });
  }
  onAltaUsuario(){
    const dialogRef = this.dialog.open(AltaUsuarioComponent,{
      width: '50%'
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log(result instanceof Usuario );
      if( result instanceof Usuario ){
        this._usuarioService.createUsuario(result).
          subscribe(
            res =>{
              console.log("alta usuario");
              console.log(res);
              let users = this.dataSource.data.filter(u => true);
              users.push(res);
              this.dataSource.data = users;
            },
            error => {
              console.log(error);
              alert("Error al crear el usuario " + error);
            }
          );
        }
    });
  }
  redirectToUpdate(usuario: Usuario){
    const dialogRef = this.dialog.open(ModificarUsuarioComponent,{
      width: '50%',
      data: {usuario: usuario}
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log(result);
      if( result ){
        this._usuarioService.updateUsuario(result).subscribe(
          res => {

          },
          error => {
            console.log(error);

          }
        );
      }
    });
  }

    doFilter  (value: string)  {
        this.dataSource.filter = value.trim().toLocaleLowerCase();
    }

    redirectToHome(){
      this._router.navigate(['home']);
    }
}
