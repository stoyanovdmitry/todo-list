import {Component, OnInit} from '@angular/core';
import {AuthorizationService} from '../authorization.service';

@Component({
  selector: 'app-authorization',
  templateUrl: './authorization.component.html',
  styleUrls: ['./authorization.component.css']
})
export class AuthorizationComponent implements OnInit {

  private _username: string;
  private _password: string;

  constructor(private authorizationService: AuthorizationService) {
  }

  ngOnInit() {
  }

  private login() {
    this.authorizationService.login(this.username, this.password);
  }

  private register() {
    this.authorizationService.signUp(this.username, this.password);
  }

  private validateData(): boolean {
    return !(this.username !== '' && this.username !== undefined
      && this.password !== '' && this.password !== undefined);
  }

  get username(): string {
    return this._username;
  }

  set username(value: string) {
    this._username = value;
  }

  get password(): string {
    return this._password;
  }

  set password(value: string) {
    this._password = value;
  }

  get invalidData(): boolean {
    return this.authorizationService.invalidCredentials;
  }
}
