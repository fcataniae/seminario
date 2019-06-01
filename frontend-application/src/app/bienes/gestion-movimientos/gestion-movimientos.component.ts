import { Component, OnInit } from '@angular/core';
import { MovimientoService } from '../../services/movimiento.service';
import { Movimiento } from '../../model/bienes/movimiento.model';
import { MatTableDataSource, MatSort, MatPaginator } from '@angular/material';
import { ViewChild } from '@angular/core';
import { Router } from '@angular/router';

export class Movi{
  nro: string;
  origen: string;
  destino: string;
  cantBienes: number;
  cantRecursos: number;
  nroDocumento: string;
  tipoDocumento: string;
  estadoViaje: string;
  tipo: string;
}
@Component({
  selector: 'app-gestion-movimientos',
  templateUrl: './gestion-movimientos.component.html',
  styleUrls: ['./gestion-movimientos.component.css']
})
export class GestionMovimientosComponent implements OnInit {

  constructor(private _movimientoService: MovimientoService,
              private _router: Router) { }

  movimientos: Movimiento[];

  public dataSource = new MatTableDataSource<Movi>();
  public displayedColumns = ['nro','tipo','origen', 'destino', 'estado','nrodocumento','modificar','cancelar'];
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  ngOnInit() {
    this._movimientoService.getAllMovimientos()
      .subscribe( res => {
        console.log(res);
        this.movimientos = res;
        this.dataSource.data = this.generateMovi();
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
      });
  }

  generateMovi(): Movi[]{
    let movis : Movi[] = [];

    this.movimientos.forEach(m => {
      let movi = new Movi();
      movi.nro = m.id.toString();
      movi.cantBienes = m.itemMovimientos.length;
      movi.cantRecursos = m.recursosAsignados.length;
      movi.destino = m.tipoMovimiento.tipoAgenteDestino.nombre + " - " + m.destino;
      movi.origen = m.tipoMovimiento.tipoAgenteOrigen.nombre + " - " + m.origen;
      movi.estadoViaje = m.estadoViaje.descrip;
      movi.nroDocumento = m.nroDocumento.toString();
      movi.tipoDocumento = m.tipoDocumento.descripcion;
      movi.tipo = m.tipoMovimiento.nombre;
      movis.push(movi);
    });

    return movis;
  }
  doFilter  (value: string)  {
      this.dataSource.filter = value.trim().toLocaleLowerCase();
  }

  redirectToHome(){
    this._router.navigate(['home']);
  }

  modificarMovimiento(element: Movimiento){
    console.log(element);
  }
  cancelarMovimiento(element:Movimiento){
    console.log(element);

  }
  onAltaMov(){
    this._router.navigate(['/home/movimientos/registrar']);
  }
}
