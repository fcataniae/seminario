import { Component, OnInit } from '@angular/core';
import { MovimientoService } from '../../services/movimiento.service';
import { Movimiento } from '../../model/bienes/movimiento.model';
import { MatTableDataSource, MatDialog, MatSort, MatPaginator } from '@angular/material';
import { ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { ConfirmarMovimientoComponent } from '../confirmar-movimiento/confirmar-movimiento.component';
import { ConfirmacionPopupComponent } from '../../adm-usuarios/confirmacion-popup/confirmacion-popup.component';

export class Movi{
  nro: string;
  fecha: Date;
  origen: string;
  destino: string;
  cantBienes: string;
  cantRecursos: string;
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
              private _dialog: MatDialog,
              private _router: Router) { }

  movimientos: Movimiento[];

  public dataSource = new MatTableDataSource<Movi>();
  public displayedColumns = ['nro','fecha','tipo','origen', 'destino', 'estado','nrodocumento','modificar','cancelar'];
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
      movi.fecha = m.fechaAlta;
      movi.nro = m.id.toString();
      movi.cantBienes = m.itemMovimientos.length.toString();
      movi.cantRecursos = m.recursosAsignados.length.toString();
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

  modificarMovimiento(element: Movi){

    this._movimientoService.getMovimientoByNro(element.nro)
      .subscribe(
        res => {
          console.log(res);
          let movimiento: Movimiento = res;
          this._router.navigate(["/home/movimientos/registrar/" + btoa(JSON.stringify(movimiento))]);

        }
      );
  }
  confirmarMovimiento(element:Movi){
    let dialog = this._dialog.open(ConfirmarMovimientoComponent,{
      width: '50%',
      data: { nro: element.nro}
    });

    dialog.afterClosed().subscribe(
      res=>{
        if(res && res == true){
          this.showDialog("Se confirmo correctamente el movimiento");
        }else if (res && res == false){
          this.showDialog("No se pudo confirmar el movimiento");
        }
      }
    );
  }
  onAltaMov(){
    this._router.navigate(['/home/movimientos/registrar']);
  }


    showDialog(msj: string) {
      let dialog = this._dialog.open(ConfirmacionPopupComponent,{
        data: {mensaje: msj },
        width: '50%'
      });
      dialog.afterClosed().subscribe();

    }
}
