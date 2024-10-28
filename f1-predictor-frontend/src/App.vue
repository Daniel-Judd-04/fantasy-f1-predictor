<template>
  <div id="app" class="min-h-screen bg-gray-100 flex items-center justify-center">
    <div class="text-center">
      <h1 class="text-4xl font-bold text-blue-600 mb-4">Welcome to Your Vue.js App!</h1>
      <button
          @click="handleClick"
          class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 focus:outline-none"
      >
        Click Me
      </button>
      <TestComponent driverCode="1"/>
      <TestComponent driverCode="7"/>
      <TestComponent driverCode="15"/>
      <TestComponent driverCode="12"/>
    </div>
  </div>
</template>

<script>
import TestComponent from './components/TestComponent.vue'

export default {
  name: 'App',
  components: {
    TestComponent,
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
