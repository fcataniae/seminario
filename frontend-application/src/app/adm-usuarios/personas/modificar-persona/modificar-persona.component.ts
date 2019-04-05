import { Component, OnInit } from '@angular/core';
import { PersonaService } from './../../../services/persona.service';
import { Persona } from './../../../model/persona.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-modificar-persona',
  templateUrl: './modificar-persona.component.html',
  styleUrls: ['./modificar-persona.component.css']
})
export class ModificarPersonaComponent implements OnInit {

  constructor(private _personaService: PersonaService,
              private _router: Router) { }

  personaCargada: boolean;
  persona: Persona;
  documentoBusqueda: number;

  ngOnInit() {
    this.personaCargada = false;
    this.persona = new Persona();
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

  onModificar(event: Event){
      event.preventDefault();

      this._personaService.modificarPersona(this.persona).subscribe(
        res => {
                alert('Se modifico la persona correctamente');
                this._router.navigate(['/home/gestion/personas']);
              },
        error => {alert('No se pudo actualizar los datos de la persona: '+error)}
      );
    }
}
