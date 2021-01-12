<template>
  <div class="background">
    <Header />
    <b-container>
      <b-row class="vh-100">
        <b-col class="testing1">username: {{ userData.name }}</b-col>
        <b-col cols="9" class="testing2">
          <div
            v-bind:key="transaction.transactionId"
            v-for="transaction in userTransactions"
          >
            <h3>receipt: {{ transaction.receipt }}</h3>
            <p>description: {{ transaction.description }}</p>
          </div>
        </b-col>
      </b-row>
    </b-container>

    <Footer />
  </div>
</template>

<script>
import axios from "axios";
import Header from "../layout/Header";
import Footer from "../layout/Footer";
export default {
  name: "Demo",
  components: {
    Header,
    Footer,
  },
  data() {
    return {
      userData: [],
      userTransactions: [],
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
        console.log(res.data);
        console.log(res.data.transactions);
      })
      .catch((err) => console.log(err));
  },
};
</script>

<style lang="css" scoped></style>
