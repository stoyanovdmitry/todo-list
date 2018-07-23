import {Injectable} from '@angular/core';
import {Todo} from './todo';
import {HttpClient, HttpHeaders} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class TodoService {

  // const headers = new HttpHeaders();
  // headers.set('Content-Type', 'application/json');
  // headers.set('Accept', 'application/json');

  private _todos: Todo[];
  private url = 'http://localhost:8080/users/user/todos';

  constructor(private _http: HttpClient) {
    this._todos = [
      new Todo(1, 'text1', false),
      new Todo(2, 'text2', true),
      new Todo(3, 'text3', false),
      new Todo(4, 'text4', true),
      new Todo(5, 'text5', false),
    ];
  }

  addTodo(text: string, event: Event): boolean {
    // Todo todo = new Todo(this.todos.length + 1, text, false);
    // this.http.post(url + '/users/user/todos', todo,);
    this.todos.push(new Todo(this.todos.length + 1, text, false));
    return true;
  }

  updateTodo(todo: Todo, event: Event): boolean {
    return true;
  }

  getTodos(): void {
    this.http.get(this.url).subscribe((data: Todo[]) => {
        this.todos = data;
        console.log(data);
      },
      err => console.error(err)
    );
  }

  get todos(): Todo[] {
    this.getTodos();
    return this._todos;
  }

  set todos(value: Todo[]) {
    this._todos = value;
  }

  get http(): HttpClient {
    return this._http;
  }

  set http(value: HttpClient) {
    this._http = value;
  }
}
