import { Component, OnInit } from '@angular/core';
import { Persona } from './../../../model/persona.model';

@Component({
  selector: 'app-alta-persona',
  templateUrl: './alta-persona.component.html',
  styleUrls: ['./alta-persona.component.css']
})
export class AltaPersonaComponent implements OnInit {

  constructor() { }

  persona: Persona;

  ngOnInit() {
    this.persona = new Persona();
  }

  onSubmit(){
    console.log("asdasd");
  console.log(this.persona);
  }

}
