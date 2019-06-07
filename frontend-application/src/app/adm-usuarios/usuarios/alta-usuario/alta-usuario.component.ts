import { Component, OnInit } from '@angular/core';
import { Usuario } from '../../../model/abm/usuario.model';
import { MatDialog, MatDialogRef } from '@angular/material';
import { Persona } from '../../../model/abm/persona.model';
import { AltaPersonaComponent } from '../../personas/alta-persona/alta-persona.component';
import { PersonaService } from '../../../services/persona.service';

@Component({
  selector: 'app-alta-usuario',
  templateUrl: './alta-usuario.component.html',
  styleUrls: ['./alta-usuario.component.css']
})
export class AltaUsuarioComponent {

  constructor(public dialogRef: MatDialogRef<AltaUsuarioComponent>,
              private dialog: MatDialog,
              private _personaService: PersonaService) {
    this.user = new Usuario();
    this.user.persona = new Persona();
  }

  user: Usuario;
  passwordCheck: string;


  onCancel(): void {
    this.dialogRef.close();
  }

  onSubmit(){
    this.dialogRef.close(this.user);
  }
  onAltaPersona(){
    let dialog = this.dialog.open(AltaPersonaComponent,{
      width: '50%'
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

}
