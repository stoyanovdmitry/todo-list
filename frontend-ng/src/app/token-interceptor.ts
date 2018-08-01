import {Injectable} from '@angular/core';
import {HttpClient, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';

import {Observable} from 'rxjs/internal/Observable';
import {AuthorizationService} from './authorization.service';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  constructor(private httpClient: HttpClient, private authenticationService: AuthorizationService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    console.log('TokenInterceptor');

    if (req.url.match('token/refresh')) {
      req = req.clone({
        setHeaders: {
          Authorization: AuthorizationService.getCookie('refreshToken')
        }
      });
    } else if (!req.url.match('token/login')) {
      req = req.clone({
        setHeaders: {
          Authorization: AuthorizationService.getCookie('accessToken')
        }
      });

      this.authenticationService.username = AuthorizationService.getCookie('username');
    }

    return next.handle(req);
  }
}
