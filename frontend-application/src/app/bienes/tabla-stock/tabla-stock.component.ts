import { Component, OnInit } from '@angular/core';
import { MovimientoService } from '../../services/movimiento.service';
import { Movimiento } from '../../model/bienes/movimiento.model';
import { MatTableDataSource, MatDialog, MatSort, MatPaginator } from '@angular/material';
import { ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { ConfirmarMovimientoComponent } from '../confirmar-movimiento/confirmar-movimiento.component';
import { ConfirmacionPopupComponent } from '../../adm-usuarios/confirmacion-popup/confirmacion-popup.component';
import { StockBienEnLocal } from '../../model/bienes/stockbienlocal.model';
import { Local } from '../../model/bienes/local.model';
import { StockBienLocalService } from '../../services/stockbienlocal.service';

export class filaTabla{
  nombre: string;
  stockBienes:StockBienEnLocal;
}

@Component({
  selector: 'app-tabla-stock',
  templateUrl: './tabla-stock.component.html',
  styleUrls: ['./tabla-stock.component.css']
})
export class TablaStockComponent implements OnInit {

constructor(private _movimientoService: MovimientoService,
              private _dialog: MatDialog,
              private _router: Router,
              private _stockbienlocalService: StockBienLocalService) { }

  listaStock: Local[];
  listaTabla: filaTabla[];

  public dataSource = new MatTableDataSource<filaTabla>();
  public displayedColumns = ['local','bien','stocklibre','stockocupado', 'stockreservado', 'stockdestruido'];
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  ngOnInit() {
    this._stockbienlocalService.getStockLocales()
      .subscribe( res => {
        console.log(res);
        this.listaStock = res;
        this.listaTabla = [];

        for(let i=0; i<this.listaStock.length; i++){
          for(let j=0; j<this.listaStock[i].stockBienes.length; j++){
            let fila = new filaTabla;
            fila.nombre = this.listaStock[i].nombre;
            fila.stockBienes = this.listaStock[i].stockBienes[j];
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
}

