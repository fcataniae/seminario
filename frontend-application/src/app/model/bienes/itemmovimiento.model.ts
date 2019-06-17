import { Bien } from "./bien.model";
import { TipoDocumentoBien } from "./tipodocumentobien.model";
import { Estado } from "./estado.model";

export class ItemMovimiento{
  bienIntercambiable: Bien;
  cantidad: number;
  itemMovimientoTipoDoc: TipoDocumentoBien[];
  estadoRecurso: Estado;
  precio: number;

  constructor(){
    this.bienIntercambiable = new Bien();
    this.itemMovimientoTipoDoc = [];
  }

}
