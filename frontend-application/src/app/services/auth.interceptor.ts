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
import { of } from 'rxjs';
import { MatDialog } from '@angular/material';
import { ConfirmacionPopupComponent } from '../adm-usuarios/confirmacion-popup/confirmacion-popup.component';


@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private _matdialog: MatDialog){}

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
          }
        }
        return of(err);
      }) as any);
  }
}
