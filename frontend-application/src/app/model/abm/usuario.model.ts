import { Persona } from './persona.model';
import { Estado } from './estado.model';
import { Rol } from './rol.model';
import { Local } from '../bienes/local.model';



export class Usuario {

  nombreUsuario: string;
  password: string;
  persona: Persona;
  estado: Estado;
  roles: Rol[];
  local: Local;

  constructor(){
    this.persona = new Persona();
  }
}
