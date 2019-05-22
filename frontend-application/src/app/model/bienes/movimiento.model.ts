import { TipoMovimiento } from "./tipomovimiento.model";
import { Recurso } from "./recurso.model";

export class Movimiento{
  tipoMovimiento : TipoMovimiento;
  origen: number;
  destino: number;
  recursosAsignados: Recurso[];
  
}
