import { Component } from '@angular/core';
import { Persona } from './../../../model/abm/persona.model';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Inject } from '@angular/core';

export interface Data{
  persona: Persona;
}

@Component({
  selector: 'app-modificar-persona',
  templateUrl: './modificar-persona.component.html',
  styleUrls: ['./modificar-persona.component.css']
})
export class ModificarPersonaComponent{

  constructor(public _dialogRef: MatDialogRef<ModificarPersonaComponent>,
              @Inject(MAT_DIALOG_DATA) public data: Data)
  {
    this.persona = data.persona;
  }

  persona: Persona;

  onCancel(){
    this._dialogRef.close();
  }

  onSubmit(){
    this._dialogRef.close(this.persona);
  }

}
