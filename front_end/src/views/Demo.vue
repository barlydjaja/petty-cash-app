<template>
  <div class="background">
    <Header />
    <b-container class="my-5">
      <b-jumbotron class="pt-4">
        <b-row class="align-items-center">
          <b-col sm="9">
            <div class="username font-weight-bold">
              Username: {{ userData.name }}
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
        <b-row class="mt-3">
          <b-col class="number" sm="2"> Tanggal </b-col>
          <b-col class="transaction" sm="2">Transaksi</b-col>
          <b-col class="date">Deskripsi</b-col>
          <b-col class="income-expenses">mutasi</b-col>
          <b-col class="ending-balance">Sisa Saldo</b-col>
          <b-col sm="1"></b-col>
        </b-row>

        <Transactions v-bind:userTransactions="userTransactions" />
        <b-row class="justify-content-center">
          <b-col>
            <jw-pagination
              :items="userTransactions"
              @changePage="onChangePage"
            />
          </b-col>
        </b-row>
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
// import Pagination from "vue-pagination-2";

export default {
  name: "Demo",
  components: {
    Header,
    Footer,
    Transactions,
    AddTransaction,
    // Pagination,
  },
  data() {
    return {
      userData: [],
      userTransactions: [],
      totalItems: 0,
      accountBalance: 0,
      page: 1,
    };
  },

  created() {
    axios
      .get(
        "http://10.69.72.89:8081/pettycash/view/getTransaction?userId=1&page=0"
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
