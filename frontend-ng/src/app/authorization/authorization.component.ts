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
  private _invalidData = false;

  constructor(private authorizationService: AuthorizationService) {
  }

  ngOnInit() {
  }

  private login() {
    this.authorizationService.login(this.username, this.password);
  }

  private register() {
    console.log(this.username);
    console.log(this.password);
  }

  private validateData(): boolean {
    if (this.username !== '' && this.username !== undefined
      && this.password !== '' && this.password !== undefined) {
      this.invalidData = true;
      return false;
    }

    this.invalidData = false;
    return true;
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
    return this._invalidData;
  }

  set invalidData(value: boolean) {
    this._invalidData = value;
  }
}
