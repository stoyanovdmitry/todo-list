import {Component, OnInit} from '@angular/core';
import {Todo} from '../todo';
import {TodoService} from '../todo.service';
import {debounceTime} from "rxjs/operators";

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
    this.todoService.getTodos().subscribe(data => this.todos = data);
  }

  addTodo(text: string, event: Event): void {
    this.disableKey(event);

    this.todoService.addTodo(text).subscribe(data => {
      console.log(data);
      this.todos.push(data);
    });
  }

  updateTodo(todo: Todo, event: Event): void {
    this.disableKey(event);
    this.todoService.updateTodo(todo);
    // if (this.todoService.updateTodo(todo, event)) {
    //
    // } else {
    //   console.log('failed to updateTodo');
    // }
  }

  // disables enter in textarea
  // todo change this method to decorator
  private disableKey(event: Event): void {
    if (event.key === 'Enter') {
      event.stopPropagation();
      event.preventDefault();
      event.returnValue = false;
    }
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
