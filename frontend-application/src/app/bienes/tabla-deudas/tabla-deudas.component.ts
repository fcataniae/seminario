import { Component, OnInit } from '@angular/core';
import { MovimientoService } from '../../services/movimiento.service';
import { Movimiento } from '../../model/bienes/movimiento.model';
import { MatTableDataSource, MatDialog, MatSort, MatPaginator } from '@angular/material';
import { ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { ConfirmarMovimientoComponent } from '../confirmar-movimiento/confirmar-movimiento.component';
import { StockBienEnLocal } from '../../model/bienes/stockbienlocal.model';
import { Proveedor } from '../../model/bienes/proveedor.model';
import { StockBienLocalService } from '../../services/stockbienlocal.service';

export class filaTabla{
  nro: number;
  nombre: string;
  descripcionBI: String;
  deuda: String;
}

@Component({
  selector: 'app-tabla-deudas',
  templateUrl: './tabla-deudas.component.html',
  styleUrls: ['./tabla-deudas.component.css']
})
export class TablaDeudasComponent implements OnInit {

constructor(private _movimientoService: MovimientoService,
              private _dialog: MatDialog,
              private _router: Router,
              private _stockbienlocalService: StockBienLocalService) { }

  listaDeudas: Proveedor[];
  listaTabla: filaTabla[];

  public dataSource = new MatTableDataSource<filaTabla>();
  public displayedColumns = ['nro','proveedor','bien','deuda'];
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
            fila.deuda = this.listaDeudas[i].deudaBienes[j].deuda;
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
