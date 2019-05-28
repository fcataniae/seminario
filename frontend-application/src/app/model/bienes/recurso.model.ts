import { TipoRecurso } from "./tiporecurso.model";
import { Estado } from "./estado.model";

export class Recurso {
  id: number;
  nroRecurso: number;
  tipoRecurso: TipoRecurso;
  estadoRecurso: Estado;
  
}
