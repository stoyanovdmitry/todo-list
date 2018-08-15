import {Injectable} from '@angular/core';
import {Todo} from './todo';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs/internal/Observable';
import {AuthorizationService} from './authorization.service';

@Injectable({
  providedIn: 'root'
})
export class TodoService {


  constructor(private _http: HttpClient, private _authorizationService: AuthorizationService) {
  }

  addTodo(text: string): Observable<Todo> {
    return this.http.post<Todo>(this.url, {
      'text': text
    }, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    });
  }

  getTodos(): Observable<Todo[]> {
    return this.http.get<Todo[]>(this.url);
  }

  updateTodo(todo: Todo): void {
    this.http.put(this.url + '/' + todo.id,
      todo, {
        headers: new HttpHeaders({
          'Content-Type': 'application/json'
        }),
        observe: 'response'
      }).subscribe(res => {
      console.log('todo successfully updated');
    });
  }

  deleteTodo(todo: Todo): void {
    this.http.delete(this.url + '/' + todo.id,
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json'
        }),
        observe: 'response'
      }).subscribe(res => {
      console.log('todo successfully deleted');
    });
  }

  get http(): HttpClient {
    return this._http;
  }

  set http(value: HttpClient) {
    this._http = value;
  }

  get url(): string {
    return 'http://localhost:8080/users/' + this._authorizationService.username + '/todos';
  }
}
