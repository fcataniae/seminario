import { Component } from '@angular/core';
import { Persona } from './../../../model/abm/persona.model';
import { Router } from '@angular/router';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Inject } from '@angular/core';

export interface Data {
  persona: Persona;
}
@Component({
  selector: 'app-alta-persona',
  templateUrl: './alta-persona.component.html',
  styleUrls: ['./alta-persona.component.css']
})
export class AltaPersonaComponent  {

  constructor(public _dialogRef: MatDialogRef<AltaPersonaComponent>,
              @Inject(MAT_DIALOG_DATA) private data: Data) {
    this.persona = this.data.persona;
  }

  persona: Persona;

  onSubmit(){
    this._dialogRef.close(this.persona);
  }

  onCancel(){
    this._dialogRef.close();
  }
}
