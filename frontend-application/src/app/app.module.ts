import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule }    from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { HTTP_INTERCEPTORS } from '@angular/common/http';


import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';


import { LoginService } from './services/login.service';
import { AuthInterceptor } from './services/auth.interceptor';
import { SessionService } from './services/session.service';
import { PersonaService } from './services/persona.service';
import { UsuarioService } from './services/usuario.service';


import { AppRoutingModule } from './app-routing.module';
import { HomeComponent } from './home/home.component';
import { GestionPersonasComponent } from './adm-usuarios/personas/gestion-personas/gestion-personas.component';
import { AltaPersonaComponent } from './adm-usuarios/personas/alta-persona/alta-persona.component';
import { ModificarPersonaComponent } from './adm-usuarios/personas/modificar-persona/modificar-persona.component';
import { EliminarPersonaComponent } from './adm-usuarios/personas/eliminar-persona/eliminar-persona.component';
import { GestionUsuariosComponent } from './adm-usuarios/usuarios/gestion-usuarios/gestion-usuarios.component';
import { AltaUsuarioComponent } from './adm-usuarios/usuarios/alta-usuario/alta-usuario.component';
import { ModificarUsuarioComponent } from './adm-usuarios/usuarios/modificar-usuario/modificar-usuario.component';
import { EliminarUsuarioComponent } from './adm-usuarios/usuarios/eliminar-usuario/eliminar-usuario.component';
import { GestionRolesComponent } from './adm-usuarios/roles/gestion-roles/gestion-roles.component';
import { AltaRolComponent } from './adm-usuarios/roles/alta-rol/alta-rol.component';
import { EditarRolComponent } from './adm-usuarios/roles/editar-rol/editar-rol.component';
import { ModificarRolComponent } from './adm-usuarios/roles/modificar-rol/modificar-rol.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    GestionPersonasComponent,
    AltaPersonaComponent,
    ModificarPersonaComponent,
    EliminarPersonaComponent,
    GestionUsuariosComponent,
    AltaUsuarioComponent,
    ModificarUsuarioComponent,
    EliminarUsuarioComponent,
    GestionRolesComponent,
    AltaRolComponent,
    EditarRolComponent,
    ModificarRolComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    CommonModule,
    AppRoutingModule
  ],
  providers: [
    LoginService,
    UsuarioService,
    PersonaService,
    SessionService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
