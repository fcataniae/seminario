import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TipoMovimiento } from '../../model/bienes/tipomovimiento.model';
import {MovimientoService} from "../../services/movimiento.service";
import {Agente} from "../../model/bienes/agente.model";
import { Movimiento } from '../../model/bienes/movimiento.model';
import { FormControl } from '@angular/forms';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';


@Component({
  selector: 'app-movimientos',
  templateUrl: './movimientos.component.html',
  styleUrls: ['./movimientos.component.css']
})
export class MovimientosComponent implements OnInit {



  formMovs = new FormControl();
  formOrig = new FormControl();
  formDest = new FormControl();

  movsFilter = new Observable<TipoMovimiento[]>();
  destFilter = new Observable<Agente[]>();
  origFilter = new Observable<Agente[]>();

  movimientos: TipoMovimiento[];
  origenes: Agente[];
  destinos: Agente[];
  agentes: Agente[];

  selectedMov: TipoMovimiento;
  selectedDest: Agente;
  selectedOrig: Agente;

  select1: string;
  select2: string;
  select3: string;
  disable: boolean = true;

  constructor(private _router : Router,
              private _movimientoService: MovimientoService) { }

  ngOnInit() {
    this._movimientoService.getAllTipoMovimientos().subscribe(
      res => {
        console.log(res);
        this.movimientos = res;

        this.movsFilter = this.formMovs.valueChanges
        .pipe(
          startWith(''),
          map(value => this.filterMovs(value))
        );

      },
      error => console.log(error)
    );

    this._movimientoService.getAllAgentes().subscribe(
    res => {
      console.log(res);
      this.agentes = res;
    },
    error => console.log(error)
    );

  }

  filterMovs(value : string): TipoMovimiento[] {
    return this.movimientos.filter(mov => mov.nombre.toLocaleLowerCase().includes(value.toLocaleLowerCase()));
  }
  filterDest(value : string): Agente[] {
    return this.destinos.filter(dest => dest.nombre.toLocaleLowerCase().includes(value.toLocaleLowerCase()));
  }
  filterOrig(value : string): Agente[] {
    return this.origenes.filter(dest => dest.nombre.toLocaleLowerCase().includes(value.toLocaleLowerCase()));
  }
  redirectToHome(){
    this._router.navigate(['home/movimientos']);
  }

  onChangeMovimiento(){
    if(this.movimientos){
      this.selectedMov = this.movimientos.filter(m => m.nombre === this.select1)[0];
      if(this.selectedMov)
        this._movimientoService.getAllAgentes().subscribe(
          res => {
            console.log(res);
            this.origenes = res.filter( a => a.tipoAgente.id === this.selectedMov.tipoAgenteOrigen.id);
            this.destinos = res.filter( a => a.tipoAgente.id === this.selectedMov.tipoAgenteDestino.id);

            this.select2 = "";
            this.onChangeOrigen();
            this.select3 = "";
            this.onChangeDestino();
            if(this.origenes.length==1){
              this.select2 = this.origenes[0].nombre;
              this.onChangeOrigen();
            }
            if(this.destinos.length==1){
              this.select3 = this.destinos[0].nombre;
              this.onChangeDestino();
            }

            this.destFilter = this.formDest.valueChanges
            .pipe(
              startWith(''),
              map(value => this.filterDest(value))
            );
            this.origFilter = this.formOrig.valueChanges
            .pipe(
              startWith(''),
              map(value => this.filterOrig(value))
            );
          },
          error => console.log(error)
        );
      }
  }

  onChangeDestino(){
    if(this.destinos)
      this.selectedDest = this.destinos.filter(d => d.nombre === this.select3)[0];

  }
  onChangeOrigen(){
    if(this.origenes)
      this.selectedOrig = this.origenes.filter(o => o.nombre === this.select2)[0];

  }

  onSubmit(){
    console.log(this.selectedMov);
    let movimiento = new Movimiento();
    movimiento.destino = this.selectedDest.nro;
    movimiento.origen = this.selectedOrig.nro;
    movimiento.nombreOrigen = this.selectedOrig.nombre;
    movimiento.nombreDestino = this.selectedDest.nombre;
    movimiento.tipoMovimiento = this.selectedMov;
    this._router.navigate(["/home/movimientos/registrar/" + btoa(JSON.stringify(movimiento))]);
  }

}
