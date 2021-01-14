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
          class="text-center"
        >
          {{
            userTransaction.receipt === "income"
              ? `+${userTransaction.amount}`
              : `${userTransaction.amount}`
          }}
        </b-col>
        <b-col> Rp. {{ userTransaction.residue }} </b-col>
        <b-col>
          <b-row class="align-items-center">
            <b-col>
              <b-button
                v-b-modal="'my-modal'"
                @click="sendUserTransaction(userTransaction)"
                >Edit</b-button
              >
            </b-col>
            <!-- <b-col
              class="btn btn-sm btn-warning"
              @click="(e) => handleEdit(e, userTransaction.transactionId)"
            >
              Edit
            </b-col> -->
            <b-col class="text-center ">
              <span
                class="btn btn-sm btn-danger "
                @click="(e) => handleDelete(e, userTransaction.transactionId)"
                >X</span
              >
            </b-col>
          </b-row>
        </b-col>
      </b-row>
      <hr />
    </div>
    <b-modal id="my-modal" title="Edit Transaksi">
      <div>
        <b-form @submit="onSubmit" @reset="onReset" v-if="show">
          <b-form-group
            id="input-group-1"
            label="Deskripsi:"
            label-for="input-1"
            description="Tuliskan Catatan Tentang Pengeluaran/Pemasukan Ini"
          >
            <b-form-input
              id="input-1"
              v-model="form.description"
              type="text"
              placeholder="deskripsi.."
              required
            ></b-form-input>
          </b-form-group>

          <b-form-group
            id="input-group-2"
            label="Jenis Transaksi"
            label-for="input-2"
          >
            <b-form-select
              id="input-2"
              v-model="form.receipt"
              :options="incomeExpenses"
              required
            ></b-form-select>
          </b-form-group>

          <b-form-group
            id="input-group-3"
            label="Nama Transaksi"
            label-for="input-3"
          >
            <b-form-select
              id="input-3"
              v-model="form.transactionTypeId"
              :options="transactions"
              required
            ></b-form-select>
          </b-form-group>

          <b-form-group
            id="input-group-4"
            label="Jumlah"
            label-for="input-4"
            description="Jumlah uang yang masuk/keluar"
          >
            <b-form-input
              id="input-4"
              v-model.number="form.amount"
              type="number"
              placeholder="Rp..."
              required
            ></b-form-input>
          </b-form-group>

          <b-button type="submit" variant="primary">Submit</b-button>
          <b-button type="reset" variant="danger">Reset</b-button>
        </b-form>
        <!-- <b-card class="mt-3" header="Form Data Result">
          <pre class="m-0">{{ form }}</pre>
        </b-card> -->
      </div>
    </b-modal>
  </div>
</template>

<script>
import axios from "axios";
// import EditTransaction from "../components/EditTransaction";
export default {
  name: "transaction",
  props: ["userTransactions"],
  data() {
    return {
      transactionId: 1,
      description: "",
      receipt: "",
      transactionType: "",
      amount: 0,
      form: {
        userId: 1,
      },
      transactions: [
        { text: "Select One", value: null },
        { text: "Transportation", value: 2 },
        { text: "Medical", value: 3 },
        { text: "Consumption", value: 4 },
        { text: "Office Supplies", value: 5 },
        { text: "Courier Fee", value: 6 },
        { text: "Stamp Duty", value: 7 },
        { text: "phone", value: 8 },
        { text: "Internet", value: 9 },
        { text: "Entertainment", value: 10 },
        { text: "Reimburse", value: 11 },
        { text: "Top Up", value: 12 },
      ],
      incomeExpenses: [
        { text: "Select One", value: null },
        { text: "Income", value: "income" },
        { text: "Expenses", value: "outcome" },
      ],
      show: true,
    };
  },
  components: {
    // EditTransaction,
  },
  methods: {
    handleDelete: function(e, id) {
      e.preventDefault();
      console.log(id);
      // const urlDel = `http://10.69.72.89:8081/pettycash/add/delete-transaction?transactionId=${id}`;
      // // const urlGet =
      // //   "http://10.69.72.89:8081/pettycash/view/getTransaction?userId=1&page=0";
      // axios.get(urlDel).then((res) => console.log(res));
    },

    sendUserTransaction: function(UserTransactionData) {
      const {
        description,
        receipt,
        transactionType,
        amount,
        transactionId,
      } = UserTransactionData;
      this.description = description;
      this.receipt = receipt;
      this.transactionType = transactionType;
      this.amount = amount;
      this.transactionId = transactionId;
      console.log(description);
      console.log(receipt);
      console.log(transactionType);
      console.log(amount);
      // console.log(this.transactionId);
    },
    onSubmit(event) {
      event.preventDefault();
      const url = `http://10.69.72.89:8081/pettycash/add/update-transaction?transactionId=${this.transactionId}`;
      console.log(this.form);
      axios
        .post(url, this.form)
        .then((res) => console.log(res.data))
        .catch((err) => console.log(err));
      // alert(JSON.stringify(this.form));
      // console.log(this.form);
    },
    onReset(event) {
      event.preventDefault();
      // Reset our form values
      this.form.description = "";
      this.form.receipt = "";
      this.form.transactionTypeId = 0;
      // Trick to reset/clear native browser form validation state
      this.show = false;
      this.$nextTick(() => {
        this.show = true;
      });
    },
  },
};
</script>

<style scoped>
.btn-del {
  width: 100%;
  height: 100%;
}

.income {
  color: green;
}
.expenses {
  color: red;
}
</style>
