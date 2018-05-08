<template>
	<div id="app" class="p-3">
		<div class="row d-flex justify-content-center" v-for="todo in todos" v-if="!todo.completed">
			<div class="text col-md-5
			border border-primary border-top-0 border-left-0 border-right-0
			p-2 mt-2 pb-3">
				<input class="mr-2" type="checkbox" v-model="todo.completed">
				<span>{{todo.text}}</span>
			</div>
		</div>
		<div class="row d-flex justify-content-center" v-for="todo in todos" v-if="todo.completed">
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
	export default {
		name: 'app',
		data() {
			return {
				todos: this.$parent.todos
			}
		},
		methods: {
			loadTodos: function () {
				const app = this;
				const requestUrl = this.$parent.serverUrl + "/users/" + this.$parent.user.username + '/todos';
				
				fetch(requestUrl, {
					method: 'GET'
				}).then(res => {
					if (res.status === 200) {
						res.json().then(body => {
							app.todos = body;
						})
					}
				}).catch(err => console.log(err.message))
			}
		},
		beforeMount() {
			this.loadTodos();
		}
	}
</script>

<style>

</style>
