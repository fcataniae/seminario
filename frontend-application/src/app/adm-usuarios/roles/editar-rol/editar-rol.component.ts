import { Component, OnInit, AfterViewInit } from '@angular/core';
import { Rol } from '../../../model/rol.model';
import { RolService } from './../../../services/rol.service';
import { MatTableDataSource, MatSort, MatPaginator } from '@angular/material';
import { ConfirmacionPopupComponent } from '../../confirmacion-popup/confirmacion-popup.component';
import { MatDialog } from '@angular/material';
import { Router } from '@angular/router';
import { ViewChild } from '@angular/core';
import { AltaRolComponent } from '../alta-rol/alta-rol.component';

@Component({
  selector: 'app-editar-rol',
  templateUrl: './editar-rol.component.html',
  styleUrls: ['./editar-rol.component.css']
})
export class EditarRolComponent implements OnInit  {

  constructor(private _rolService: RolService,
              private dialog: MatDialog,
              private _router: Router) { }


  public dataSource = new MatTableDataSource<Rol>();
  public displayedColumns = ['nombre', 'descripcion', 'modificar', 'eliminar'];
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  ngOnInit() {

    this._rolService.getAllRoles().subscribe(
      res => {
        console.log(res);
        this.dataSource.data = res;
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
      },
      error =>
      {
        console.log(error);
      }
    )
  }


  deleteRol(rol: Rol){
    const dialogRef = this.dialog.open(ConfirmacionPopupComponent,{
      width: '90%',
      data: { mensaje: "Desea eliminar el rol?"}
    });
    dialogRef.afterClosed().subscribe(result => {

        if (result && result == "true"){
          this._rolService.deleteRol(rol).subscribe(
            res => {
              this.dataSource.data = this.dataSource.data.filter (e => e.nombre !== rol.nombre);
            }
          );
        }
    });
  }

  onAltaRol(){
    const dialogRef = this.dialog.open(AltaRolComponent,{
      width: '50%'
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log(result instanceof Rol );
      if( result instanceof Rol ){
        this._rolService.createRol(result).subscribe(
          res => {
            this._rolService.getAllRoles().subscribe(
              res2 => {
                this.dataSource.data = res2;
              },
              error =>
              {
                console.log(error);
              }
            )
          },
          error => {
            console.log(error);
            alert("Error al dar de alta el rol");
          }
        );
      }
    });
  }

  redirectToUpdate(nombre: string){
    this._router.navigate(['/home/adm/roles/' + nombre]);
  }


  doFilter  (value: string)  {
      this.dataSource.filter = value.trim().toLocaleLowerCase();
  }

  redirectToHome(){
    this._router.navigate(['home']);
  }
}
