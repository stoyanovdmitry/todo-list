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

    let token;

    if (req.url.match('token/refresh')) {
      token = AuthorizationService.getCookie('refreshToken');
    } else if (!req.url.match('token/login')) {
      token = AuthorizationService.getCookie('accessToken');
    }

    if (token !== undefined) {
      req = req.clone({
        setHeaders: {
          Authorization: token
        }
      });
      // this.authenticationService.username = AuthorizationService.getCookie('username');
    }

    return next.handle(req);
  }
}
