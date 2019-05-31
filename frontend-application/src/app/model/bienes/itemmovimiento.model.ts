import { Bien } from "./bien.model";
import { TipoDocumentoBien } from "./tipodocumentobien.model";
import { Estado } from "./estado.model";

export class ItemMovimiento{
  bienIntercambiable: Bien;
  cantidad: number;
  itemMovimientoTipoDoc: TipoDocumentoBien[];
  estadoRecurso: Estado;

  constructor(){
    this.bienIntercambiable = new Bien();
    this.itemMovimientoTipoDoc = [];
  }

}
