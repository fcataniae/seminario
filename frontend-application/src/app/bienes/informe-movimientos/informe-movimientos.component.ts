import { Component, OnInit, ViewChild } from '@angular/core';
import { MovimientoService } from '../../services/movimiento.service';
import { MatTableDataSource, MatPaginator, MatSort} from '@angular/material';
import { Dashboard } from '../../model/bienes/dashboard.model';
import { Chart } from 'chart.js';
import { Agente } from '../../model/bienes/agente.model';
import { Transportista } from '../../model/bienes/transportista.model';
import { Recurso } from '../../model/bienes/recurso.model';
import { Bien } from '../../model/bienes/bien.model';
import { forkJoin, Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { FormControl } from '@angular/forms';
import { TipoMovimiento } from '../../model/bienes/tipomovimiento.model';

export class MovimientoReducido{
  nro:number;
  destino: string;
  origen: string;
  fecha: Date;
  transportista: string;
  estadoViaje: string;
  usuarioAlta: string;
  tipo: string;
}
@Component({
  selector: 'app-informe-movimientos',
  templateUrl: './informe-movimientos.component.html',
  styleUrls: ['./informe-movimientos.component.css']
})
export class InformeMovimientosComponent implements OnInit {

  constructor(private _movimientoService: MovimientoService) {
  }

  @ViewChild("sortMovs") sortEnvios: MatSort;
  @ViewChild("paginatorMovs") paginatorEnvios: MatPaginator;
  datasourceMovimientos = new MatTableDataSource<MovimientoReducido>();

  agentes: Agente[];
  transportistas: Transportista[];
  bienes: Bien[];
  recursos: Recurso[];
  tipoMovs: TipoMovimiento[];

  origen: Agente;
  destino: Agente;
  bien:Bien;
  recurso: Recurso;
  transportista: Transportista;
  tipo: TipoMovimiento;

  formAgD = new FormControl();
  formAgO = new FormControl();
  formTp = new FormControl();
  formRe = new FormControl();
  formBi = new FormControl();
  formMo = new FormControl();

  obserAgD = new Observable<Agente[]>();
  obserAgO = new Observable<Agente[]>();
  obserTp = new Observable<Transportista[]>();
  obserRe = new Observable<Recurso[]>();
  obserBi = new Observable<Bien[]>();
  obserMo = new Observable<TipoMovimiento[]>();

  ngOnInit() {

    let ag = this._movimientoService.getAllAgentes();
    let tp = this._movimientoService.getAllTransportistas();
    let bi = this._movimientoService.getAllBienes();
    let re = this._movimientoService.getAllRecursos();
    let tm = this._movimientoService.getAllTipoMovimientos();
    forkJoin(ag,tp,bi,re,tm).pipe(
      map(([agres,tpres,bires,reres,tmres])=>{
        this.agentes = agres;
        this.transportistas = tpres;
        this.bienes = bires;
        this.recursos = reres;
        this.tipoMovs = tmres;
        this.initFormControlers();
      })
    ).subscribe();
  }

  initFormControlers() {
    this.obserAgO = this.formAgO.valueChanges
    .pipe(
      startWith(''),
      map(value => this.filterAgO(value))
    );

    this.obserAgD = this.formAgD.valueChanges
    .pipe(
      startWith(''),
      map(value => this.filterAgD(value))
    );
    this.obserBi = this.formBi.valueChanges
    .pipe(
      startWith(''),
      map(value => this.filterBi(value))
    );
    this.obserRe = this.formRe.valueChanges
    .pipe(
      startWith(''),
      map(value => this.filterRe(value))
    );
    this.obserTp = this.formTp.valueChanges
    .pipe(
      startWith(''),
      map(value => this.filterTp(value))
    );
    this.obserMo = this.formMo.valueChanges
    .pipe(
      startWith(''),
      map(value => this.filterMo(value))
    );
    this.onChanges();
  }
  onChanges() {
    this.formBi.valueChanges.subscribe(
      res=> {
        this.bien = this.bienes.filter(b => b.descripcion === res)[0];
        console.log(this.bien);
      }
    );
    this.formRe.valueChanges.subscribe(
      res=> {
        this.recurso = this.recursos.filter(b => b.nroRecurso.toString() == res)[0];
        console.log(this.recurso);
      }
    );
    this.formTp.valueChanges.subscribe(
      res=> {
        this.transportista = this.transportistas.filter(b => b.nombre === res)[0];
        console.log(this.transportista);
      }
    );
    this.formAgD.valueChanges.subscribe(
      res=> {
        this.destino = this.agentes.filter(b => b.denominacion === res)[0];
        console.log(this.destino);
      }
    );
    this.formAgO.valueChanges.subscribe(
      res=> {
        this.origen = this.agentes.filter(b => b.denominacion === res)[0];
        console.log(this.origen);
      }
    );
    this.formMo.valueChanges.subscribe(
      res=> {
        this.tipo = this.tipoMovs.filter(b => b.nombre === res)[0];
        console.log(this.tipo);
      }
    );

  }
  filterTp(value: string): any {
    return this.transportistas.filter(t => t.nombre.toLocaleLowerCase().includes(value.toLocaleLowerCase()));
  }
  filterMo(value: string): any {
    return this.tipoMovs.filter(t => t.nombre.toLocaleLowerCase().includes(value.toLocaleLowerCase()));
  }
  filterRe(value: string): any {
    return this.recursos.filter(t => t.nroRecurso.toString().includes(value));
  }
  filterBi(value: string): any {
    return this.bienes.filter(t => t.descripcion.toLocaleLowerCase().includes(value.toLocaleLowerCase()));
  }
  filterAgD(value: string): any {
    return this.agentes.filter(t => t.denominacion.toLocaleLowerCase().includes(value.toLocaleLowerCase()));
  }
  filterAgO(value: string): any {
    return this.agentes.filter(t => t.denominacion.toLocaleLowerCase().includes(value.toLocaleLowerCase()));
  }

}
