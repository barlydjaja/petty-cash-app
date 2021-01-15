<template>
  <div class="text-center align-center">
    <div>
      <b-button v-b-modal.modal-prevent-closing-2 class="transaction"
        >Tambah Transaksi</b-button
      >

      <!-- Submitted Names: -->

      <b-modal
        id="modal-prevent-closing-2"
        ref="modal"
        title="Masukan Transaksi"
      >
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
                placeholder="Deskripsi..."
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
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "AddTransaction",
  data() {
    return {
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
  methods: {
    onSubmit(event) {
      event.preventDefault();
      console.log(localStorage.getItem("token"));
      const url = "http://10.69.72.89:8081/pettycash/v1/add/addTransaction";
      const config = {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      };
      console.log(this.form);
      axios
        .post(url, this.form, config)
        .then((res) => console.log(res.data))
        .catch((err) => console.log(err));
      // alert(JSON.stringify(this.form));
      // console.log(this.form);
      this.$bvModal.hide("modal-prevent-closing-2");
      this.$forceUpdate();
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
.btn-secondary {
  color: #fdcb5a;
  background-color: #1a3150;
}

.btn-secondary:hover {
  color: #fdcb5a;
  background-color: #1a3150;
}

.btn-secondary:focus {
  color: #fdcb5a;
  background-color: #1a3150;
}

.transaction {
  font-size: 1em;
  font-weight: bold;
}
</style>
