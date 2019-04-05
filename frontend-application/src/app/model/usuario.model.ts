import { Persona } from './persona.model';
import { Estado } from './estado.model';
import { Rol } from './rol.model';



export class Usuario {

  nombreUsuario: string;
  password: string;
  persona: Persona;
  estado: Estado;
  roles: Rol[];

}
