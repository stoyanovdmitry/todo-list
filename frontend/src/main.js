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

let firstAuth = true;

router.beforeEach((to, from, next) => {
	if(firstAuth) {
		store.dispatch('loadAuth');
		firstAuth = false;
		console.log(store.getters.isAuthenticated);
	}
	if (to.matched.some(record => record.meta.requiresAuth) && !store.getters.isAuthenticated) {
		console.log(store.getters.isAuthenticated);
		next({path: '/login'});
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
		}
	},
	router: router,
	method: {
		logout: function () {
			store.dispatch('logout');
		}
	}
});
