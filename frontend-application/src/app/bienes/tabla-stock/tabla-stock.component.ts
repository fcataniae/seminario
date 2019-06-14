import { Component, OnInit } from '@angular/core';
import { MatTableDataSource, MatSort, MatPaginator } from '@angular/material';
import { ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { Local } from '../../model/bienes/local.model';
import { StockBienLocalService } from '../../services/stockbienlocal.service';
import { ExcelService } from '../../services/excel.service';

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

constructor(private _excelService: ExcelService,
              private _router: Router,
              private _stockbienlocalService: StockBienLocalService) { }

  listaStock: Local[];
  listaTabla: filaTabla[];

  public dataSource = new MatTableDataSource<filaTabla>();
  public displayedColumns = ['nro','local','bien','stocklibre','stockocupado', 'stockreservado', 'stockdestruido'];
  @ViewChild("sortStock") sort: MatSort;
  @ViewChild("paginatorStock") paginator: MatPaginator;

  ngOnInit() {
    this._stockbienlocalService.getStockLocales()
      .subscribe( res => {
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

  doFilter  (value: string)  {
      this.dataSource.filter = value.trim().toLocaleLowerCase();
  }

  redirectToHome(){
    this._router.navigate(['home']);
  }
  exportAsExcel(){
    this._excelService.exportAsExcelFile(this.dataSource.filteredData,'stock-bienes');
  }
}
