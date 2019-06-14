import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TipoMovimiento } from '../../model/bienes/tipomovimiento.model';
import {MovimientoService} from "../../services/movimiento.service";
import {Agente} from "../../model/bienes/agente.model";
import { Movimiento } from '../../model/bienes/movimiento.model';
import { FormControl } from '@angular/forms';
import { Observable, forkJoin } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { IntercambioProv } from 'src/app/model/bienes/intercambioprovedor.model';


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

  intercambios: IntercambioProv[];

  constructor(private _router : Router,
              private _movimientoService: MovimientoService) { }

  ngOnInit() {
    let observer1 = this._movimientoService.getAllIntercambioProveedor();
    let observer2 = this._movimientoService.getAllTipoMovimientos();
    let observer3 = this._movimientoService.getAllAgentes();
    forkJoin(observer1,observer2,observer3).pipe(
      map( ([res1,res2,res3]) => {
        console.log(res2);
        this.movimientos = res2;
        this.intercambios = res1;
        this.movsFilter = this.formMovs.valueChanges
        .pipe(
          startWith(''),
          map(value => this.filterMovs(value))
        );

        console.log(res3);
        this.agentes = res3;
      }
    )
  ).subscribe();

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
      if(this.selectedMov){
          if(this.selectedMov.tipo === 'ENVIOINTERCAMBIO'){
            console.log("es intercambio");
            let dest = [];
            this.intercambios.forEach(i => dest.push(i.proveedor));
            this.destinos = dest;
            console.log(this.destinos);
          }else{
            this.destinos = this.agentes.filter( a => a.tipoAgente.id === this.selectedMov.tipoAgenteDestino.id);
          }
          this.origenes = this.agentes.filter( a => a.tipoAgente.id === this.selectedMov.tipoAgenteOrigen.id);

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
      }
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
