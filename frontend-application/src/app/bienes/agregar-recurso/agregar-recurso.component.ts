import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material';
import { Recurso } from '../../model/bienes/recurso.model';

@Component({
  selector: 'app-agregar-recurso',
  templateUrl: './agregar-recurso.component.html',
  styleUrls: ['./agregar-recurso.component.css']
})
export class AgregarRecursoComponent implements OnInit {

  recurso: Recurso;

  constructor(public dialogRef: MatDialogRef<AgregarRecursoComponent>) {
    this.recurso = new Recurso();
   }

  ngOnInit() {
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  onSubmit(){
    this.dialogRef.close(this.recurso);
  }

}
