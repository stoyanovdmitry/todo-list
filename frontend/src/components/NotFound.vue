<template>
	<div class="container">
		<div class="mt-4">
			<h2>{{ message }}</h2>
		</div>
	</div>
</template>

<script>
	export default {
		name: "NotFound",
		data() {
			return {
				message: "404. Sorry, but you have nothing to do here"
			}
		}
	}
</script>

<style scoped>
</style>