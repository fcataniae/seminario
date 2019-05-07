import { Component } from '@angular/core';
import { Persona } from './../../../model/persona.model';
import { Router } from '@angular/router';
import { MatDialogRef } from '@angular/material';

@Component({
  selector: 'app-alta-persona',
  templateUrl: './alta-persona.component.html',
  styleUrls: ['./alta-persona.component.css']
})
export class AltaPersonaComponent  {

  constructor(public _dialogRef: MatDialogRef<AltaPersonaComponent>) {
    this.persona = new Persona();
  }

  persona: Persona;

  onSubmit(){
    this._dialogRef.close(this.persona);
  }

  onCancel(){
    this._dialogRef.close();
  }
}
