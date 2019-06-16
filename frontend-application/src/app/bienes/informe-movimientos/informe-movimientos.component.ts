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
import { Estado } from '../../model/bienes/estado.model';
import { Movimiento } from '../../model/bienes/movimiento.model';

export class MovimientoReducido{
  nro:string;
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

  @ViewChild("sortMovs") sortMov: MatSort;
  @ViewChild("paginatorMovs") pagiMov: MatPaginator;
  dsMov = new MatTableDataSource<MovimientoReducido>();
  displayedColumns = ['nro','destino','origen','tipo','fecha','estado','usuario','transport','ver'];

  agentes: Agente[];
  transportistas: Transportista[];
  bienes: Bien[];
  recursos: Recurso[];
  tipoMovs: TipoMovimiento[];
  estadosViajes: Estado[];

  origen: Agente;
  destino: Agente;
  bien:Bien;
  recurso: Recurso;
  transportista: Transportista;
  tipo: TipoMovimiento;
  estado: Estado;
  fechaHasta: Date = new Date();
  fechaDesde: Date = new Date();
  cantidadBi: number = 0;
  usuarioAlta: string = '';

  formAgD = new FormControl();
  formAgO = new FormControl();
  formTp = new FormControl();
  formRe = new FormControl();
  formBi = new FormControl();
  formMo = new FormControl();
  formEs = new FormControl();

  obserEs = new Observable<Estado[]>();
  obserAgD = new Observable<Agente[]>();
  obserAgO = new Observable<Agente[]>();
  obserTp = new Observable<Transportista[]>();
  obserRe = new Observable<Recurso[]>();
  obserBi = new Observable<Bien[]>();
  obserMo = new Observable<TipoMovimiento[]>();

  movimientos: Movimiento[];


  ngOnInit() {

    let ag = this._movimientoService.getAllAgentes();
    let tp = this._movimientoService.getAllTransportistas();
    let bi = this._movimientoService.getAllBienes();
    let re = this._movimientoService.getAllRecursos();
    let tm = this._movimientoService.getAllTipoMovimientos();
    let es = this._movimientoService.getAllEstadosViaje();

    forkJoin(ag,tp,bi,re,tm,es).pipe(
      map(([agres,tpres,bires,reres,tmres,esres])=>{
        this.agentes = agres;
        this.transportistas = tpres;
        this.bienes = bires;
        this.recursos = reres;
        this.tipoMovs = tmres;
        this.estadosViajes = esres;
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
    this.obserEs = this.formEs.valueChanges
    .pipe(
      startWith(''),
      map(value => this.filterEs(value))
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
    this.formEs.valueChanges.subscribe(
      res=> {
        this.estado = this.estadosViajes.filter(b => b.descrip === res)[0];
        console.log(this.estado);
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
  filterEs(value: string): any {
    return this.estadosViajes.filter(t => t.descrip.toLocaleLowerCase().includes(value.toLocaleLowerCase()));
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

  submitSeach(){
    console.log('search');
    this._movimientoService
        .getInformeMovimientos(this.origen,
                               this.destino,
                               this.bien,
                               this.recurso,
                               this.transportista,
                               this.tipo,
                               this.estado,
                               this.fechaDesde,
                               this.fechaHasta,
                               this.cantidadBi,
                               this.usuarioAlta)
           .subscribe( res=>{
             console.log(res);
             this.movimientos = res;
             this.dsMov.data = this.toArray(res);
             this.dsMov.sort = this.sortMov;
             this.dsMov.paginator = this.pagiMov;
           });
  }
  toArray(array: Movimiento[]): MovimientoReducido[]{
    let movis: MovimientoReducido[] = [];
    array.forEach( m => {
      let mr: MovimientoReducido = new MovimientoReducido();
      this.agentes.forEach(a => {
          if(a.nro == m.origen){
            mr.origen = a.denominacion.substr(0,a.denominacion.length < 18 ? a.denominacion.length : 18);
          }
          else if(a.nro == m.destino){
            mr.destino = a.denominacion.substr(0,a.denominacion.length < 18 ? a.denominacion.length : 18);
          }
      });

      mr.estadoViaje = m.estadoViaje.descrip;
      mr.nro = m.id.toString();
      mr.tipo = m.tipoMovimiento.nombre;
      mr.fecha = m.fechaSalida;
      this.transportistas.filter(t => {
        if(t.id == m.idTransportista)
        {
        mr.transportista = t.nombre;
        }
      });
      mr.usuarioAlta = m.usuarioAlta;
      movis.push(mr);
    });
    return movis;
  }
  parseDate(dateString): Date {
    console.log(dateString);
    var month = dateString.split("-")[1];
    var year = dateString.split("-")[0];
    var day = dateString.split("-")[2];

    if (dateString) {
        let strDate = (year+"-"+month+"-"+(Number(day)+1));
        console.log(strDate);
        return new Date(strDate);
    }
    return null;
}
}
