import { Component, OnInit } from '@angular/core';
import { PersonaService } from './../../../services/persona.service';
import { Persona } from './../../../model/persona.model';
import { Router } from '@angular/router';


@Component({
  selector: 'app-eliminar-persona',
  templateUrl: './eliminar-persona.component.html',
  styleUrls: ['./eliminar-persona.component.css']
})
export class EliminarPersonaComponent implements OnInit {

  constructor(private _personaService: PersonaService,
              private _router: Router) { }

  documentoBusqueda: number;
  persona: Persona;
  personaCargada: boolean;

  ngOnInit() {
    this.personaCargada = false;
  }


  buscarPersona(event: Event){
      event.preventDefault();

      this._personaService.getPersonaByDocumento(this.documentoBusqueda)
        .subscribe(
          res => {
            this.persona = res;
            this.personaCargada = true;
          },
          error => {
            alert('No se encontro una persona dada de alta con ese documento: ' + error);
            this.documentoBusqueda = null;
          }
        );
  }

  onDelete(event: Event){
      event.preventDefault();

      this._personaService.deletePersona(this.persona).subscribe(
        res => {
          alert('Se elimino la persona correctamente');
          this.personaCargada = false;
          this.persona = null;
          this._router.navigate(['/home/gestion/personas']);
        },
        error => {
          alert('No se pudo eliminar la persona: '+error);
        }
      );
  }
}
