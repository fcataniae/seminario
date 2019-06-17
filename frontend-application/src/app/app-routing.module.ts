import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent }      from './login/login.component';
import { HomeComponent }      from './home/home.component';
import { GestionPersonasComponent } from './adm-usuarios/personas/gestion-personas/gestion-personas.component';
import { EditarUsuarioComponent } from './adm-usuarios/usuarios/editar-usuario/editar-usuario.component';
import { EditarRolComponent } from './adm-usuarios/roles/editar-rol/editar-rol.component';
import { MovimientosComponent } from './bienes/movimientos/movimientos.component';
import { EnvioComponent } from './bienes/envio/envio.component';
import { GestionMovimientosComponent } from './bienes/gestion-movimientos/gestion-movimientos.component';
import { InformeComponent } from './bienes/informe/informe.component';
import { TablaStockComponent } from './bienes/tabla-stock/tabla-stock.component';
import { TablaDeudasComponent } from './bienes/tabla-deudas/tabla-deudas.component';
import { InformeMovimientosComponent } from './bienes/informe-movimientos/informe-movimientos.component';
import { InformeStockComponent } from './bienes/informe-stock/informe-stock.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'home', component: HomeComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full'},
  { path: 'home/adm/personas', component: GestionPersonasComponent},
  { path: 'home/adm/usuarios', component: EditarUsuarioComponent},
  { path: 'home/adm/roles', component: EditarRolComponent},
  { path: 'home/movimientos', component: GestionMovimientosComponent},
  { path: 'home/movimientos/registrar', component: MovimientosComponent},
  { path: 'home/movimientos/registrar/:mov', component: EnvioComponent},
  { path: 'home/movimientos/modificar/:mov', component: EnvioComponent},
  { path: 'home/informe/bienes', component: InformeComponent},
  { path: 'home/informe/movimientos', component: InformeMovimientosComponent},
  { path: 'home/informe/stock', component: InformeStockComponent},
  { path: 'home/informe/deudas', component: TablaDeudasComponent}

];
@NgModule({
  imports: [ RouterModule.forRoot(routes, { useHash: true }) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
