<template>
  <div>
    <hr />

    <div
      class="transactions"
      v-for="userTransaction in userTransactions"
      v-bind:key="userTransaction.transactionId"
    >
      <b-row>
        <b-col sm="2">
          {{ userTransaction.transactionDate.split("T")[0] }}
        </b-col>
        <b-col sm="2">
          {{ userTransaction.transactionType.transactionTypeName }}
        </b-col>
        <b-col>
          {{ userTransaction.description }}
        </b-col>
        <b-col
          v-bind:class="
            userTransaction.receipt === 'income' ? 'income' : 'expenses'
          "
        >
          {{
            userTransaction.receipt === "income"
              ? `+${userTransaction.amount}`
              : `${userTransaction.amount}`
          }}
        </b-col>
        <b-col> Rp. {{ userTransaction.residue }} </b-col>
        <b-col
          sm="1"
          class="btn btn-sm btn-danger"
          @click="(e) => handleDelete(e, userTransaction.transactionId)"
        >
          del</b-col
        >
      </b-row>
      <hr />
    </div>
  </div>
</template>

<script>
import axios from "axios";
export default {
  name: "transaction",
  props: ["userTransactions"],
  data() {
    return {
      number: 1,
    };
  },
  methods: {
    handleDelete: function(e, id) {
      e.preventDefault();
      const urlDel = `http://10.69.72.89:8081/pettycash/add/delete-transaction?transactionId=${id}`;
      const urlGet =
        "http://10.69.72.89:8081/pettycash/view/getTransaction?userId=1&page=0";
      axios.get(urlDel).then((res) => console.log(res));
      axios.get(urlGet);
    },
  },
};
</script>

<style scoped>
.income {
  color: green;
}
.expenses {
  color: red;
}
</style>
