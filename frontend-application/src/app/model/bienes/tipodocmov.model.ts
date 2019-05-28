import { TipoDocumento } from "./tipodocumento.model";
import { TipoMovimiento } from "./tipomovimiento.model";

export class TipoMovDoc{
  tipoMovimiento: TipoMovimiento;
  tipoDocumento: TipoDocumento[];
  id: number;
}
