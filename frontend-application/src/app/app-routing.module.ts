import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent }      from './login/login.component';
import { HomeComponent }      from './home/home.component';
import { GestionPersonasComponent } from './adm-usuarios/personas/gestion-personas/gestion-personas.component';
import { AltaPersonaComponent } from './adm-usuarios/personas/alta-persona/alta-persona.component';
import { ModificarPersonaComponent } from './adm-usuarios/personas/modificar-persona/modificar-persona.component';
import { EliminarPersonaComponent } from './adm-usuarios/personas/eliminar-persona/eliminar-persona.component';
import { GestionUsuariosComponent } from './adm-usuarios/usuarios/gestion-usuarios/gestion-usuarios.component';
import { AltaUsuarioComponent } from './adm-usuarios/usuarios/alta-usuario/alta-usuario.component';
import { ModificarUsuarioComponent } from './adm-usuarios/usuarios/modificar-usuario/modificar-usuario.component';
import { EditarUsuarioComponent } from './adm-usuarios/usuarios/editar-usuario/editar-usuario.component';
import { GestionRolesComponent } from './adm-usuarios/roles/gestion-roles/gestion-roles.component';
import { AltaRolComponent } from './adm-usuarios/roles/alta-rol/alta-rol.component';
import { EditarRolComponent } from './adm-usuarios/roles/editar-rol/editar-rol.component';
import { ModificarRolComponent } from './adm-usuarios/roles/modificar-rol/modificar-rol.component';


const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'home', component: HomeComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full'},
  { path: 'home/gestion/personas', component: GestionPersonasComponent},
  { path: 'home/gestion/personas/alta', component: AltaPersonaComponent},
  { path: 'home/gestion/personas/baja', component: EliminarPersonaComponent},
  { path: 'home/gestion/personas/modi', component: ModificarPersonaComponent},
  { path: 'home/gestion/usuarios', component: GestionUsuariosComponent},
  { path: 'home/gestion/usuarios/alta', component: AltaUsuarioComponent},
  { path: 'home/gestion/usuarios/adm', component: EditarUsuarioComponent},
  { path: 'home/gestion/usuarios/adm/:id', component: ModificarUsuarioComponent},
  { path: 'home/gestion/roles', component: GestionRolesComponent},
  { path: 'home/gestion/roles/alta', component: AltaRolComponent},
  { path: 'home/gestion/roles/adm', component: EditarRolComponent},
  { path: 'home/gestion/roles/adm/:id', component: ModificarRolComponent}
];
@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
