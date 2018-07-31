import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {RouterModule, Routes} from '@angular/router';

import {AppComponent} from './app.component';
import {TodosComponent} from './todos/todos.component';
import {AuthorizationComponent} from './authorization/authorization.component';

const routes: Routes = [
  {path: '', component: TodosComponent},
  {path: 'login', component: AuthorizationComponent},
];

@NgModule({
  declarations: [
    AppComponent,
    TodosComponent,
    AuthorizationComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule.forRoot(routes)
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
