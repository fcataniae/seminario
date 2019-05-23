import { Bien } from "./bien.model";
import { TipoDocumentoBien } from "./tipodocumentobien.model";

export class ItemMovimiento{
  bien: Bien;
  cantidad: number;
  itemMovimientoTipoDoc: TipoDocumentoBien[];
  vacio: boolean;

  constructor(){
    this.bien = new Bien();
    this.itemMovimientoTipoDoc = [];
  }

}
