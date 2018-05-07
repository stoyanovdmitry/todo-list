import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter);

// const mainPage = require('./components/NotFound').default;
const mainPage = require('./components/TodosPage').default;

const router = new VueRouter({
	routes: [
		{path: '/main', component: mainPage}
	]
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
	}
});
