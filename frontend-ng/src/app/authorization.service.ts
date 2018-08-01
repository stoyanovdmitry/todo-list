import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthorizationService {

  private loginUrl = 'http://localhost:8080/token/login';
  private refreshUrl = 'http://localhost:8080/token/refresh';

  private _username: string;

  constructor(private httpClient: HttpClient, private router: Router) {
  }

  login(username: string, password: string) {
    this.httpClient.post(this.loginUrl, {
      'username': username,
      'password': password
    }, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      }),
      observe: 'response'
    }).subscribe(res => {
      const accessToken = res.headers.get('Access-Token');
      const refreshToken = res.headers.get('Refresh-Token');

      this._username = username;
      this.fillCookies(username, accessToken, refreshToken);
      this.router.navigate(['']);
    });
  }

  logout() {
    this.router.navigate(['login']);
  }

  signUp() {

  }

  refreshToken() {
    this.httpClient.post(this.refreshUrl, {}).subscribe();
  }

  private fillCookies(username: string, accessToken: string, refreshToken: string): void {
    const twoWeeks = new Date(new Date().getTime() + 1209600 * 1000);
    document.cookie = 'username=' + username + '; path=/; expires=' + twoWeeks.toUTCString();
    document.cookie = 'accessToken=' + accessToken + '; path=/; expires=' + twoWeeks.toUTCString();
    document.cookie = 'refreshToken=' + refreshToken + '; path=/; expires=' + twoWeeks.toUTCString();
  }

  static getCookie(value: string): string {
    const matches = document.cookie.match(new RegExp(
      '(?:^|; )' + value.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + '=([^;]*)'
    ));
    return matches ? decodeURIComponent(matches[1]) : undefined;
  }

  set username(value: string) {
    this._username = value;
  }

  get username(): string {
    return this._username;
  }
}
