import {Injectable} from '@angular/core';
import {Todo} from './todo';
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root'
})
export class TodoService {

  private url = 'http://localhost:8080/users/user/todos';

  constructor(private _http: HttpClient) {
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
      // console.log(res);
      // console.log('----------------------------------------------------');
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
      // console.log(res);
      // console.log('----------------------------------------------------');
    });
  }

  get http(): HttpClient {
    return this._http;
  }

  set http(value: HttpClient) {
    this._http = value;
  }
}
