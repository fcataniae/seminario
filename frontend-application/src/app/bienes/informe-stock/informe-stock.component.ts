import { Component, OnInit, ViewChild  } from '@angular/core';
import { MovimientoService } from '../../services/movimiento.service';
import { StockBienLocalService } from '../../services/stockbienlocal.service';
import { MatTableDataSource, MatPaginator, MatSort, MatDialog} from '@angular/material';
import { Dashboard } from '../../model/bienes/dashboard.model';
import { Chart } from 'chart.js';
import { Agente } from '../../model/bienes/agente.model';
import { Local } from '../../model/bienes/local.model';
import { Bien } from '../../model/bienes/bien.model';
import { forkJoin, Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { FormControl } from '@angular/forms';
import { ExcelService } from '../../services/excel.service';

export class filaTabla{
  nro: number;
  nombre: string;
  descripcionBI: String;
  stock_ocupado: String;
  stock_libre: String;
  stock_reservado: String;
  stock_destruido: String;
  actualizacion: Date;
}

@Component({
  selector: 'app-informe-stock',
  templateUrl: './informe-stock.component.html',
  styleUrls: ['./informe-stock.component.css']
})

export class InformeStockComponent implements OnInit {

  constructor(private _movimientoService: MovimientoService,
              private _stockBienLocalService: StockBienLocalService,
              private _excelService: ExcelService,
              private _dialog: MatDialog) {
  }

  @ViewChild(MatSort) sortMov: MatSort;
  @ViewChild(MatPaginator) pagiMov: MatPaginator;
  // dsMov = new MatTableDataSource<filaTabla>();
  public dataSource = new MatTableDataSource<filaTabla>();
  public displayedColumns = ['nro','local','bien','stocklibre','stockocupado', 'stockreservado', 'stockdestruido', 'actualizacion'];

  fechaHasta: Date;
  fechaDesde: Date;
  local: Agente;
  bien: Bien;
  cantMinStockLibre: number;
  cantMaxStockLibre: number;
  cantMinStockOcupado: number;
  cantMaxStockOcupado: number;
  cantMinStockReservado: number;
  cantMaxStockReservado: number;
  cantMinStockDestruido: number;
  cantMaxStockDestruido: number;

  bienes : Bien[];
  locales: Agente[];
  obserBi = new Observable<Bien[]>();
  obserLo = new Observable<Agente[]>();

  formBi = new FormControl();
  formLo = new FormControl();

  ngOnInit() {
    let lo = this._movimientoService.getAllLocales();
    let bi = this._movimientoService.getAllBienes();
    forkJoin(lo,bi).pipe(
      map(([lores,bires])=>{
        this.locales = lores;
        this.bienes = bires;
        console.log(this.locales);
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
    this.obserLo = this.formLo.valueChanges
          .pipe(
            startWith(''),
            map(value => this.filterLo(value))
          );
    this.onChanges();
  }

  filterBi(value: string): any {
    return this.bienes.filter(t => t.descripcion.toLocaleLowerCase().includes(value.toLocaleLowerCase()));
  }
  filterLo(value: string): any {
    return this.locales.filter(t => t.denominacion.toLocaleLowerCase().includes(value.toLocaleLowerCase()));
  }

  submitSeach(){
    console.log('search');
    console.log(this.fechaHasta,
    this.fechaDesde,
    this.local,
    this.bien);
    this._stockBienLocalService
        .getStockLocales(
            this.fechaDesde,
            this.fechaHasta,
            this.local,
            this.bien,
            this.cantMinStockLibre,
            this.cantMaxStockLibre,
            this.cantMinStockOcupado,
            this.cantMaxStockOcupado,
            this.cantMinStockReservado,
            this.cantMaxStockReservado,
            this.cantMinStockDestruido,
            this.cantMaxStockDestruido
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
        this.bien = this.bienes.filter(b => b.descripcion === res)[0];
        console.log(this.bien);
      }
    );

    this.formLo.valueChanges.subscribe(
      res=> {
        this.local = this.locales.filter(b => b.denominacion === res)[0];
        console.log(this.local);
      }
    );
  }

  toArray(listaStock: Local[]): filaTabla[]{
    let listaTabla = [];

    for(let i=0; i<listaStock.length; i++){
      for(let j=0; j<listaStock[i].stockBienes.length; j++){
        let fila = new filaTabla;
        fila.nro = listaStock[i].nro;
        fila.nombre = listaStock[i].nombre;
        fila.descripcionBI = listaStock[i].stockBienes[j].descripcionBI.toString();
        fila.stock_ocupado = listaStock[i].stockBienes[j].stock_ocupado.toString();
        fila.stock_libre = listaStock[i].stockBienes[j].stock_libre.toString();
        fila.stock_reservado = listaStock[i].stockBienes[j].stock_reservado.toString();
        fila.stock_destruido = listaStock[i].stockBienes[j].stock_destruido.toString();
        fila.actualizacion = listaStock[i].stockBienes[j].actualizacion;
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
    this._excelService.exportAsExcelFile(this.dataSource.filteredData,'stock-bienes');
  }
}
