import { Component, OnInit } from '@angular/core';
import { Persona } from './../../../model/persona.model';

@Component({
  selector: 'app-modificar-persona',
  templateUrl: './modificar-persona.component.html',
  styleUrls: ['./modificar-persona.component.css']
})
export class ModificarPersonaComponent implements OnInit {

  constructor() { }

  personaCargada: boolean;
  persona: Persona;
  documentoBusqueda: number;

  ngOnInit() {
    this.personaCargada = false;
    this.persona = new Persona();
  }

  buscarPersona(){

  }

  onSubmit(){

  }
}
