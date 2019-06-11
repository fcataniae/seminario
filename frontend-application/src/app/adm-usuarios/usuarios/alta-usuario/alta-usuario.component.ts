import { Component, OnInit } from '@angular/core';
import { Usuario } from '../../../model/abm/usuario.model';
import { MatDialog, MatDialogRef } from '@angular/material';
import { Persona } from '../../../model/abm/persona.model';
import { AltaPersonaComponent } from '../../personas/alta-persona/alta-persona.component';
import { PersonaService } from '../../../services/persona.service';
import { Agente } from '../../../model/bienes/agente.model';
import { Observable } from 'rxjs';
import { FormControl } from '@angular/forms';
import { MovimientoService } from '../../../services/movimiento.service';
import { startWith } from 'rxjs/internal/operators/startWith';
import { map } from 'rxjs/operators';
import { Local } from '../../../model/bienes/local.model';

@Component({
  selector: 'app-alta-usuario',
  templateUrl: './alta-usuario.component.html',
  styleUrls: ['./alta-usuario.component.css']
})
export class AltaUsuarioComponent {

  constructor(public dialogRef: MatDialogRef<AltaUsuarioComponent>,
              private dialog: MatDialog,
              private _personaService: PersonaService,
              private _movimientoService: MovimientoService)
  {
    this._movimientoService.getAllLocales().subscribe(
      res => {
        this.locales = res;

        this.localFilter = this.localForm.valueChanges
        .pipe(
          startWith(''),
          map(value => this.filterLoca(value))
        );
      }
    );
  }

  user: Usuario = new Usuario();
  passwordCheck: string;


  onCancel(): void {
    this.dialogRef.close();
  }

  onSubmit(){
    this._personaService.getPersonaByDocumento(this.user.persona.nroDoc).subscribe(res => {
      if(res && res !== null){
        this.dialogRef.close(this.user);
      }else{
        this.onAltaPersona();
      }
    });

  }
  onAltaPersona(){
    let dialog = this.dialog.open(AltaPersonaComponent,{
      width: '50%',
      data: {persona: this.user.persona}
    });

    dialog.afterClosed().subscribe(
      res => {
        console.log(res);
        if(res || res != null ){
            this._personaService.createPersona(res).subscribe(
              res2 => {
                this.user.persona = res;
              }
            );
        }
      }
    );

  }

  localForm = new FormControl();
  localFilter = new Observable<Agente[]>();
  locales: Agente[];
  selectedLoca: string;

  refreshLoca(){
    if(this.locales){
      this.locales.forEach(l =>{
         if(l.denominacion == this.selectedLoca){
           let local: Local = new Local();

           local.nro = l.nro;
           local.denominacion = l.denominacion;
           local.direccion_nro = l.direccion_nro;
           local.email = l.email;

           this.user.local = local;
         }
       });
    }
  }

  filterLoca(value : string){
    return this.locales.filter(l => l.denominacion.toLocaleLowerCase().includes(value.toLocaleLowerCase()) );
  }

}
