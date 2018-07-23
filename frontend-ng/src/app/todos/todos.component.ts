import {Component, OnInit} from '@angular/core';
import {Todo} from '../todo';
import {TodoService} from '../todo.service';

@Component({
  selector: 'app-todos',
  templateUrl: './todos.component.html',
  styleUrls: ['./todos.component.css']
})
export class TodosComponent implements OnInit {

  private _todos: Todo[];
  private _todoText: string;

  constructor(private _todoService: TodoService) {
  }

  ngOnInit() {
    this.todos = this.todoService.todos;
  }

  addTodo(text: string, event: Event): void {
    this.disableKey(event);
    if (this.todoService.addTodo(text, event)) {
      this.todoText = null;
    } else {
      console.log('failed to addTodo');
    }
  }

  updateTodo(todo: Todo, event: Event): void {
    this.disableKey(event);
    if (this.todoService.updateTodo(todo, event)) {

    } else {
      console.log('failed to updateTodo');
    }
  }

  // disables enter in textarea
  private disableKey(event: Event): void {
    event.stopPropagation();
    event.preventDefault();
    event.returnValue = false;
  }

  get todos(): Todo[] {
    return this._todos;
  }

  set todos(value: Todo[]) {
    this._todos = value;
  }

  get todoText(): string {
    return this._todoText;
  }

  set todoText(value: string) {
    this._todoText = value;
  }

  get todoService(): TodoService {
    return this._todoService;
  }

  set todoService(value: TodoService) {
    this._todoService = value;
  }
}
