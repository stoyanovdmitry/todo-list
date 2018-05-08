<template>
	<div id="app" class="p-3">
		<div class="row d-flex justify-content-center">
			<div class="text col-md-5
			border border-success border-top-0 border-left-0 border-right-0
			p-2 mt-2 pb-3">
				<button v-on:click="addTodo(todoText)" class="mr-2 btn btn-success rounded-circle">+</button>
				<input v-on:keyup.enter="addTodo(todoText)"  type="text" class="new-todo-input border-0" v-model="todoText" placeholder="Todo...">
			</div>
		</div>
		<div class="row d-flex justify-content-center" v-for="todo in todoDescOrder()" v-if="!todo.completed">
			<div class="text col-md-5
			border border-primary border-top-0 border-left-0 border-right-0
			p-2 mt-2 pb-3">
				<input class="mr-2" type="checkbox" v-model="todo.completed">
				<span>{{todo.text}}</span>
			</div>
		</div>
		<div class="row d-flex justify-content-center" v-for="todo in todoDescOrder()" v-if="todo.completed">
			<div class="text col-md-5
			border border-muted border-top-0 border-left-0 border-right-0
			p-2 mt-2 pb-3">
				<input class="mr-2" type="checkbox" v-model="todo.completed">
				<span class="text-muted">{{todo.text}}</span>
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
			todoDescOrder: function() {
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
			addTodo: function (text) {
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
	}
</style>
