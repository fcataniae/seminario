import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TipoMovimiento } from '../../model/bienes/tipomovimiento.model';

@Component({
  selector: 'app-movimientos',
  templateUrl: './movimientos.component.html',
  styleUrls: ['./movimientos.component.css']
})
export class MovimientosComponent implements OnInit {

  movimientos: TipoMovimiento[] = [
                             {nombre: 'Recepción', path: 'recepcion'},
                             {nombre: 'Envío', path: 'envio'},
                             {nombre: 'Destrucción', path: 'destruccion'},
                             {nombre: 'Devolución', path: 'devolucion'}
                             ];

  origenes: String[] = ['1','2','3'];
  destinos: String[] = ['1','2','3'];

  disable: boolean = true;

  redirectToHome(){
      this._router.navigate(['home']);
    }

  constructor(private _router : Router) { }

  ngOnInit() {
  }

}
