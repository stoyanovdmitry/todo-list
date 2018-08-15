import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthorizationService {

  private loginUrl = 'http://localhost:8080/token/login';
  private signUpUrl = 'http://localhost:8080/users';
  private refreshUrl = 'http://localhost:8080/token/refresh';
  private _invalidCredentials = false;

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
      this.invalidCredentials = false;
    });
  }

  logout() {
    this.username = null;
    this.clearCookies();
    this.router.navigate(['login']);
  }

  signUp(username: string, password: string) {
    this.httpClient.post(this.signUpUrl, {
      'username': username,
      'password': password
    }, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      }),
      observe: 'response'
    }).subscribe(res => {
      console.log(res.status);
      if (res.status === 200) {
        this.login(username, password);
      }
    },
      err => {
        console.log('cant signup');
        this.clearCookies();
        this.invalidCredentials = true;
      });
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

  private clearCookies() {
    const date = new Date(new Date().getTime() - 1);
    document.cookie = 'username=;' + 'expires=' + date.toUTCString();
    document.cookie = 'accessToken=;' + 'expires=' + date.toUTCString();
    document.cookie = 'refreshToken=;' + 'expires=' + date.toUTCString();
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

  get invalidCredentials(): boolean {
    return this._invalidCredentials;
  }

  set invalidCredentials(value: boolean) {
    this._invalidCredentials = value;
  }
}
