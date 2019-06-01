import { Permiso } from "./abm/permiso.model";

export class Token {

  username: string;
  token: string;
  permisos: Permiso[];
}
