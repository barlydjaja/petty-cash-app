<template>
  <div id="app">
    <router-link to="/" />
    <router-link to="/about" />
    <router-link to="/demo" />
    <router-view />
  </div>
</template>

<script>
export default {
  name: "App",
};
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.background {
  background: #458679;
  min-height: 100vh;
}
</style>
