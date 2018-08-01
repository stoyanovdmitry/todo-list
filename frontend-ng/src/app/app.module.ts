import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {RouterModule, Routes} from '@angular/router';

import {AppComponent} from './app.component';
import {TodosComponent} from './todos/todos.component';
import {AuthorizationComponent} from './authorization/authorization.component';
import {NotFoundComponent} from './not-found/not-found.component';
import {AuthorizationService} from './authorization.service';
import {TokenInterceptor} from './token-interceptor';
import {UnauthorizedInterceptor} from './unauthorized-interceptor';

const routes: Routes = [
  {path: '', component: TodosComponent},
  {path: 'login', component: AuthorizationComponent},
  {path: '**', component: NotFoundComponent},
];

@NgModule({
  declarations: [
    AppComponent,
    TodosComponent,
    AuthorizationComponent,
    NotFoundComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule.forRoot(routes)
  ],
  providers: [
    AuthorizationService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: UnauthorizedInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
