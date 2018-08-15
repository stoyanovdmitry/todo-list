import {Injectable} from '@angular/core';
import {HttpClient, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';

import {Observable} from 'rxjs/internal/Observable';
import {throwError} from 'rxjs/internal/observable/throwError';
import {catchError} from 'rxjs/operators';
import {AuthorizationService} from './authorization.service';
import {Router} from '@angular/router';

@Injectable()
export class UnauthorizedInterceptor implements HttpInterceptor {

  constructor(private httpClient: HttpClient, private authenticationService: AuthorizationService, private router: Router) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    console.log('unauth-interceptor');
    return next.handle(req).pipe(catchError(err => {
      if (err.status === 401) {
        if (err.url.match('token/refresh')) {
          this.authenticationService.logout();
        } else if (err.url.match('token/login')) {
          this.authenticationService.invalidCredentials = true;
        } else {
          this.authenticationService.refreshToken();
        }
      }

      const error = err.message || err.statusText;
      return throwError(error);
    }));
  }
}
