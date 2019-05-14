import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent }      from './login/login.component';
import { HomeComponent }      from './home/home.component';
import { GestionPersonasComponent } from './adm-usuarios/personas/gestion-personas/gestion-personas.component';
import { EditarUsuarioComponent } from './adm-usuarios/usuarios/editar-usuario/editar-usuario.component';
import { EditarRolComponent } from './adm-usuarios/roles/editar-rol/editar-rol.component';


const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'home', component: HomeComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full'},
  { path: 'home/adm/personas', component: GestionPersonasComponent},
  { path: 'home/adm/usuarios', component: EditarUsuarioComponent},
  { path: 'home/adm/roles', component: EditarRolComponent}
];
@NgModule({
  imports: [ RouterModule.forRoot(routes, { useHash: true }) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
