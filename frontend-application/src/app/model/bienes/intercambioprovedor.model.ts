import { Agente } from "./agente.model";
import { Bien } from "./bien.model";

export class IntercambioProv{
  proveedor: Agente;
  bienIntercambiableEntregado: Bien;
  bienIntercambiableRecibido: Bien;
  cantidadRecibida: number;
  cantidadEntregada: number;
  nombreIntercambio: string;

}
