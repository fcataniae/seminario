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

  public dataSource = new MatTableDataSource<Local>();
  public displayedColumns = ['local','bien','stocklibre','stockocupado', 'stockreservado', 'stockdestruido'];
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  ngOnInit() {
    this._stockbienlocalService.getStockLocales()
      .subscribe( res => {
        console.log(res);
        this.listaStock = res;
        this.dataSource.data = this.listaStock;
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

