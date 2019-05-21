import {Agente} from "./agente.model";
import { TipoLocal } from "./tipolocal.model";
export class Proveedor implements Agente{
  tipoLocal: TipoLocal;
  nro: number;
  denominacion: string;
  nombre: string;
  email: string;
  direccion_nro: number;

}
