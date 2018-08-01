import {Component} from '@angular/core';
import {AuthorizationService} from './authorization.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';

  constructor(private authorizationService: AuthorizationService) {
  }

  get username(): string {
    return this.authorizationService.username;
  }
}
