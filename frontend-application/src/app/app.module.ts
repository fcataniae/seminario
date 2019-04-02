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

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    GestionPersonasComponent,
    AltaPersonaComponent,
    ModificarPersonaComponent,
    EliminarPersonaComponent
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
