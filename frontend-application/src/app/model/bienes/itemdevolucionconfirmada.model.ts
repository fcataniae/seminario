import { TipoDocumentoBien } from "./tipodocumentobien.model";

export class ItemDevolucionConfirmada {

  posicion: number;
  idDocumento: number;
  tipoDocumento: TipoDocumentoBien;
  fecha: Date;
  estado: string;

  construct(){
    this.fecha = new Date();
  }

}

