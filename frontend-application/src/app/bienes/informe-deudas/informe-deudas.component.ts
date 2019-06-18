import { Component, OnInit, ViewChild  } from '@angular/core';
import { MovimientoService } from '../../services/movimiento.service';
import { StockBienLocalService } from '../../services/stockbienlocal.service';
import { MatTableDataSource, MatPaginator, MatSort, MatDialog} from '@angular/material';
import { Dashboard } from '../../model/bienes/dashboard.model';
import { Chart } from 'chart.js';
import { Agente } from '../../model/bienes/agente.model';
import { Proveedor } from '../../model/bienes/proveedor.model';
import { Bien } from '../../model/bienes/bien.model';
import { forkJoin, Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { FormControl } from '@angular/forms';
import { ExcelService } from '../../services/excel.service';

export class filaTabla{
  nro: number;
  nombre: string;
  descripcionBI: String;
  deudaBulto: String;
  deudaMonetaria: String;
}

@Component({
  selector: 'app-informe-deudas',
  templateUrl: './informe-deudas.component.html',
  styleUrls: ['./informe-deudas.component.css']
})
export class InformeDeudasComponent implements OnInit {

  constructor(private _movimientoService: MovimientoService,
              private _stockBienLocalService: StockBienLocalService,
              private _excelService: ExcelService,
              private _dialog: MatDialog) {
  }

  @ViewChild(MatSort) sortMov: MatSort;
  @ViewChild(MatPaginator) pagiMov: MatPaginator;
  // dsMov = new MatTableDataSource<filaTabla>();
  public dataSource = new MatTableDataSource<filaTabla>();
  public displayedColumns = ['nro','proveedor','bien','deudaBulto','deudaMonetaria'];

  proveedor: Agente;
  bien: Bien;
  cantBultosMin: number;
  cantBultosMax: number;
  montoMin: number;
  montoMax: number;

  bienes : Bien[];
  proveedores: Agente[];
  obserBi = new Observable<Bien[]>();
  obserPr = new Observable<Agente[]>();

  formBi = new FormControl();
  formPr = new FormControl();

  ngOnInit() {
    let lo = this._movimientoService.getAllAgentes();
    let bi = this._movimientoService.getAllBienes();
    forkJoin(lo,bi).pipe(
      map(([lores,bires])=>{
        this.proveedores = lores.filter(p => p.tipoAgente.nombre=== 'PROVEEDOR');
        this.bienes = bires;
        this.initFormControlers();
      })
    ).subscribe();
  }

  initFormControlers(){
    this.obserBi = this.formBi.valueChanges
    .pipe(
      startWith(''),
      map(value => this.filterBi(value))
    );
    this.obserPr = this.formPr.valueChanges
          .pipe(
            startWith(''),
            map(value => this.filterPr(value))
          );
    this.onChanges();
  }

  filterBi(value: string): any {
    return this.bienes.filter(t => t.descripcion.toLocaleLowerCase().includes(value.toLocaleLowerCase()));
  }
  filterPr(value: string): any {
    return this.proveedores.filter(t => t.denominacion.toLocaleLowerCase().includes(value.toLocaleLowerCase()));
  }

  submitSeach(){
    console.log('search');
    this._stockBienLocalService
        .getDeudaProveedores(
            this.proveedor,
            this.bien,
            this.cantBultosMin,
            this.cantBultosMax,
            this.montoMin,
            this.montoMax
        ).subscribe( res=>{
             console.log(res);
             this.dataSource.data = this.toArray(res);
             this.dataSource.sort = this.sortMov;
             this.dataSource.paginator = this.pagiMov;
        });
  }

  onChanges() {
    this.formBi.valueChanges.subscribe(
      res=> {
        this.bien = this.bienes.find(b => b.descripcion === res);
        console.log(this.bien);
      }
    );

    this.formPr.valueChanges.subscribe(
      res=> {
        this.proveedor = this.proveedores.find(b => b.denominacion === res);
        console.log(this.proveedor);
      }
    );
  }

  toArray(listaDeudas: Proveedor[]): filaTabla[]{
    let listaTabla = [];

    for(let i=0; i<listaDeudas.length; i++){
      for(let j=0; j<listaDeudas[i].deudaBienes.length; j++){
        let fila = new filaTabla;
        fila.nro = listaDeudas[i].nro;
        fila.nombre = listaDeudas[i].nombre;
        fila.descripcionBI = listaDeudas[i].deudaBienes[j].descripcionBI;
        fila.deudaBulto = listaDeudas[i].deudaBienes[j].deudaBultos;
        fila.deudaMonetaria = listaDeudas[i].deudaBienes[j].deudaMonetaria;
        listaTabla.push(fila);
      }
    }
    return listaTabla;
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

  exportAsExcel(){
    this._excelService.exportAsExcelFile(this.dataSource.filteredData,'deuda-proveedores');
  }
}
