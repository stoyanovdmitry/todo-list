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
			this.username = this.getCookie('username');
			this.accessToken = this.getCookie('accessToken');
			this.refreshToken = this.getCookie('refreshToken');
		}
		
		if (this.username === undefined || this.accessToken === undefined || this.refreshToken === undefined) {
			this.logout();
		}
		
		
		//todo check is access token working. if not - try to refresh, than if refresh is bad - clear cookies
		return this.isAuthenticated;
	},
	tryToRefresh: function () {
		return true;
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

router.beforeEach((to, from, next) => {
	if (to.matched.some(record => record.meta.requiresAuth) && !Auth.isAuthenticated) {
		if (!Auth.tryToLogin()) {
			next({path: '/login'});
		}
	} else {
		next();
	}
});

const serverUrl = 'http://localhost:8080';

new Vue({
	el: '#app',
	data: {
		user: null,
		serverUrl: serverUrl
	},
	router: router,
	methods: {
		loggin: function () {
		
		}
	},
	beforeMount() {
		this.user = {
			username: 'user'
		}
	}
});
