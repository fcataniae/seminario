import { Component, OnInit } from '@angular/core';
import { MatTableDataSource, MatSort, MatPaginator } from '@angular/material';
import { ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { Local } from '../../model/bienes/local.model';
import { StockBienLocalService } from '../../services/stockbienlocal.service';
import { ExcelService } from '../../services/excel.service';
import { Agente } from '../../model/bienes/agente.model';
import { Bien } from '../../model/bienes/bien.model';
import { MovimientoService } from '../../services/movimiento.service';
import { forkJoin, Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { FormControl } from '@angular/forms';

export class filaTabla{
  nro: number;
  nombre: string;
  descripcionBI: String;
  stock_ocupado: String;
  stock_libre: String;
  stock_reservado: String;
  stock_destruido: String;
}

@Component({
  selector: 'app-tabla-stock',
  templateUrl: './tabla-stock.component.html',
  styleUrls: ['./tabla-stock.component.css']
})
export class TablaStockComponent implements OnInit {

  agentes: Agente[];
  bienes: Bien[];

  selectedBien: string;
  selectedLocal: string;

  local: Agente;
  bien:Bien;
  stockOcupadoMin: number;
  stockOcupadoMax: number;
  stockLibreMin: number;
  stockLibreMax: number;
  stockReservadoMin: number;
  stockReservadoMax: number;
  stockDestruidoMin: number;
  stockDestruidoMax: number;

  formLocal = new FormControl();
  formBi = new FormControl();

  obserLocal = new Observable<Agente[]>();
  obserBi = new Observable<Bien[]>();

constructor(private _excelService: ExcelService,
              private _router: Router,
              private _stockbienlocalService: StockBienLocalService,
              private _movimientoService: MovimientoService) { }

  listaStock: Local[];
  listaTabla: filaTabla[];

  public dataSource = new MatTableDataSource<filaTabla>();
  public displayedColumns = ['nro','local','bien','stocklibre','stockocupado', 'stockreservado', 'stockdestruido'];
  @ViewChild("sortStock") sort: MatSort;
  @ViewChild("paginatorStock") paginator: MatPaginator;

  ngOnInit() {
    let ag = this._movimientoService.getAllAgentes();
    let bi = this._movimientoService.getAllBienes();

    forkJoin(ag,bi).pipe(
      map(([agres,bires])=>{
        this.agentes = agres.filter(a => a.tipoAgente.id !== 3);
        this.bienes = bires;
        console.log(this.agentes);
        console.log(bires);

        this.obserLocal = this.formLocal.valueChanges
        .pipe(
          startWith(''),
          map(value => this.filterLocal(value))
        );

        this.obserBi = this.formBi.valueChanges
        .pipe(
          startWith(''),
          map(value => this.filterBi(value))
        );

        this.onChanges();

      })
    ).subscribe();
  }

  onChanges() {
    this.formBi.valueChanges.subscribe(
      res=> {
        this.bien = this.bienes.filter(b => b.descripcion === res)[0];
        console.log(this.bien);
      }
    );

    this.formLocal.valueChanges.subscribe(
      res=> {
        this.local = this.agentes.filter(b => b.denominacion === res)[0];
        console.log(this.local);
      }
    );
  }

  filterBi(value: string): any {
    return this.bienes.filter(t => t.descripcion.toLocaleLowerCase().includes(value.toLocaleLowerCase()));
  }
  filterLocal(value: string): any {
    return this.agentes.filter(t => t.denominacion.toLocaleLowerCase().includes(value.toLocaleLowerCase()));
  }

  redirectToHome(){
    this._router.navigate(['home']);
  }
  exportAsExcel(){
    this._excelService.exportAsExcelFile(this.dataSource.filteredData,'stock-bienes');
  }

  submitSearch(){
    console.log('search');
    this._movimientoService
    .getInformeStock(
                     this.bien,
                     this.local,
                     this.stockOcupadoMin,
                     this.stockOcupadoMax,
                     this.stockLibreMin,
                     this.stockLibreMax,
                     this.stockReservadoMin,
                     this.stockReservadoMax,
                     this.stockDestruidoMin,
                     this.stockDestruidoMax)
       .subscribe( res=>{
         console.log(res);
         this.listaStock = res;
         this.listaTabla = [];

         for(let i=0; i<this.listaStock.length; i++){
           for(let j=0; j<this.listaStock[i].stockBienes.length; j++){
             let fila = new filaTabla;
             fila.nro = this.listaStock[i].nro;
             fila.nombre = this.listaStock[i].nombre;
             fila.descripcionBI = this.listaStock[i].stockBienes[j].descripcionBI.toString();
             fila.stock_ocupado = this.listaStock[i].stockBienes[j].stock_ocupado.toString();
             fila.stock_libre = this.listaStock[i].stockBienes[j].stock_libre.toString();
             fila.stock_reservado = this.listaStock[i].stockBienes[j].stock_reservado.toString();
             fila.stock_destruido = this.listaStock[i].stockBienes[j].stock_destruido.toString();
             this.listaTabla.push(fila);
           }
         }

         this.dataSource.data = this.listaTabla;
         this.dataSource.sort = this.sort;
         this.dataSource.paginator = this.paginator;
       });
  }


}
