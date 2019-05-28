import { TipoMovimiento } from "./tipomovimiento.model";
import { Recurso } from "./recurso.model";
import { ItemMovimiento } from "./itemmovimiento.model";
import { TipoDocumento } from "./tipodocumento.model";

export class Movimiento{
  tipoMovimiento : TipoMovimiento;
  origen: number;
  destino: number;
  recursosAsignados: Recurso[];
  itemMovimientos : ItemMovimiento[];
  tipoDocumento: TipoDocumento;
  nroDocumento: number;

  constructor(){
    this.recursosAsignados = [];
    this.itemMovimientos = []
    this.tipoMovimiento = new TipoMovimiento();
  }

}
