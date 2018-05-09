<template>
	<div class="app">
		<div class="row">
			<div class="col-sm-5 ml-auto mr-auto border auth-block">
				<form class="p-3" @submit.prevent="login">
					<div class="form-group">
						<label for="username">Username:</label>
						<input v-model="username" type="text" class="form-control" id="username" required>
					</div>
					<div class="form-group">
						<label for="pwd">Password:</label>
						<input v-model="password" type="password" class="form-control" id="pwd" required>
					</div>
					<div class="form-group">
						<span v-if="wrongAuth" class="text-danger">Something went wrong. Maybe username, maybe password, maybe something else, who knows?</span>
					</div>
					<div class="form-check">
						<div class="btn-group">
							<button class="btn btn-group btn-primary" type="submit">
								<span>Login</span>
							</button>
							<button type="button" @click="register" class="btn btn-group btn-muted">
								<span>Sign Up</span>
							</button>
						</div>
						<!--<label class="form-check-label ml-3">-->
						<!--<input v-model="rememberMe" class="form-check-input" type="checkbox"> Remember me-->
						<!--</label>-->
					</div>
				</form>
			</div>
		</div>
	</div>
</template>

<script>
	import {headers} from '../main';
	import {Auth} from '../main';
	
	export default {
		name: "AuthPage",
		data() {
			return {
				username: null,
				password: null,
				wrongAuth: false
			}
		},
		methods: {
			login: function () {
				const requestUrl = this.$parent.serverUrl + '/token/login';
				
				fetch(requestUrl, {
					method: 'POST',
					headers: headers,
					body: JSON.stringify({
						username: this.username,
						password: this.password
					})
				}).then(res => {
					if (res.ok) {
						let accessToken = res.headers.get('access-token');
						let refreshToken = res.headers.get('refresh-token');
						
						Auth.username = this.username;
						Auth.accessToken = accessToken;
						Auth.refreshToken = refreshToken;
						Auth.fillCookie();
						
						this.username = null;
						this.password = null;
						this.wrongAuth = false;
					} else {
						this.wrongAuth = true;
					}
				}).catch(err => {
					console.log(err.message);
					this.wrongAuth = true;
				})
			},
			register: function () {
				const requestUrl = this.$parent.serverUrl + '/users';
				
				fetch(requestUrl, {
					method: 'POST',
					headers: headers,
					body: JSON.stringify({
						username: this.username,
						password: this.password
					}),
				}).then(res => {
					if (res.ok) {
						this.login();
					} else {
						this.wrongAuth = true;
					}
				}).catch(err => {
					console.log(err.message);
					this.wrongAuth = true;
				})
			}
		}
	}
</script>

<style scoped>

</style>