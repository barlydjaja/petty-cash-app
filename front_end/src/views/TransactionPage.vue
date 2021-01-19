<template>
  <div class="background">
    <Header />
    <b-container class="my-5 ">
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
        <b-row class="mt-3 font-weight-bold">
          <b-col class="number" sm="2"> Tanggal </b-col>
          <b-col class="transaction" sm="2">Transaksi</b-col>
          <b-col class="date" sm="2">Deskripsi</b-col>
          <b-col class="income-expenses" sm="2">mutasi</b-col>
          <b-col class="ending-balance" sm="2">Sisa Saldo</b-col>
          <b-col></b-col>
        </b-row>

        <Transactions v-bind:userTransactions="userTransactions" />
        <div class="text-center ">
          <pagination
            v-model="pages"
            :records="totalItems"
            @paginate="handlePageChange"
            :per-page="10"
          ></pagination>
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

export default {
  name: "Demo",
  components: {
    Header,
    Footer,
    Transactions,
    AddTransaction,
  },
  data() {
    return {
      userData: [],
      userTransactions: [],
      totalItems: 0,
      accountBalance: 0,
      pages: 1,
      totalPages: 1,
    };
  },

  methods: {
    handlePageChange(pageNumber) {
      console.log(pageNumber);
      let url = `http://10.69.72.89:8081/pettycash/v1/view/approved-transaction?userId=1&page=${pageNumber -
        1}`;
      const config = {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      };

      axios
        .get(url, config)
        .then((res) => {
          // console.log(res.data);
          this.userData = res.data;
          this.userTransactions = res.data.transactions;
          this.accountBalance = res.data.transactions[0].user.accountBalance;
          this.totalItems = res.data.totalItems;

          // console.log(res.data);
          // console.log(res.data.transactions);
        })
        .catch((err) => console.log(err));
    },
  },

  created() {
    // console.log("user role is: " + localStorage.getItem("role"));
    // console.log("user id is: " + localStorage.getItem("userId"));

    if (localStorage.getItem("token")) {
      let page = this.pages;
      if (this.pages === 0) this.page = 0;

      const url = `http://10.69.72.89:8081/pettycash/v1/view/approved-transaction?userId=1&page=${page -
        1}`;

      const config = {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      };
      axios
        .get(url, config)
        .then((res) => {
          this.userData = res.data;
          this.userTransactions = res.data.transactions;
          this.accountBalance = res.data.transactions[0].user.accountBalance;
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

<style lang="css">
.balance {
  font-size: 1.25em;
  display: inline;
}

.pagination {
  justify-content: center;
}
</style>
