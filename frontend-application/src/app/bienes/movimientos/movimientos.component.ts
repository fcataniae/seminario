import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TipoMovimiento } from '../../model/bienes/tipomovimiento.model';
import {MovimientoService} from "../../services/movimiento.service";
import {Agente} from "../../model/bienes/agente.model";

@Component({
  selector: 'app-movimientos',
  templateUrl: './movimientos.component.html',
  styleUrls: ['./movimientos.component.css']
})
export class MovimientosComponent implements OnInit {

  movimientos: TipoMovimiento[];
  origenes: Agente[];
  destinos: Agente[];

  disable: boolean = true;

  constructor(private _router : Router,
              private _movimientoService: MovimientoService) { }

  ngOnInit() {
    this._movimientoService.getAllTipoMovimientos().subscribe(
      res => {
        console.log(res);
        this.movimientos = res;
      },
      error => console.log(error)

    );
  }

  redirectToHome(){
    this._router.navigate(['home']);
  }



}
