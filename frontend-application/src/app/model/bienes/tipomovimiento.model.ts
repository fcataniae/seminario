import {TipoLocal} from "./tipolocal.model";
export class TipoMovimiento {
  id: number;
  nombre: string;
  tipo: string;
  tipoLocalDestino: TipoLocal;
  tipoLocalOrigen: TipoLocal;
}
