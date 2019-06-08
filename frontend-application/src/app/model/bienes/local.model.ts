import {Agente} from "./agente.model";
import { TipoAgente } from "./tipoagente.model";
import { StockBienEnLocal } from "./stockbienlocal.model";

export class Local implements Agente{
  tipoAgente: TipoAgente ;
  nro: number;
  denominacion: string;
  nombre: string;
  email: string;
  direccion_nro: number;
  direccion:string;
  stockBienes:StockBienEnLocal;
  coordenada: string;

}
