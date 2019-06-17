import { Component, OnInit, ViewChild } from '@angular/core';
import { MovimientoService } from '../../services/movimiento.service';
import { MatTableDataSource, MatPaginator, MatSort, MatDialog} from '@angular/material';
import { Agente } from '../../model/bienes/agente.model';
import { Chart } from 'chart.js';
import { Transportista } from '../../model/bienes/transportista.model';
import { Recurso } from '../../model/bienes/recurso.model';
import { Bien } from '../../model/bienes/bien.model';
import { forkJoin, Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { FormControl } from '@angular/forms';
import { Estado } from '../../model/bienes/estado.model';
import { TipoMovimiento } from '../../model/bienes/tipomovimiento.model';
import { Movimiento } from '../../model/bienes/movimiento.model';
import { VistaMovimientoComponent } from '../vista-movimiento/vista-movimiento.component';
import { ConfirmacionPopupComponent } from '../../adm-usuarios/confirmacion-popup/confirmacion-popup.component';
import { ExcelService } from '../../services/excel.service';

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

  constructor(private _movimientoService: MovimientoService,private _dialog: MatDialog,
              private _excelService: ExcelService) {
  }

  @ViewChild(MatSort) sortMov: MatSort;
  @ViewChild(MatPaginator) pagiMov: MatPaginator;
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

  movimientos: Movimiento[] = [];


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
            m.nombreOrigen = mr.origen;
          }
          else if(a.nro == m.destino){
            mr.destino = a.denominacion.substr(0,a.denominacion.length < 18 ? a.denominacion.length : 18);
            m.nombreDestino = mr.destino;
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
  parseDate(dateString: string): Date {
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
  showMovimiento(mov : MovimientoReducido){
    let movi = this.movimientos.filter(m => m.id.toString() == mov.nro)[0];
    console.log(movi);
    let dialog = this._dialog.open(VistaMovimientoComponent,{
      width: '70%',
      data: {movimiento: movi}
    });
    dialog.afterClosed().subscribe();
  }

  generarGraficos(){
    let divprincipal = document.getElementById("dg");
    if(this.movimientos){
        let estados: any[]= [];
        this.estadosViajes.forEach(e => { estados.push({estado: e.descrip,cantidad: 0})});
        this.movimientos.forEach(m=> {estados.forEach(e=> e.estado === m.estadoViaje.descrip? e.cantidad++:e.cantidad)});
        estados.forEach(e => e.cantidad = ( e.cantidad / this.movimientos.length)*100);
        let labels=[];
        let data=[];
        estados.forEach( e => { data.push(e.cantidad);labels.push(e.estado)})

        let div = document.getElementById('divcanvas1');
        let canvas = document.getElementById('canvas1');
        if(canvas){
          canvas.parentNode.removeChild(canvas);
          div.parentNode.removeChild(div);
          canvas = document.createElement('canvas');
          canvas.id= 'canvas1';
          div.classList.add("grafico");div.classList.add("mat-h2");div.classList.add("mat-elevation-z2");
          div.id= 'divcanvas1';
        }else{
          div = document.createElement('div');
          div.id= 'divcanvas1';
          div.classList.add("grafico");div.classList.add("mat-h2");div.classList.add("mat-elevation-z2");
          canvas = document.createElement('canvas');
          canvas.id= 'canvas1';
        }
        div.setAttribute("style", "display: inline-block; width: 40vw; heigth: 40vh; margin-left:5vw;");

        let chart : Chart;

        let canvasCast = <HTMLCanvasElement> canvas;

        chart = new Chart(canvasCast,{
          type: 'pie',
          data:{
            labels: labels,
            datasets:[{
              label: "Distribucion de estados de movimientos",
              data: data,
              backgroundColor:' rgba(54, 162, 235, 1)'
            }]
          },
          options: {
            title:{
               text:"Distribucion de estados de movimientos",
               display:true,
               fontSize: 18
            },
          }
        });
        div.appendChild(canvas);
        divprincipal.appendChild(div);


        let dias=[];
        this.movimientos.forEach(m => dias.push({dia:m.fechaSalida,total:0,entregado:0,pendiente:0,cancelado:0}));
        dias = dias.filter((ac,index,arr) => {
          return arr.findIndex( a => a.dia === ac.dia )  === index;
        });

        this.movimientos.forEach(
          m => {
            dias.forEach(d => {
              if(d.dia === m.fechaSalida){
                d.total++;
                if(m.estadoViaje.descrip==='ENTREGADO'){
                  d.entregado++;
                }else if(m.estadoViaje.descrip ==='PENDIENTE'){
                  d.pendiente++;
                }else if(m.estadoViaje.descrip === 'CANCELADO'){
                  d.cancelado++;
                }
              }
            })
          }
        );
        let div2 = document.getElementById('divcanvas2');
        let canvas2 = document.getElementById('canvas2');

        if(canvas2){
          canvas2.parentNode.removeChild(canvas2);
          div2.parentNode.removeChild(div2);
          div2 = document.createElement('div');
          div2.classList.add("grafico");div2.classList.add("mat-h2");div2.classList.add("mat-elevation-z2");
          canvas2 = document.createElement('canvas');
          canvas2.id= 'canvas2';
          div2.id = 'divcanvas2';
        }else{
          div2 = document.createElement('div');
          canvas2 = document.createElement('canvas');
          canvas2.id= 'canvas2';
          div2.id = 'divcanvas2';
          div2.classList.add("grafico");div2.classList.add("mat-h2");div2.classList.add("mat-elevation-z2");
        }
        div2.setAttribute("style", "display: inline-block; width: 40vw; heigth: 40vh; margin-left:5vw;");

        let label =[];
        let entregados = [];
        let cancelados = [];
        let pendientes = [];
        let totales = [];

        dias.sort((a,b) => {
          let d1 = new Date(a.dia).getTime();
          let d2 = new Date(b.dia).getTime();
          return  d1-d2;
        });
        dias.forEach(d => {
          label.push(d.dia);
          entregados.push(d.entregado);
          totales.push(d.total);
          cancelados.push(d.cancelado);
          pendientes.push(d.pendiente);
        });

        let canvasCast2 = <HTMLCanvasElement> canvas2;

        let chart2 = new Chart(canvasCast2,{
          type: (dias.length == 1) ?'bar':'line',
          data:((dias.length == 1)?
              {
              labels: ["Totales", "Entregados", "Pendientes", "Cancelados"],
                 datasets: [
                   {
                     label: "Distribucion de estados de movimientos",
                     backgroundColor: ["#3e95cd", "#8e5ea2","#3cba9f","#c45850"],
                     data: [dias[0].total,dias[0].entregado,dias[0].pendiente, dias[0].cancelado]
                   }
                 ]
               }

              : {
                labels: label,
                datasets: [{
                    data: totales,
                    label: "Totales",
                    borderColor: "#3e95cd",
                    fill: false
                  }, {
                    data: entregados,
                    label: "Entregados",
                    borderColor: "#8e5ea2",
                    fill: false
                  }, {
                    data: cancelados,
                    label: "Cancelados",
                    borderColor: "#3cba9f",
                    fill: false
                  }, {
                    data: pendientes,
                    label: "Pendientes",
                    borderColor: "#F35A30",
                    fill: false
                  }
                ]
              }),
          options: {
            title: {
              display: true,
              text: 'Distribucion de los movimientos por estado/dia'
            }
          }
        });
        div2.appendChild(canvas2);
        divprincipal.appendChild(div2);

        let div3 = document.getElementById('divcanvas3');
        let canvas3 = document.getElementById('canvas3');

        if(canvas3){
          canvas3.parentNode.removeChild(canvas3);
          div3.parentNode.removeChild(div3);
          div3 = document.createElement('div');
          canvas3 = document.createElement('canvas');
        }else{
          div3 = document.createElement('div');
          canvas3 = document.createElement('canvas');
        }
        canvas3.id= 'canvas3';
        div3.id = 'divcanvas3';
        div3.classList.add("grafico");div3.classList.add("mat-h2");div3.classList.add("mat-elevation-z2");
        div3.setAttribute("style", "display: inline-block; width: 40vw; heigth: 40vh; margin-left:5vw;");

        let dias2 = [];
        this.movimientos.forEach(m => dias2.push({dia: m.fechaSalida,stockdestruido:0,stockdevuelto:0,stockintercambiado:0, stockenviado: 0,stockrecibido: 0}));
        dias2 = dias2.filter((ac,index,arr) => {
          return arr.findIndex( a => a.dia === ac.dia )  === index;
        });
        dias2.sort((a,b) => {
          let d1 = new Date(a.dia).getTime();
          let d2 = new Date(b.dia).getTime();
          return  d1-d2;
        });
        this.movimientos.forEach(m=>{
          dias2.forEach(d =>{
            if(d.dia === m.fechaSalida){
              if(m.estadoViaje.descrip !=='CANCELADO'){
                let t = 0;
                m.itemMovimientos.forEach(i => t += i.cantidad);
                if(m.tipoMovimiento.tipo === 'ENVIO') d.stockenviado +=t;
                if(m.tipoMovimiento.tipo === 'RECEPCION') d.stockrecibido +=t;
                if(m.tipoMovimiento.tipo === 'DESTRUCCION') d.stockdestruido += t;
                if(m.tipoMovimiento.tipo === 'DEVOLUCION') d.stockdevuelto += t;
                if(m.tipoMovimiento.tipo === 'ENVIOINTERCAMBIO') d.stockintercambiado +=t;
              }
            }
          });
        });

        let labeld = [];
        let senviado = [];
        let sdestruido = [];
        let sdevuelto = [];
        let sintercamb = [];
        let srecibid = [];

        dias2.forEach(d => {
          labeld.push(d.dia);
          senviado.push(d.stockenviado);
          sdestruido.push(d.stockdestruido);
          sdevuelto.push(d.stockdestruido);
          sintercamb.push(d.stockintercambiado);
          srecibid.push(d.stockrecibido);
        });

        let chart3 = new Chart(canvas3,{
          type: (dias2.length == 1) ?'bar':'line',
          data:((dias2.length == 1)?
              {
              labels: ["Devueltos", "Intercambiados", "Recibidos", "Enviados", "Destruidos"],
                 datasets: [
                   {
                     label: "Distribucion flujo de bienes",
                     backgroundColor: ["#3e95cd", "#8e5ea2","#3cba9f","#c45850","ADF20E"],
                     data: [dias2[0].stockdevuelto,dias2[0].stockintercambiado,dias2[0].stockrecibido, dias2[0].stockenviado,dias2[0].stockdestruido]
                   }
                 ]
               }

              : {
                labels: labeld,
                datasets: [{
                    data: senviado,
                    label: "Enviados",
                    borderColor: "#3e95cd",
                    fill: false
                  }, {
                    data: sdestruido,
                    label: "Destruidos",
                    borderColor: "#8e5ea2",
                    fill: false
                  }, {
                    data: srecibid,
                    label: "Recibidos",
                    borderColor: "#3cba9f",
                    fill: false
                  }, {
                    data: sdevuelto,
                    label: "Devueltos",
                    borderColor: "#F35A30",
                    fill: false
                  }, {
                    data: sintercamb,
                    label: "Intercambiados",
                    borderColor: "#ADF20E",
                    fill: false
                  }
                ]
              }),
          options: {
            title: {
              display: true,
              text: 'Flujo de bienes por dia'
            }
          }
        });
        console.log(dias2);

        div3.appendChild(canvas3);
        divprincipal.appendChild(div3);

                let div4 = document.getElementById('divcanvas4');
                let canvas4 = document.getElementById('canvas4');

                if(canvas4){
                  canvas4.parentNode.removeChild(canvas4);
                  div4.parentNode.removeChild(div4);
                  div4 = document.createElement('div');
                  canvas4 = document.createElement('canvas');
                }else{
                  div4 = document.createElement('div');
                  canvas4 = document.createElement('canvas');
                }
                canvas4.id= 'canvas4';
                div4.id = 'divcanvas4';
                div4.classList.add("grafico");div4.classList.add("mat-h2");div4.classList.add("mat-elevation-z2");
                div4.setAttribute("style", "display: inline-block; width: 40vw; heigth: 40vh; margin-left:5vw;");

                let dias4 = [];
                this.movimientos.forEach(m => dias4.push({dia: m.fechaSalida,flujosalientet:0,flujosalientep:0,flujoentrantet:0,flujoentrantep:0,flujoperdido:0}));
                dias4 = dias4.filter((ac,index,arr) => {
                  return arr.findIndex( a => a.dia === ac.dia )  === index;
                });
                dias4.sort((a,b) => {
                  let d1 = new Date(a.dia).getTime();
                  let d2 = new Date(b.dia).getTime();
                  return  d1-d2;
                });
                this.movimientos.forEach(m=>{
                  dias4.forEach(d =>{
                    if(d.dia === m.fechaSalida){
                      if(m.estadoViaje.descrip !=='CANCELADO'){
                        let t = 0;
                        m.itemMovimientos.forEach(i => t += i.cantidad*i.precio);
                        if(m.tipoMovimiento.tipo === 'ENVIO') d.flujosalientet +=t;
                        if(m.tipoMovimiento.tipo === 'RECEPCION' &&
                           m.tipoMovimiento.tipoAgenteOrigen.nombre===  'PROVEEDOR') d.flujoentrantep +=t;
                        if(m.tipoMovimiento.tipo === 'RECEPCION' &&
                           m.tipoMovimiento.tipoAgenteOrigen.nombre===  'TIENDA') d.flujoentrantet +=t;
                        if(m.tipoMovimiento.tipo === 'DESTRUCCION') d.flujoperdido += t;
                        if(m.tipoMovimiento.tipo === 'DEVOLUCION') d.flujosalientep += t;
                        if(m.tipoMovimiento.tipo === 'ENVIOINTERCAMBIO') d.flujosalientep +=t;
                      }
                    }
                  });
                });

                let labelf = [];
                let fentrantep = [];
                let fentrantet = [];
                let fsalientep = [];
                let fsalientet = [];
                let fperdido = [];

                dias4.forEach(d => {
                  labelf.push(d.dia);
                  fentrantet.push(d.flujoentrantet);
                  fentrantep.push(d.flujoentrantep);
                  fsalientep.push(d.flujosalientep);
                  fsalientet.push(d.flujosalientet);
                  fperdido.push(d.flujoperdido);
                });

                let chart4 = new Chart(canvas4,{
                  type: (dias4.length == 1) ?'bar':'line',
                  data:((dias4.length == 1)?
                      {
                      labels: ["Flujo saliente proveedor","Flujo saliente tienda", "Fluejo entrante tienda","Fluejo entrante proveedor", "Flujo perdido"],
                         datasets: [
                           {
                             label: "Flujo monetario de operaciones en pesos($)",
                             backgroundColor: ["#3e95cd", "#8e5ea2","#EF0CA3","#011078","#3cba9f"],
                             data: [dias4[0].flujosalientep,dias4[0].flujosalientet,dias4[0].flujoentrantet,dias4[0].flujoentrantep,dias4[0].flujoperdido]
                           }
                         ]
                       }

                      : {
                        labels: labeld,
                        datasets: [{
                            data: fsalientet,
                            label: "Flujo Saliente Tienda",
                            borderColor: "#3e95cd",
                            fill: false
                          },{
                              data: fsalientep,
                              label: "Flujo Saliente Proveedor",
                              borderColor: "#EF0CA3",
                              fill: false
                            },
                          {
                            data: fentrantet,
                            label: "Flujo Entrante Tienda",
                            borderColor: "#8e5ea2",
                            fill: false
                          },
                          {
                            data: fentrantep,
                            label: "Flujo Entrante Proveedor",
                            borderColor: "#011078",
                            fill: false
                          }, {
                            data: fperdido,
                            label: "Flujo Perdido",
                            borderColor: "#3cba9f",
                            fill: false
                          }
                        ]
                      }),
                  options: {
                    title: {
                      display: true,
                      text: 'Flujo monetario de movimientos por dia en pesos ($)'
                    }
                  }
                });

                div4.appendChild(canvas4);
                divprincipal.appendChild(div4);

    }else{
      this.showError('No hay datos sobre los que se pueda realizar un grafico, por favor realice una busqueda!','Error',true);
    }

  }
  showError(msg: string, titul: string, err: boolean) {
    let dialog = this._dialog.open(ConfirmacionPopupComponent,{
      width: '30%',
      data: { mensaje: msg, titulo: titul, error: err}
    });
    dialog.afterClosed().subscribe();
  }
  exportAsExcel(){
    this._excelService.exportAsExcelFile(this.dsMov.data,'informe-movimientos');
  }
}
