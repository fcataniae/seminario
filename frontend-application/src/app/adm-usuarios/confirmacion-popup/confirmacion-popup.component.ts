import {Component, Inject} from '@angular/core';
import {MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';

export interface Data{
  mensaje: string;
}

@Component({
  selector: 'app-confirmacion-popup',
  templateUrl: './confirmacion-popup.component.html',
  styleUrls: ['./confirmacion-popup.component.css']
})
export class ConfirmacionPopupComponent {

  constructor(
      public dialogRef: MatDialogRef<ConfirmacionPopupComponent>,
      @Inject(MAT_DIALOG_DATA) public data: Data) {

      }

    onCancel(): void {
      this.dialogRef.close();
    }
    onSubmit(): void {
      this.dialogRef.close("true");
    }
}