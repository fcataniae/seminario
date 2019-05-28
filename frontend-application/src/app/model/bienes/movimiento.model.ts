import { TipoMovimiento } from "./tipomovimiento.model";
import { Recurso } from "./recurso.model";
import { ItemMovimiento } from "./itemmovimiento.model";
import { TipoDocumento } from "./tipodocumento.model";

export class Movimiento{
  tipoMovimiento : TipoMovimiento;
  origen: number;
  destino: number;
  recursosAsignados: Recurso[];
  items : ItemMovimiento[];
  tipoDocumento: TipoDocumento;

  constructor(){
    this.recursosAsignados = [];
    this.items = []
    this.tipoMovimiento = new TipoMovimiento();
  }

}
