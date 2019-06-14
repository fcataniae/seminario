import { Component, OnInit } from '@angular/core';
import { MatTableDataSource, MatSort, MatPaginator } from '@angular/material';
import { ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { Proveedor } from '../../model/bienes/proveedor.model';
import { StockBienLocalService } from '../../services/stockbienlocal.service';
import { ExcelService } from '../../services/excel.service';

export class filaTabla{
  nro: number;
  nombre: string;
  descripcionBI: String;
  deudaBulto: String;
  deudaMonetaria: String;
}

@Component({
  selector: 'app-tabla-deudas',
  templateUrl: './tabla-deudas.component.html',
  styleUrls: ['./tabla-deudas.component.css']
})
export class TablaDeudasComponent implements OnInit {

constructor(private _excelService: ExcelService,
              private _router: Router,
              private _stockbienlocalService: StockBienLocalService) { }

  listaDeudas: Proveedor[];
  listaTabla: filaTabla[];

  public dataSource = new MatTableDataSource<filaTabla>();
  public displayedColumns = ['nro','proveedor','bien','deudaBulto','deudaMonetaria'];
  @ViewChild("sortDeuda") sort: MatSort;
  @ViewChild("paginatorDeuda") paginator: MatPaginator;

  ngOnInit() {
    this._stockbienlocalService.getDeudaProveedores()
      .subscribe( res => {
        console.log(res);
        this.listaDeudas = res;
        this.listaTabla = [];

        for(let i=0; i<this.listaDeudas.length; i++){
          for(let j=0; j<this.listaDeudas[i].deudaBienes.length; j++){
            let fila = new filaTabla;
            fila.nro = this.listaDeudas[i].nro;
            fila.nombre = this.listaDeudas[i].nombre;
            fila.descripcionBI = this.listaDeudas[i].deudaBienes[j].descripcionBI;
            fila.deudaBulto = this.listaDeudas[i].deudaBienes[j].deudaBultos;
            fila.deudaMonetaria = this.listaDeudas[i].deudaBienes[j].deudaMonetaria;
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
    this._excelService.exportAsExcelFile(this.dataSource.filteredData,'deudas-prov');
  }
}
