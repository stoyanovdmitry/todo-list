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
    this.todoService.getTodos().subscribe(data => this.todos = data);
  }

  addTodo(text: string, event: KeyboardEvent): void {
    this.disableKey(event);

    if (this.todoText != null && this.todoText.trim() !== '') {
      this.todoService.addTodo(text.trim()).subscribe(data => {
        console.log(data);
        this.todos.push(data);
        this.todoText = null;
      });
    }
  }

  updateTodo(todo: Todo, event: KeyboardEvent): void {
    this.disableKey(event);
    if (todo.text === '') {
      this.todoService.deleteTodo(todo);
      const index = this.todos.indexOf(todo);
      this.todos.splice(index, 1);
    } else {
      this.todoService.updateTodo(todo);
    }
  }

  // disables enter in textarea
  // todo change this method to decorator
  private disableKey(event: KeyboardEvent): void {
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
