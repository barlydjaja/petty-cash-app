<template>
  <div class="background">
    <Header />
    <b-container class="my-5 ">
      <b-jumbotron class="pt-4">
        <b-row class="align-items-center">
          <b-col sm="9">
            <div class="username font-weight-bold">
              Username: {{ username }}
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

        <div class="new-transaction mt-2">{{ $t("newTransaction") }}</div>
        <b-row class="mt-3 font-weight-bold">
          <b-col class="number" sm="2"> {{ $t("date") }} </b-col>
          <b-col class="transaction" sm="2">{{ $t("transaction2") }}</b-col>
          <b-col class="date" sm="2">{{ $t("description") }}</b-col>
          <b-col class="income-expenses" sm="2">{{ $t("mutation") }}</b-col>
          <b-col class="ending-balance" sm="2">{{ $t("endBalance") }}</b-col>
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
      username: localStorage.getItem("username"),
    };
  },

  methods: {
    handlePageChange(pageNumber) {
      console.log(pageNumber);
      let url = `http://10.69.72.89:8081/pettycash/v1/view/approved-transaction?userId=${localStorage.getItem(
        "userId"
      )}&page=${pageNumber - 1}`;
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

      const url = `http://10.69.72.89:8081/pettycash/v1/view/approved-transaction?userId=2&page=${page -
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
          if (res.data.transactions[0])
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
