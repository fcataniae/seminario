import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material';
import { Recurso } from '../../model/bienes/recurso.model';
import { MovimientoService } from '../../services/movimiento.service';
import { TipoRecurso } from '../../model/bienes/tiporecurso.model';

@Component({
  selector: 'app-agregar-recurso',
  templateUrl: './agregar-recurso.component.html',
  styleUrls: ['./agregar-recurso.component.css']
})
export class AgregarRecursoComponent implements OnInit {

  recurso: Recurso;
  recursos: Recurso[];
  tipoRecursos: TipoRecurso[];
  recursosFiltrados: Recurso[];
  tipoRecurso: TipoRecurso;

  constructor(private dialogRef: MatDialogRef<AgregarRecursoComponent>,
              private _movimientoService: MovimientoService) {

   }

  ngOnInit() {
    this.recurso = new Recurso();
    this._movimientoService.getAllRecursos().subscribe(
      res=> {
        console.log(res);
        this.recursos = res;
        this.tipoRecursos = [];
        //devuelvo un array de recursos filtrando por los tipo de recursos
        //para despues poder tomar esos recursos y mostrarlos en el combo
        let filtrarTipoRecursos = this.recursos.filter((valor,indice,array) =>{
          return array.findIndex( valor2 => valor2.tipoRecurso.id === valor.tipoRecurso.id) === indice;
        } );
        //pusheo lo que filtre
        filtrarTipoRecursos.forEach(r => {
          this.tipoRecursos.push(r.tipoRecurso);
        });
        //en el onchange tipo recurso tengo que filtrar el array de recursos
        console.log(this.tipoRecursos);
      },
      error => console.log(error)
    );
  }

  onChangeTipoRecurso(){
    console.log(this.tipoRecurso);
    this.recursosFiltrados = this.recursos.filter(r => r.tipoRecurso.id === this.tipoRecurso.id);
    console.log(this.recursosFiltrados);
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  onSubmit(){
    this.recurso.tipoRecurso = this.tipoRecurso;
    console.log('boolean' + (this.recurso instanceof Recurso));
    this.dialogRef.close(this.recurso);
  }

}
