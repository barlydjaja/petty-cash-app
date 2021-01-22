import Vue from "vue";
import VueRouter from "vue-router";
import Home from "./views/Home";
import Demo from "./views/Demo";
import About from "./views/About";
import TransactionPage from "./views/TransactionPage";
import Approval from "./views/ApprovalPage";
import Edited from "./views/EditedPage";
import Deleted from "./views/DeletedPage";

Vue.use(VueRouter);

export const router = new VueRouter({
  mode: "history",
  routes: [
    { path: "/", component: Home },
    { path: "/demo", component: Demo },
    { path: "/about", component: About },
    { path: "/transaction", component: TransactionPage },
    { path: "/approval", component: Approval },
    { path: "/edited", component: Edited },
    { path: "/deleted", component: Deleted },
  ],
});
