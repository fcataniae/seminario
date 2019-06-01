import { TipoMovimiento } from "./tipomovimiento.model";
import { Recurso } from "./recurso.model";
import { ItemMovimiento } from "./itemmovimiento.model";
import { TipoDocumento } from "./tipodocumento.model";
import { Estado } from "./estado.model";

export class Movimiento{
  id: number;
  tipoMovimiento : TipoMovimiento;
  origen: number;
  destino: number;
  recursosAsignados: Recurso[];
  itemMovimientos : ItemMovimiento[];
  tipoDocumento: TipoDocumento;
  nroDocumento: number;
  estado: Estado;

  constructor(){
    this.recursosAsignados = [];
    this.itemMovimientos = []
    this.tipoMovimiento = new TipoMovimiento();
  }

}
