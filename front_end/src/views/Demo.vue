<template>
  <div class="background">
    <Header />
    <b-container class="my-5 vh-100">
      <b-jumbotron class="pt-4">
        <b-row class="align-items-center">
          <b-col sm="9">
            <div class="username font-weight-bold">
              Username: {{ userData.username }}
            </div>
            <br />
            <div class="balance">
              Current Balance: Rp.
              {{ accountBalance }}
            </div>
          </b-col>
          <b-col>
            <AddTransaction />
          </b-col>
        </b-row>

        <div class="new-transaction mt-2">Transaksi Baru</div>
        <b-row class="mt-3 font-weight-bold">
          <b-col class="number" sm="2"> Tanggal </b-col>
          <b-col class="transaction" sm="2">Transaksi</b-col>
          <b-col class="date">Deskripsi</b-col>
          <b-col class="income-expenses">mutasi</b-col>
          <b-col class="ending-balance">Sisa Saldo</b-col>
          <b-col>
            <b-row sm="3"></b-row>
          </b-col>
        </b-row>

        <Transactions v-bind:userTransactions="userTransactions" />
        <div class="text-center">
          <Pagination
            v-bind:nextPage="{ nextPage }"
            v-bind:prevPage="{ prevPage }"
          />
        </div>
      </b-jumbotron>
    </b-container>

    <Footer />
  </div>
</template>

<script>
import axios from "axios";
import Header from "../layout/Header";
import Footer from "../layout/Footer";
import Transactions from "../components/Transactions";
import AddTransaction from "../components/AddTransaction";
import Pagination from "../components/Pagination";

export default {
  name: "Demo",
  components: {
    Header,
    Footer,
    Transactions,
    AddTransaction,
    Pagination,
  },
  data() {
    return {
      userData: [],
      userTransactions: [],
      totalItems: 0,
      accountBalance: 0,
      pages: 0,
      totalPages: 1,
    };
  },

  methods: {
    nextPage: function(e) {
      let currentPage = this.pages;
      if (this.pages < this.totalPages) {
        let nextPage = currentPage + 1;
        this.pages = nextPage;
      }
      e.preventDefault();
      console.log("next page");
    },

    prevPage: function(e) {
      e.preventDefault();
      console.log("prev page");
    },
  },

  created() {
    let page = this.pages;
    // console.log(this.pages);
    if (this.pages === 0) page = 0;
    axios
      .get(
        `http://10.69.72.89:8081/pettycash/view/getTransaction?userId=1&page=${page}`
      )
      .then((res) => {
        this.userData = res.data;
        this.userTransactions = res.data.transactions;
        this.accountBalance = res.data.transactions[0].user.accountBalance;
        this.totalItems = res.data.totalItems;
        console.log(res.data);
        console.log(res.data.transactions);
      })
      .catch((err) => console.log(err));
  },
};
</script>

<style lang="css" scoped>
.username,
.balance {
  font-size: 1.25em;
  display: inline;
}
</style>
