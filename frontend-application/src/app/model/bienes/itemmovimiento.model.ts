import { Bien } from "./bien.model";
import { TipoDocumentoBien } from "./tipodocumentobien.model";

export class ItemMovimiento{
  bienIntercambiable: Bien;
  cantidad: number;
  itemMovimientoTipoDoc: TipoDocumentoBien[];
  vacio: boolean;

  constructor(){
    this.bienIntercambiable = new Bien();
    this.itemMovimientoTipoDoc = [];
  }

}
