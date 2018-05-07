<template>
	<div id="app">
		<div v-for="todo in todos">{{ todo.text }}</div>
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
			loadTodos: function (username) {
				const app = this;
				const requestUrl = this.$parent.serverUrl + "/users/" + username + '/todos';
				
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
			this.loadTodos('user');
		}
	}
</script>

<style>

</style>
