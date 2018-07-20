import {Injectable} from '@angular/core';
import {Todo} from './todo';

@Injectable({
  providedIn: 'root'
})
export class TodoService {

  private _todos: Todo[];

  constructor() {
    this._todos = [
      new Todo(1, 'text1', false),
      new Todo(2, 'text2', true),
      new Todo(3, 'text3', false),
      new Todo(4, 'text4', true),
      new Todo(5, 'text5', false),
    ];
  }

  addTodo(text: string, event: Event): boolean {
    this.todos.push(new Todo(this.todos.length + 1, text, false));
    return true;
  }

  get todos(): Todo[] {
    return this._todos;
  }

  set todos(value: Todo[]) {
    this._todos = value;
  }
}
