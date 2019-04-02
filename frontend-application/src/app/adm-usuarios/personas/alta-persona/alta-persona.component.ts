import { Component, OnInit } from '@angular/core';
import { Persona } from './../../../model/persona.model';

@Component({
  selector: 'app-alta-persona',
  templateUrl: './alta-persona.component.html',
  styleUrls: ['./alta-persona.component.css']
})
export class AltaPersonaComponent implements OnInit {

  constructor() { }

  persona: Persona = new Persona();

  ngOnInit() {
  }

  onSubmit(){
    console.log("asdasd");
  console.log(this.persona);
  }

}
