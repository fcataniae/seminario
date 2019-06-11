import { Component, OnInit } from '@angular/core';
import { Persona } from '../../../model/abm/persona.model';
import { MatTableDataSource, MatPaginator, MatSort, MatDialog } from '@angular/material';
import { ViewChild } from '@angular/core';
import { PersonaService } from '../../../services/persona.service';
import { Router } from '@angular/router';
import { ConfirmacionPopupComponent } from '../../confirmacion-popup/confirmacion-popup.component';
import { ModificarPersonaComponent } from '../modificar-persona/modificar-persona.component';
import { AltaPersonaComponent } from '../alta-persona/alta-persona.component';

@Component({
  selector: 'app-gestion-personas',
  templateUrl: './gestion-personas.component.html',
  styleUrls: ['./gestion-personas.component.css']
})
export class GestionPersonasComponent implements OnInit {

  constructor(private _personaService: PersonaService,
              private _dialog : MatDialog,
              private _router : Router) { }

  ngOnInit() {
    this._personaService.getAllPersonas().subscribe(
      res => {
        console.log(res);
        this.dataSource.data = res;
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
      },
      error => {
        console.log(error);
      }
    );

  }

  public dataSource = new MatTableDataSource<Persona>();
  public displayedColumns = ['nombre', 'apellido', 'modificar', 'eliminar'];
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  deletePersona(persona: Persona){
    const dialogRef = this._dialog.open(ConfirmacionPopupComponent,{
      width: '90%',
      data: { mensaje: "Desea eliminar la persona?", titulo: "Confirmar accion" , error : false}
    });
    dialogRef.afterClosed().subscribe(result=>{
      if (result && result == "true"){
        this._personaService.deletePersona(persona).subscribe(
          res => {
            this.dataSource.data = this.dataSource.data.filter (e => e.nroDoc !== persona.nroDoc);
          }
        );
      }
    });
  }


    onAltaPersona(){
      const dialogRef = this._dialog.open(AltaPersonaComponent,{
        width: '50%'
      });
      dialogRef.afterClosed().subscribe(result => {
        console.log(result instanceof Persona );
        if( result instanceof Persona ){
          this._personaService.createPersona(result).subscribe(
            res => {
              this._personaService.getAllPersonas().subscribe(
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
              alert("Error al dar de alta la persona");
            }
          );
        }
      });
    }

    redirectToUpdate(persona: Persona){
      const dialogRef = this._dialog.open(ModificarPersonaComponent,{
        width: '50%',
        data: {persona: persona}
      });
      dialogRef.afterClosed().subscribe(result => {
        if( result ){
          this._personaService.modificarPersona(result).subscribe(
            res => {
              console.log("update ok");
            },
            error => {
              console.log(error);
              alert('No se pudo actualizar los datos de la persona.');
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
