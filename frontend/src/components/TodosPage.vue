<template>
	<div id="app" class="p-3">
		<div class="row d-flex justify-content-center">
			<div class="text col-md-5 d-flex align-items-start
			border border-success border-top-0 border-left-0 border-right-0
			p-2 mt-2 pb-3">
				<button @click="addTodo(todoText)" class="mr-3 mt-2
				 d-flex justify-content-center align-items-center
				 btn-addnote"><span class="btn-addnote-symbol">+</span></button>
				<textarea @keydown.enter="addTodo(todoText, $event)" v-autosize="todoText"
						  class="new-todo-input border-0 mt-1" v-model="todoText" placeholder="Todo...">
				</textarea>
			</div>
		</div>
		<div class="row d-flex justify-content-center"
			 v-for="todo in todoDescOrder()"
			 v-if="!todo.completed">
			<div class="text col-md-5 d-flex align-items-start
			border border-primary border-top-0 border-left-0 border-right-0
			p-2 mt-2 pb-3">
				<!--<input @change="updateTodo(todo)" class="mr-2" type="checkbox" v-model="todo.completed">-->
				<div class="custom-control custom-checkbox">
					<input @change="updateTodo(todo)" class="custom-control-input" type="checkbox"
						   v-model="todo.completed" v-bind:id="todo.id">
					<label class="custom-control-label" v-bind:for="todo.id"></label>
				</div>
				<textarea @keydown.enter="disableKey"
						  @keyup="updateTodo(todo)"
						  v-autosize="todoText"
						  class="new-todo-input border-0 ml-3" v-model="todo.text" placeholder="Todo...">
				</textarea>
			</div>
		</div>
		<div class="row d-flex justify-content-center"
			 v-for="todo in todoDescOrder()"
			 v-if="todo.completed">
			<div class="ttext col-md-5 d-flex align-items-start
			border border-muted border-top-0 border-left-0 border-right-0
			p-2 mt-2 pb-3">
				<!--<input @change="updateTodo(todo)" class="mr-2" type="checkbox" v-model="todo.completed">-->
				<div class="custom-control custom-checkbox">
					<input @change="updateTodo(todo)" class="custom-control-input" type="checkbox"
						   v-model="todo.completed" v-bind:id="todo.id">
					<label class="custom-control-label" v-bind:for="todo.id"></label>
				</div>
				<textarea @keydown.enter="disableKey"
						  @keyup="updateTodo(todo)"
						  v-autosize="todoText"
						  class="new-todo-input border-0 ml-3" v-model="todo.text" placeholder="Todo...">
				</textarea>
			</div>
		</div>
	</div>
</template>

<script>
	import {headers} from '../main';
	
	export default {
		name: 'TodosPage',
		data() {
			return {
				todos: this.$parent.todos,
				todoText: null
			}
		},
		methods: {
			todoDescOrder: function () {
				if (this.todos === undefined) {
					return;
				}
				return this.todos.slice().reverse();
			},
			loadTodos: function () {
				const app = this;
				const requestUrl = this.$parent.serverUrl + "/users/"
					+ this.$parent.username + '/todos';
				headers.set('Authorization', this.$store.getters.getAccessToken);
				
				fetch(requestUrl, {
					method: 'GET',
					headers: headers
				}).then(res => {
					if (res.status === 200) {
						res.json().then(body => {
							app.todos = body;
						})
					}
				}).catch(err => console.log(err.message))
			},
			addTodo: function (text, e) {
				if (e !== undefined) this.disableKey(e);
				const app = this;
				const requestUrl = this.$parent.serverUrl + "/users/"
					+ this.$parent.username + '/todos';
				
				fetch(requestUrl, {
					method: 'POST',
					body: JSON.stringify({text: text}),
					headers: headers
				}).then(res => {
					if (res.status === 200) {
						res.json().then(body => {
							app.todoText = null;
							app.todos.push(body);
							console.log(body);
							console.log('successful addTodo')
						});
					}
				}).catch(err => console.log(err.message))
			},
			updateTodo: function (todo, e) {
				if (e !== undefined) this.disableKey(e);
				if (todo.text === '') {
					this.deleteTodo(todo);
					return;
				}
				
				const app = this;
				const requestUrl = this.$parent.serverUrl + "/users/"
					+ this.$parent.username + '/todos/' + todo.id;
				
				fetch(requestUrl, {
					method: 'PUT',
					body: JSON.stringify(todo),
					headers: headers
				}).then(res => {
					if (res.status === 200) {
						console.log('Todo successfully updated')
					}
				}).catch(err => console.log(err.message))
			},
			deleteTodo: function (todo) {
				const app = this;
				const requestUrl = this.$parent.serverUrl + "/users/"
					+ this.$parent.username + '/todos/' + todo.id;
				
				fetch(requestUrl, {
					method: 'DELETE',
					headers: headers
				}).then(res => {
					if (res.status === 200) {
						let indexOfTodo = app.todos.indexOf(todo);
						app.todos.splice(indexOfTodo, 1);
						console.log('Todo successfully deleted');
					}
				}).catch(err => console.log(err.message))
			},
			disableKey: function (e) {
				e.stopPropagation();
				e.preventDefault();
				e.returnValue = false;
			}
		},
		created() {
			if (this.$store.getters.isAuthenticated) {
				this.loadTodos();
			}
		}
	}
</script>

<style>
	.new-todo-input {
		outline: 0;
		resize: none;
		height: 34px;
		width: 100%;
	}
	.btn-addnote {
		width: 1.5rem;
		height: 1.5rem;
		border-radius: 50%;
		border: 0px;
		background: #5cb85c;
		outline: none !important;
	}
	.btn-addnote:hover {
		opacity: .9;
	}
	.btn-addnote-symbol {
		font-size: 1.45em;
		position: relative;
		top: -.55rem;
		color: white;
	}
	.custom-checkbox .custom-control-input:checked ~ .custom-control-label::before {
		background: #dee2e6;
	}
	.custom-control-label::after {
		top: .5rem;
		left: .25rem;
		background-size: 80% 80%;
	}
	.custom-checkbox .custom-control-label::before {
		border-radius: 1rem;
		/*top: .5rem;*/
		width: 1.5rem;
		height: 1.5rem;
	}
	.custom-checkbox .custom-control-label:hover::before {
		border: 2px solid #007bff;
		opacity: .9;
	}
	.custom-control-label::before {
		background: none;
		border: 2px solid #dee2e6;
	}
</style>
