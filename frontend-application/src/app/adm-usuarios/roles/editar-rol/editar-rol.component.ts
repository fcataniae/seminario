import { Component, OnInit, AfterViewInit } from '@angular/core';
import { Rol } from '../../../model/rol.model';
import { RolService } from './../../../services/rol.service';
import { MatTableDataSource, MatSort, MatPaginator } from '@angular/material';
import { ConfirmacionPopupComponent } from '../../confirmacion-popup/confirmacion-popup.component';
import { MatDialog } from '@angular/material';
import { Router } from '@angular/router';
import { ViewChild } from '@angular/core';

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
          this._rolService.deleteRol(rol).subscribe();
        }
    });
  }

  redirectToUpdate(nombre: string){
    this._router.navigate(['/home/gestion/roles/adm/' + nombre]);
  }


  public doFilter  (value: string)  {
      this.dataSource.filter = value.trim().toLocaleLowerCase();
  }
}
