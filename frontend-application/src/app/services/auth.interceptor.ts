import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { catchError } from 'rxjs/operators';
import { of, throwError } from 'rxjs';
import { MatDialog } from '@angular/material';
import { ConfirmacionPopupComponent } from '../adm-usuarios/confirmacion-popup/confirmacion-popup.component';
import { Router } from '@angular/router';


@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private _matdialog: MatDialog, private _router : Router){}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    if(sessionStorage.getItem('Auth'))
      request = request.clone({
        setHeaders: {
          Authorization: sessionStorage.getItem('Auth')
        }
      });
      return next.handle(request).pipe(catchError((err, caught) => {
        if (err instanceof HttpErrorResponse) {
          if (err.status === 500) {
            console.log(err);
            console.log(caught);
            let dialog = this._matdialog.open(ConfirmacionPopupComponent,{
              data: {mensaje:"Ocurrio un error inesperado: " + err.error.message, titulo: "Error:",error: true}
            });
            dialog.afterClosed().subscribe();
          }else if (err.status === 403){
            console.log(window.location.href);
            if(!window.location.href.includes('login')){
              let dialog = this._matdialog.open(ConfirmacionPopupComponent,{
                data: {mensaje:"No se pudo autenticar al usuario!",error: true}
              });
              dialog.afterClosed().subscribe( res => this._router.navigate(['/login']));
            }else{
              return throwError("Error al autenticar el usuario");
            }
          }
        }
        return of(err);
      }) as any);
  }
}
