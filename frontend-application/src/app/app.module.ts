import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule }    from '@angular/common/http';
import { CommonModule, LocationStrategy } from '@angular/common';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { APP_BASE_HREF } from '@angular/common';
import 'hammerjs';
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
import { AltaUsuarioComponent } from './adm-usuarios/usuarios/alta-usuario/alta-usuario.component';
import { ModificarUsuarioComponent } from './adm-usuarios/usuarios/modificar-usuario/modificar-usuario.component';
import { EditarUsuarioComponent } from './adm-usuarios/usuarios/editar-usuario/editar-usuario.component';
import { AltaRolComponent } from './adm-usuarios/roles/alta-rol/alta-rol.component';
import { EditarRolComponent } from './adm-usuarios/roles/editar-rol/editar-rol.component';
import { ModificarRolComponent } from './adm-usuarios/roles/modificar-rol/modificar-rol.component';
import { ConfirmacionPopupComponent } from './adm-usuarios/confirmacion-popup/confirmacion-popup.component';
import { MatDialogModule,MatDialog} from '@angular/material/dialog';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {
  MatNativeDateModule,
  MatButtonModule, MatTableModule, MatIconModule, MatFormFieldModule, MatInputModule, MatPaginatorModule,
  MatSelectModule, MatCheckboxModule, MatDatepickerModule, MatTabsModule, MatAutocompleteModule
} from '@angular/material';
import { MatSortModule } from '@angular/material/sort';
import { CustomLocationStrategy } from './app.common';
import { MovimientosComponent } from './bienes/movimientos/movimientos.component';
import { EnvioComponent } from './bienes/envio/envio.component';
import { AgregarBienComponent } from './bienes/agregar-bien/agregar-bien.component';
import { AgregarRecursoComponent } from './bienes/agregar-recurso/agregar-recurso.component';
import { ConfirmarMovimientoComponent } from './bienes/confirmar-movimiento/confirmar-movimiento.component';
import { GestionMovimientosComponent } from './bienes/gestion-movimientos/gestion-movimientos.component';
import { ValidpatentePipe } from './utils/validpatente.pipe';
import { ModificarBienComponent } from './bienes/modificar-bien/modificar-bien.component';
import { InformeComponent } from './bienes/informe/informe.component';
import { InformeTiendasComponent } from './bienes/informe-tiendas/informe-tiendas.component';
import { ExcelService } from './services/excel.service';
import { InformeMovimientosComponent } from './bienes/informe-movimientos/informe-movimientos.component';
import { VistaMovimientoComponent } from './bienes/vista-movimiento/vista-movimiento.component';
import { InformeStockComponent } from './bienes/informe-stock/informe-stock.component';
import {MatTooltipModule} from '@angular/material/tooltip';
import {MatExpansionModule} from '@angular/material/expansion';
import { InformeDeudasComponent } from './bienes/informe-deudas/informe-deudas.component';
import { PdfViewerComponent } from './pdf-viewer/pdf-viewer.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    GestionPersonasComponent,
    AltaPersonaComponent,
    ModificarPersonaComponent,
    AltaUsuarioComponent,
    ModificarUsuarioComponent,
    EditarUsuarioComponent,
    AltaRolComponent,
    EditarRolComponent,
    ModificarRolComponent,
    ConfirmacionPopupComponent,
    MovimientosComponent,
    EnvioComponent,
    AgregarBienComponent,
    AgregarRecursoComponent,
    ConfirmarMovimientoComponent,
    GestionMovimientosComponent,
    ValidpatentePipe,
    ModificarBienComponent,
    InformeComponent,
    InformeTiendasComponent,
    InformeMovimientosComponent,
    VistaMovimientoComponent,
    InformeStockComponent,
    InformeDeudasComponent,
    PdfViewerComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    MatTabsModule ,
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
    MatCheckboxModule,
    MatDatepickerModule,
    MatNativeDateModule,
    BrowserAnimationsModule,
    MatAutocompleteModule,
    MatTooltipModule,
    MatExpansionModule
  ],
  providers: [
    LoginService,
    UsuarioService,
    PersonaService,
    SessionService,
    ExcelService,
    MatDialog,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    {
            provide: APP_BASE_HREF,
            useValue: '/' + (window.location.pathname.split('/')[1] || 'desa')
    },
    { provide: LocationStrategy, useClass: CustomLocationStrategy }
  ],
  bootstrap: [AppComponent],
  entryComponents: [
    ConfirmacionPopupComponent,
    AltaRolComponent,
    ModificarUsuarioComponent,
    ModificarRolComponent,
    AltaUsuarioComponent,
    AltaPersonaComponent,
    ModificarPersonaComponent,
    AgregarBienComponent,
    AgregarRecursoComponent,
    ConfirmarMovimientoComponent,
    ModificarBienComponent,
    VistaMovimientoComponent
  ],
  exports: [
    ValidpatentePipe
  ]
})
export class AppModule { }
