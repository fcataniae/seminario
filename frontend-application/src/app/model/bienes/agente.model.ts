import { TipoLocal } from "./tipolocal.model";

export interface Agente{
  nro:number;
  denominacion:string;
  nombre:string;
  email:string;
  direccion_nro:number;
  tipoLocal : TipoLocal;
}
