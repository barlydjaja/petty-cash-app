<template>
  <div class="background">
    <Header />
    <b-alert
      class="alertUnathorized text-center"
      variant="danger"
      v-model="showDismissibleAlert"
      >Unathorized!</b-alert
    >
    <b-container class="my-5 vh-75">
      <b-jumbotron class="pt-4">
        <b-row class="justify-content-center">
          <h1>Delete Request</h1>
        </b-row>
        <hr />
        <b-row class="align-items-center">
          <b-col sm="9">
            <div class="username font-weight-bold">
              Username: {{ username }}
            </div>
            <br />
          </b-col>
        </b-row>
        <div class="new-transaction mt-2">{{ $t("newTransaction") }}</div>
        <b-row class="mt-3 font-weight-bold">
          <b-col class="number text-center" sm="2"> {{ $t("date") }} </b-col>
          <b-col class="transaction text-center" sm="2">{{
            $t("transaction2")
          }}</b-col>
          <b-col class="date text-center" sm="3">{{ $t("description") }}</b-col>
          <b-col class="date text-center" sm="2">{{ $t("mutation") }}</b-col>
          <b-col class="approval text-center" sm="3" v-if="Number(role) === 1"
            >Delete</b-col
          >
        </b-row>
        <hr />

        <Deleted
          v-bind:userApprovals="userApprovals"
          v-on:unathorized="onUnathorized"
        />
      </b-jumbotron>
    </b-container>
    <Footer />
  </div>
</template>

<script>
import Header from "../layout/Header";
import Footer from "../layout/Footer";
import axios from "axios";
import Deleted from "../components/Deleted";

export default {
  name: "DeletedPage",
  components: {
    Header,
    Footer,
    Deleted,
  },

  data() {
    return {
      username: localStorage.getItem("username"),
      userData: [],
      userApprovals: [],
      totalItems: 0,
      accountBalance: 0,
      pages: 1,
      totalPages: 1,
      showDismissibleAlert: false,
      role: localStorage.getItem("roleId"),
    };
  },

  methods: {
    onUnathorized() {
      this.showDismissibleAlert = true;
      setTimeout(() => {
        this.showDismissibleAlert = false;
      }, 2000);
      // console.log("received");
      // this.$emit("fromApprovalPage", 1);
    },
  },

  created() {
    // console.log("user role is: " + localStorage.getItem("role"));
    // console.log("user id is: " + localStorage.getItem("userId"));

    if (localStorage.getItem("token")) {
      // let page = this.pages;
      // if (this.pages === 0) this.page = 0;
      const url = `http://10.69.72.99:8081/pettycash/v1/view/pending-delete?userId=${localStorage.getItem(
        "userId"
      )}&page=${0}`;

      const config = {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      };
      axios
        .get(url, config)
        .then((res) => {
          this.userData = res.data;
          this.userApprovals = res.data.transactions;
          this.totalItems = res.data.totalItems;
          // this.totalPages = res.data.totalPages;
          console.log(res.data);
          console.log(res.data.transactions);
        })
        .catch((err) => console.log(err));
    }
  },
};
</script>

<style scoped>
.vh-75 {
  min-height: 75vh !important;
}

.alertUnathorized {
  position: fixed;
  width: 200px;
  top: 20%;
  left: 50%;
  transform: translateX(-50%);
  z-index: 1;
}
</style>
