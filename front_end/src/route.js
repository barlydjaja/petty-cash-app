import Vue from "vue";
import VueRouter from "vue-router";
import Home from "./views/Home";
import Demo from "./views/Demo";
import About from "./views/About";

Vue.use(VueRouter);

export const router = new VueRouter({
  mode: "history",
  routes: [
    { path: "/", component: Home },
    { path: "/demo", component: Demo },
    { path: "/about", component: About },
  ],
});
