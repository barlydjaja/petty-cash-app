import Vue from "vue";
import App from "./App.vue";
import { BootstrapVue, IconsPlugin } from "bootstrap-vue";
import { router } from "./route";
import VueAuthImage from "vue-auth-image";
import VueAuthHref from "vue-auth-href";
import Pagination from "vue-pagination-2";
import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-vue/dist/bootstrap-vue.css";

Vue.config.productionTip = false;
Vue.use(BootstrapVue);
Vue.use(IconsPlugin);
Vue.use(VueAuthImage);
Vue.use(VueAuthHref);

Vue.component("pagination", Pagination);

new Vue({
  router,
  render: (h) => h(App),
}).$mount("#app");
