import { Component, OnInit } from '@angular/core';
import { MovimientoService } from '../../services/movimiento.service';
import { Movimiento } from '../../model/bienes/movimiento.model';
import { MatTableDataSource, MatDialog, MatSort, MatPaginator } from '@angular/material';
import { ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { ConfirmarMovimientoComponent } from '../confirmar-movimiento/confirmar-movimiento.component';
import { ConfirmacionPopupComponent } from '../../adm-usuarios/confirmacion-popup/confirmacion-popup.component';
import { VistaMovimientoComponent } from '../vista-movimiento/vista-movimiento.component';

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
  cantidadMovs: number = 100;
  public dataSource = new MatTableDataSource<Movi>();
  public displayedColumns = ['nro','fecha','tipo','origen', 'destino', 'estado','nrodocumento','visualizar','modificar','cancelar','clonar'];
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  ngOnInit() {
    this._movimientoService.getAllMovimientos(100)
      .subscribe( res => {
        console.log(res);
        this.movimientos = res;
        this.dataSource.data = this.generateMovi();
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
      });
  }

  submitSeach(){
    this._movimientoService.getAllMovimientos(this.cantidadMovs)
      .subscribe( res => {
        this.movimientos = res;
        this.dataSource.data = this.generateMovi();
      });
  }

  generateMovi(): Movi[]{
    let movis : Movi[] = [];

    this.movimientos.forEach(m => {
      let movi = new Movi();
      movi.fecha = m.fechaSalida;
      movi.nro = m.id.toString();
      movi.cantBienes = m.itemMovimientos.length.toString();
      movi.cantRecursos = m.recursosAsignados.length.toString();
      movi.destino = m.tipoMovimiento.tipoAgenteDestino.nombre + " - " + m.destino;
      movi.origen = m.tipoMovimiento.tipoAgenteOrigen.nombre + " - " + m.origen;
      movi.estadoViaje = m.estadoViaje.descrip;
      movi.nroDocumento = ((m.nroDocumento != null) ? m.nroDocumento.toString() : "");
      movi.tipoDocumento = ((m.tipoDocumento != null) ? m.tipoDocumento.descripcion : "");
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
          this._router.navigate(["/home/movimientos/modificar/" + btoa(JSON.stringify(movimiento))]);

        }
      );
  }

  duplicarMovimiento(element: Movi){

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
          this.showDialog("Se cambio el estado correctamente.","Confimacion de movimiento",true);//lo dejo en true para que me muestre solo el boton continuar
          this._movimientoService.getAllMovimientos(this.cantidadMovs)
            .subscribe( res => {
              console.log(res);
              this.movimientos = res;
              this.dataSource.data = this.generateMovi();
            });
        }else if (res && res == false){
          this.showDialog("No se pudo realizar la accion correctamente!","Confirmacion de movimiento",true);
        }
      }
    );
  }
  onAltaMov(){
    this._router.navigate(['/home/movimientos/registrar']);
  }


    showDialog(msj: string, titulo: string, error: boolean) {
      let dialog = this._dialog.open(ConfirmacionPopupComponent,{
        data: {mensaje: msj, titulo: titulo, error: error },
        width: '50%'
      });
      dialog.afterClosed().subscribe();

    }
  visualizarMovimiento(mov: Movi){
    let movimiento = this.movimientos.find(m => m.id.toString() === mov.nro);
    let dialog = this._dialog.open(VistaMovimientoComponent,{
      width: '70%',
      data: {movimiento: movimiento}
    });
    dialog.afterClosed().subscribe();
  }
}
