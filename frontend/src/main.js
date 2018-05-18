import Vue from 'vue'
import VueRouter from 'vue-router'
import VueAutosize from 'vue-autosize'
import store from './store/store'

export {
	headers,
	serverUrl,
	router
}

Vue.use(VueRouter);
Vue.use(VueAutosize);

const notFoundPage = require('./components/NotFound').default;
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
		{path: '/login', component: authPage},
		{path: '/**', component: notFoundPage}
	],
	mode: 'history'
});

let firstAuth = true;

router.beforeEach((to, from, next) => {
	if (to.matched.some(record => record.meta.requiresAuth) && firstAuth) {
		store.dispatch('loadAuth');
		firstAuth = false;
	} else {
		next();
	}
});

const serverUrl = 'http://localhost:8080';

const app = new Vue({
	el: '#app',
	store,
	data: {
		serverUrl: serverUrl
	},
	computed: {
		username() {
			return store.getters.getUsername;
		},
		isAuthenticated() {
			return store.getters.isAuthenticated;
		}
	},
	router: router,
	methods: {
		logout: function () {
			store.dispatch('logout');
		},
		refreshAuthorization() {
		
		}
	},
	created() {
		console.log('created');
		
		(function () {
			let originalFetch = fetch;
			fetch = function () {
				return originalFetch.apply(this, arguments).then(res => {
					console.log(res.status);
					if (res.status === 401) {
						console.log('401 intercepted');
						store.dispatch('loadAuth')
					}
					return res;
				});
			};
		})();
	}
});
