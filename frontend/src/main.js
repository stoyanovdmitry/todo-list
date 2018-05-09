import Vue from 'vue'
import VueRouter from 'vue-router'
import VueAutosize from 'vue-autosize'

export {
	headers,
	Auth
}

Vue.use(VueRouter);
Vue.use(VueAutosize);

// const mainPage = require('./components/NotFound').default;
const mainPage = require('./components/TodosPage').default;
const authPage = require('./components/AuthPage').default;

const headers = new Headers();
headers.append('Content-Type', 'application/json');
headers.append('Accept', 'application/json');


const router = new VueRouter({
	routes: [
		{
			path: '/', component: mainPage, meta: {
				requiresAuth: true
			}
		},
		{path: '/login', component: authPage}
	]
});


const Auth = {
	isAuthenticated: false,
	username: null,
	accessToken: null,
	refreshToken: null,
	tryToLogin: function () {
		if (!this.isAuthenticated) {
			this.isAuthenticated = true;
			this.username = this.getCookie('username');
			this.accessToken = this.getCookie('accessToken');
			this.refreshToken = this.getCookie('refreshToken');
			
			console.log(this.username);
			
			if (this.username === undefined || this.accessToken === undefined || this.refreshToken === undefined) {
				this.logout();
			} else {
				this.tryToRefresh();
			}
		}
		
		return this.isAuthenticated;
	},
	tryToRefresh: function () {
		const refreshUrl = serverUrl + '/token/refresh';
		headers.append('Authorization', this.refreshToken);
		
		fetch(refreshUrl, {
			method: 'POST',
			headers: headers
		}).then(res => {
			if (res.ok) {
				let accessToken = res.headers.get('access-token');
				let refreshToken = res.headers.get('refresh-token');
				
				this.accessToken = accessToken;
				this.refreshToken = refreshToken;
				this.fillCookie();
				
				app.username = this.username;
				app.callLoadTodos();
				
				console.log('refreshed')
			} else {
				this.logout();
				console.log('didnt refreshed')
			}
		}).catch(err => {
			console.log(err.message);
			console.log('err');
			// this.logout();
		})
	},
	logout: function () {
		this.isAuthenticated = false;
		this.username = null;
		this.accessToken = null;
		this.refreshToken = null;
		
		this.clearCookie();
	},
	getCookie: function (value) {
		let matches = document.cookie.match(new RegExp(
			"(?:^|; )" + value.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
		));
		return matches ? decodeURIComponent(matches[1]) : undefined;
	},
	fillCookie: function () {
		let twoWeeks = new Date(new Date().getTime() + 1209600 * 1000);
		document.cookie = 'username=' + this.username + '; path=/; expires=' + twoWeeks.toUTCString();
		document.cookie = 'accessToken=' + this.accessToken + '; path=/; expires=' + twoWeeks.toUTCString();
		document.cookie = 'refreshToken=' + this.refreshToken + '; path=/; expires=' + twoWeeks.toUTCString();
	},
	clearCookie: function () {
		let date = new Date(new Date().getTime() - 1);
		document.cookie = 'username=;' + 'expires=' + date.toUTCString();
		document.cookie = 'accessToken=;' + 'expires=' + date.toUTCString();
		document.cookie = 'refreshToken=;' + 'expires=' + date.toUTCString();
	}
};

// router.beforeEach((to, from, next) => {
// 	if (to.matched.some(record => record.meta.requiresAuth) && !Auth.isAuthenticated) {
// 		if (!Auth.tryToLogin()) {
// 			next({path: '/login'});
// 		}
// 	} else {
// 		next();
// 	}
// });

const serverUrl = 'http://localhost:8080';

const app = new Vue({
	el: '#app',
	data: {
		username: 'user',
		serverUrl: serverUrl
	},
	router: router,
	method: {
		callLoadTodos: function () {
			this.loadTodos();
		}
	}
});
