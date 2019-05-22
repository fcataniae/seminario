import {TipoAgente} from "./tipoagente.model";
export class TipoMovimiento {
  id: number;
  nombre: string;
  tipo: string;
  tipoAgenteDestino: TipoAgente;
  tipoAgenteOrigen: TipoAgente;
}
