
import { Permiso } from './permiso.model';

import { Estado } from './estado.model';

export class Rol{

  id:number;
  nombre:string;
  descripcion:string;
  estado: Estado;
  permisos[]:Permiso;
}
