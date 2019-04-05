import { Component, OnInit } from '@angular/core';
import { Persona } from './../../../model/persona.model';
import { Router } from '@angular/router';
import { PersonaService } from './../../../services/persona.service';

@Component({
  selector: 'app-alta-persona',
  templateUrl: './alta-persona.component.html',
  styleUrls: ['./alta-persona.component.css']
})
export class AltaPersonaComponent implements OnInit {

  constructor(private _personaService: PersonaService,
              private _router: Router) { }

  persona: Persona;

  ngOnInit() {
    this.persona = new Persona();
  }

  onSubmit(){
    this._personaService.createPersona(this.persona).
      subscribe(
        res => {
          console.log(res);
          alert(res);
          this._router.navigate(['/home/gestion/personas']);

        },
        error => {console.log(error);}
      )
  }

}
