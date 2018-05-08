<template>
	<div id="app" class="p-3">
		<div class="row d-flex justify-content-center">
			<div class="text col-md-5 d-flex align-items-start
			border border-success border-top-0 border-left-0 border-right-0
			p-2 mt-2 pb-3">
				<button @click="addTodo(todoText)" class="mr-2 btn btn-success rounded-circle">+</button>
				<textarea @keydown.enter="addTodo(todoText, $event)" v-autosize="todoText"
						  class="new-todo-input border-0 mt-1" v-model="todoText" placeholder="Todo...">
				</textarea>
			</div>
		</div>
		<div class="row d-flex justify-content-center" v-for="todo in todoDescOrder()" v-if="!todo.completed">
			<div class="text col-md-5 d-flex align-items-start
			border border-success border-top-0 border-left-0 border-right-0
			p-2 mt-2 pb-3">
				<input @change="updateTodo(todo)" class="mr-2" type="checkbox" v-model="todo.completed">
				<textarea @keydown.enter="disableEnter"
						  @keyup="updateTodo(todo)"
						  v-autosize="todoText"
						  class="new-todo-input border-0" v-model="todo.text" placeholder="Todo...">
				</textarea>
			</div>
		</div>
		<div class="row d-flex justify-content-center" v-for="todo in todoDescOrder()" v-if="todo.completed">
			<div class="ttext col-md-5 d-flex align-items-start
			border border-success border-top-0 border-left-0 border-right-0
			p-2 mt-2 pb-3">
				<input @change="updateTodo(todo)" class="mr-2" type="checkbox" v-model="todo.completed">
				<textarea @keydown.enter="disableEnter"
						  @keyup="updateTodo(todo)"
						  v-autosize="todoText"
						  class="new-todo-input border-0" v-model="todo.text" placeholder="Todo...">
				</textarea>
			</div>
		</div>
	</div>
</template>

<script>
	const headers = new Headers();
	headers.append('Content-Type', 'application/json');
	headers.append('Accept', 'application/json');
	
	export default {
		name: 'app',
		data() {
			return {
				todos: this.$parent.todos,
				todoText: null
			}
		},
		methods: {
			todoDescOrder: function () {
				return this.todos.slice().reverse();
			},
			loadTodos: function () {
				const app = this;
				const requestUrl = this.$parent.serverUrl + "/users/"
					+ this.$parent.user.username + '/todos';
				
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
				if(e !== undefined) this.disableEnter(e);
				const app = this;
				const requestUrl = this.$parent.serverUrl + "/users/"
					+ this.$parent.user.username + '/todos';
				
				fetch(requestUrl, {
					method: 'POST',
					body: JSON.stringify({text: text}),
					headers: headers
				}).then(res => {
					res.json().then(body => {
						app.todoText = null;
						app.todos.push(body);
						console.log(body);
						console.log('successful addTodo')
					});
				}).catch(err => console.log(err.message))
			},
			updateTodo: function (todo, e) {
				if(e !== undefined) this.disableEnter(e);
				const app = this;
				const requestUrl = this.$parent.serverUrl + "/users/"
					+ this.$parent.user.username + '/todos/' + todo.id;
				
				fetch(requestUrl, {
					method: 'PUT',
					body: JSON.stringify(todo),
					headers: headers
				}).then(res => console.log('Todo successful updated'))
					.catch(err => console.log(err.message))
			},
			disableEnter: function (e) {
				e.stopPropagation();
				e.preventDefault();
				e.returnValue = false;
				this.input = e.target.value;
			}
		},
		beforeMount() {
			this.loadTodos();
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
</style>
