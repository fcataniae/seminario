import { Component, OnInit, ElementRef, ViewChild  } from '@angular/core';
import { Rol } from '../../../model/rol.model';
import { Permiso } from '../../../model/permiso.model';
import { Router, ActivatedRoute } from '@angular/router';
import { RolService } from './../../../services/rol.service';
import { PermisoService } from './../../../services/permiso.service';
import {forkJoin} from 'rxjs';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-modificar-rol',
  templateUrl: './modificar-rol.component.html',
  styleUrls: ['./modificar-rol.component.css']
})
export class ModificarRolComponent implements OnInit {


  constructor(private _router: Router,
              private _route: ActivatedRoute,
              private _rolService: RolService,
              private _permisoService: PermisoService) { }

  @ViewChild('PermisosNo') selectPN: ElementRef;
  @ViewChild('PermisosSi') selectPS: ElementRef;

  rol : Rol;
  permisosAsignados: Permiso[];
  permisosNoAsignados: Permiso[];

  ngOnInit() {
    this.rol = new Rol();
    this._route.paramMap.subscribe(params => {

      let id = params.get("id");

      const observable1 = this._rolService.getRolByName(id).pipe(map(res => res));
      const observable2 = this._permisoService.getAllPermisos().pipe(map(res => res));

      forkJoin(observable1,
               observable2)
               .subscribe(([res1,res2]) => {
                 this.rol = res1;

                 let permisos = this.rol.permisos;
                 this.permisosNoAsignados = res2.filter(function(array){
                   return permisos.filter( function(array2){
                     return array2.id == array.id;
                   }).length == 0
                 });

                 this.permisosAsignados = permisos;

                 console.log(this.permisosAsignados);
                 console.log(this.permisosNoAsignados);
               },

               error => {
                 console.log(error);
               }

            );
        },
        error => {
          console.log(error);
        }
      );
    }

    onSubmit(){
      this.rol.permisos = this.permisosAsignados;
      this._rolService.updateRol(this.rol).subscribe(
        res => {
          console.log(res);
          alert('Se actualizo el rol correctamente');
          this._router.navigate(['/home/gestion/roles']);
        },
        error => {
          console.log(error);
          alert('No se pudo actualizar el rol');
        }
      );

    }

    asignarPermiso(event: Event){
      event.preventDefault();
      console.log("asigna");
      console.log(this.selectPN);
      if(this.selectPN.nativeElement.selectedIndex != -1){
        var selected = this.selectPN.nativeElement.options[this.selectPN.nativeElement.selectedIndex].innerHTML;
        console.log(selected);
        if(selected) {
          let rol = this.permisosNoAsignados.find(function(element){
            return element.nombre == selected;
          });
          console.log(rol);
          this.permisosNoAsignados.splice(this.permisosNoAsignados.indexOf(rol),1);
          this.permisosAsignados.push(rol);
        }
      }
    }

    desasignarPermiso(event: Event){
      event.preventDefault();
      console.log("desasigna");
      console.log(this.selectPS);
      if(this.selectPS.nativeElement.selectedIndex != -1){
        var selected = this.selectPS.nativeElement.options[this.selectPS.nativeElement.selectedIndex].innerHTML;
        console.log(selected);
        if(selected) {
          let rol = this.permisosAsignados.find(function(element){
            return element.nombre == selected;
          });
          console.log(rol);
          this.permisosAsignados.splice(this.permisosAsignados.indexOf(rol),1);
          this.permisosNoAsignados.push(rol);
        }
      }
    }
}
