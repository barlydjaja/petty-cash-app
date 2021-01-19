<template>
  <div class="background">
    <Header />
    <b-container class="my-5 vh-75">
      <b-jumbotron class="pt-4">
        <b-row class="align-items-center">
          <b-col sm="9">
            <div class="username font-weight-bold">
              Username: {{ userApprovals[0].user.username }}
            </div>
            <br />
            <div class="balance">
              Current Balance: Rp. {{ userApprovals[0].user.accountBalance }}
            </div>
          </b-col>
        </b-row>
        <div class="new-transaction mt-2">Transaksi Baru</div>
        <b-row class="mt-3 font-weight-bold">
          <b-col class="number"> Tanggal </b-col>
          <b-col class="transaction">Transaksi</b-col>
          <b-col class="date">Deskripsi</b-col>
          <b-col class="date">Mutasi</b-col>
          <b-col class="ending-balance">Approval</b-col>
        </b-row>
        <hr />

        <Approvals v-bind:userApprovals="userApprovals" />
      </b-jumbotron>
    </b-container>
    <Footer />
  </div>
</template>

<script>
import Header from "../layout/Header";
import Footer from "../layout/Footer";
import axios from "axios";
import Approvals from "../components/Approvals";

export default {
  name: "Approval",
  components: {
    Header,
    Footer,
    Approvals,
  },

  data() {
    return {
      userData: [],
      userApprovals: [],
      totalItems: 0,
      accountBalance: 0,
      pages: 1,
      totalPages: 1,
    };
  },

  created() {
    // console.log("user role is: " + localStorage.getItem("role"));
    // console.log("user id is: " + localStorage.getItem("userId"));

    if (localStorage.getItem("token")) {
      // let page = this.pages;
      // if (this.pages === 0) this.page = 0;
      const url = `http://10.69.72.89:8081/pettycash/v1/view/not-approved-transaction?userId=1&page=${0}`;

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
</style>
