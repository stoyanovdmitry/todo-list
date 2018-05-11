import Vue from 'vue'
import Vuex from 'vuex'
import {headers} from "../main";
import {serverUrl} from "../main"
import {router} from "../main"

Vue.use(Vuex);


const store = new Vuex.Store({
	state: {
		auth: {
			authenticated: false,
			username: null,
			accessToken: null,
			refreshToken: null
		}
	},
	getters: {
		isAuthenticated: state => {
			return state.auth.authenticated;
		},
		getUsername: state => {
			return state.auth.username;
		},
		getAccessToken: state => {
			return state.auth.accessToken;
		},
		getRefreshToken: state => {
			return state.auth.refreshToken;
		}
	},
	mutations: {
		setAuthenticated(state, isAuthenticated) {
			state.auth.authenticated = isAuthenticated;
		},
		setUsername(state, username) {
			state.auth.username = username;
		},
		setAccessToken(state, accessToken) {
			state.auth.accessToken = accessToken;
		},
		setRefreshToken(state, refreshToken) {
			state.auth.refreshToken = refreshToken;
		},
	},
	actions: {
		loadAuth({commit, dispatch}) {
			console.log('dispatch start');
			
			let username = getCookie('username');
			let accessToken = getCookie('accessToken');
			let refreshToken = getCookie('refreshToken');
			
			if (username === undefined || accessToken === undefined || refreshToken === undefined) {
				dispatch('logout');
			} else {
				tryToRefresh();
			}
			
			function getCookie(value) {
				let matches = document.cookie.match(new RegExp(
					"(?:^|; )" + value.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
				));
				return matches ? decodeURIComponent(matches[1]) : undefined;
			}
			
			function tryToRefresh() {
				let refreshUrl = serverUrl + '/token/refresh';
				headers.set('Authorization', refreshToken);
				
				fetch(refreshUrl, {
					method: 'POST',
					headers: headers
				}).then(res => {
					if (res.ok) {
						accessToken = res.headers.get('access-Token');
						refreshToken = res.headers.get('refresh-token');
						
						commit('setUsername', username);
						commit('setAccessToken', accessToken);
						commit('setRefreshToken', refreshToken);
						commit('setAuthenticated', true);
						
						dispatch('fillCookies');
						
						headers.set('Authorization', accessToken);
						router.push('/');
						console.log('successLoading');
					} else {
						console.log('didnt refreshed');
						dispatch('logout');
					}
				}).catch(err => {
					console.log(err.message);
					console.log('err');
					dispatch('logout');
				})
			}
		},
		logout({commit}) {
			let date = new Date(new Date().getTime() - 1);
			document.cookie = 'username=;' + 'expires=' + date.toUTCString();
			document.cookie = 'accessToken=;' + 'expires=' + date.toUTCString();
			document.cookie = 'refreshToken=;' + 'expires=' + date.toUTCString();
			
			commit('setAuthenticated', false);
			commit('setUsername', undefined);
			commit('setAccessToken', undefined);
			commit('setRefreshToken', undefined);
			
			router.push('/login');
		},
		fillCookies() {
			let twoWeeks = new Date(new Date().getTime() + 1209600 * 1000);
			document.cookie = 'username=' + store.getters.getUsername + '; path=/; expires=' + twoWeeks.toUTCString();
			document.cookie = 'accessToken=' + store.getters.getAccessToken + '; path=/; expires=' + twoWeeks.toUTCString();
			document.cookie = 'refreshToken=' + store.getters.getRefreshToken + '; path=/; expires=' + twoWeeks.toUTCString();
		}
	}
});

export default store;