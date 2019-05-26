import { TipoMovimiento } from "./tipomovimiento.model";
import { Recurso } from "./recurso.model";
//import { ItemMovimiento } from "./itemmovimiento.model";
import { ItemTabla } from "./itemtabla.model";

export class Movimiento{
  tipoMovimiento : TipoMovimiento;
  origen: number;
  destino: number;
  recursosAsignados: Recurso[];
  items : ItemTabla[];
  //items : ItemMovimiento[];
  constructor(){
    this.recursosAsignados = [];
    this.items = []
  }

}
