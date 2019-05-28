import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TipoMovimiento } from '../../model/bienes/tipomovimiento.model';
import {MovimientoService} from "../../services/movimiento.service";
import {Agente} from "../../model/bienes/agente.model";
import { Movimiento } from '../../model/bienes/movimiento.model';


@Component({
  selector: 'app-movimientos',
  templateUrl: './movimientos.component.html',
  styleUrls: ['./movimientos.component.css']
})
export class MovimientosComponent implements OnInit {

  movimientos: TipoMovimiento[];
  origenes: Agente[];
  destinos: Agente[];
  selectedMov: TipoMovimiento;
  selectedDest: Agente;
  selectedOrig: Agente;
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

  onChangeMovimiento(){
    console.log(this.selectedMov);
    this._movimientoService.getAllAgentes().subscribe(
      res => {
        console.log(res);
        this.origenes = res.filter( a => a.tipoAgente.id === this.selectedMov.tipoAgenteOrigen.id);
        this.destinos = res.filter( a => a.tipoAgente.id === this.selectedMov.tipoAgenteDestino.id);
      },
      error => console.log(error)
    );

  }

  onSubmit(){
    console.log(this.selectedMov);
    let movimiento = new Movimiento();
    movimiento.destino = this.selectedDest.nro;
    movimiento.origen = this.selectedOrig.nro;
    movimiento.tipoMovimiento = this.selectedMov;
    this._router.navigate(["/home/movimientos/registrar/" + btoa(JSON.stringify(movimiento))]);
  }

}
