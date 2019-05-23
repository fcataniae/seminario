import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-confirmar-devolucion',
  templateUrl: './confirmar-devolucion.component.html',
  styleUrls: ['./confirmar-devolucion.component.css']
})
export class ConfirmarDevolucionComponent implements OnInit {

  columnsToDisplay = ['tipoDoc', 'idDoc', 'eliminar'];

  constructor(private _router : Router) { }

  ngOnInit() {
  }

  redirectToHome(){
    this._router.navigate(['home']);
  }

  onAgregarConfirmacion() {

  }

/*BORRAR*/
  estados: string[] = ['Devuelto','Cancelado'];

}
