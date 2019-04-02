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
import { EliminarUsuarioComponent } from './adm-usuarios/usuarios/eliminar-usuario/eliminar-usuario.component';


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
  { path: 'home/gestion/usuarios/baja', component: EliminarUsuarioComponent},
  { path: 'home/gestion/usuarios/modi', component: ModificarUsuarioComponent}
];
@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
