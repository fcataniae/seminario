import {TipoAgente} from "./tipoagente.model";
import { TipoDocumento } from "./tipodocumento.model";
export class TipoMovimiento {

  id: number;
  nombre: string;
  tipo: string;
  tipoAgenteDestino: TipoAgente;
  tipoAgenteOrigen: TipoAgente;
  tipoDocumentos: TipoDocumento[];
}
