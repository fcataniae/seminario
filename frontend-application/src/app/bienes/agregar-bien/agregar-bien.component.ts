import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material';
import { Bien } from '../../model/bienes/bien.model';

@Component({
  selector: 'app-agregar-bien',
  templateUrl: './agregar-bien.component.html',
  styleUrls: ['./agregar-bien.component.css']
})
export class AgregarBienComponent implements OnInit {

  bien: Bien;

  constructor(public dialogRef: MatDialogRef<AgregarBienComponent>) {
    this.bien = new Bien();
   }

  ngOnInit() {
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  onSubmit(){
    this.dialogRef.close(this.bien);
  }

}
