import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
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
import { AltaUsuarioComponent } from './adm-usuarios/usuarios/alta-usuario/alta-usuario.component';
import { ModificarUsuarioComponent } from './adm-usuarios/usuarios/modificar-usuario/modificar-usuario.component';
import { EditarUsuarioComponent } from './adm-usuarios/usuarios/editar-usuario/editar-usuario.component';
import { AltaRolComponent } from './adm-usuarios/roles/alta-rol/alta-rol.component';
import { EditarRolComponent } from './adm-usuarios/roles/editar-rol/editar-rol.component';
import { ModificarRolComponent } from './adm-usuarios/roles/modificar-rol/modificar-rol.component';
import { ConfirmacionPopupComponent } from './adm-usuarios/confirmacion-popup/confirmacion-popup.component';
import { MatDialogModule,MatDialog} from '@angular/material/dialog';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatButtonModule, MatTableModule, MatIconModule, MatFormFieldModule, MatInputModule, MatPaginatorModule, MatSelectModule, MatCheckboxModule } from '@angular/material';
import { MatSortModule } from '@angular/material/sort';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    GestionPersonasComponent,
    AltaPersonaComponent,
    ModificarPersonaComponent,
    EliminarPersonaComponent,
    AltaUsuarioComponent,
    ModificarUsuarioComponent,
    EditarUsuarioComponent,
    AltaRolComponent,
    EditarRolComponent,
    ModificarRolComponent,
    ConfirmacionPopupComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    CommonModule,
    AppRoutingModule,
    MatDialogModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatTableModule,
    MatIconModule,
    MatSortModule,
    MatFormFieldModule,
    MatInputModule,
    MatPaginatorModule,
    ReactiveFormsModule,
    MatSelectModule,
    MatCheckboxModule
  ],
  providers: [
    LoginService,
    UsuarioService,
    PersonaService,
    SessionService,
    MatDialog,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent],
  entryComponents: [
    ConfirmacionPopupComponent,
    AltaRolComponent,
    ModificarUsuarioComponent,
    ModificarRolComponent,
    AltaUsuarioComponent
]
})
export class AppModule { }
