import { TipoAgente } from "./tipoagente.model";

export interface Agente{
  nro:number;
  denominacion:string;
  nombre:string;
  email:string;
  direccion_nro:number;
  tipoAgente : TipoAgente;
}
