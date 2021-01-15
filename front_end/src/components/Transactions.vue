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
        <b-col sm="2"> Rp. {{ userTransaction.residue }} </b-col>
      </b-row>
      <b-row>
        <b-col>
          <b-button
            v-b-modal="'my-modal'"
            @click="(e) => handleEdit(e, userTransaction.transactionId)"
            >Edit</b-button
          >
        </b-col>
        <b-col>
          <b-button
            v-if="!userTransaction.fileName"
            v-b-modal="'upload'"
            @click="(e) => handleUpload(e, userTransaction.transactionId)"
          >
            upload
          </b-button>
          <b-button
            v-if="userTransaction.fileName"
            @click="(e) => handleDownload(e, userTransaction.transactionId)"
          >
            download
            <div id="image"></div>
          </b-button>
        </b-col>
        <b-col class="text-center ">
          <span
            class="btn btn-sm btn-danger "
            @click="(e) => handleDelete(e, userTransaction.transactionId)"
            >X</span
          >
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
        <b-card class="mt-3" header="Form Data Result">
          <pre class="m-0">{{ form }}</pre>
        </b-card>
      </div>
    </b-modal>
    <b-modal id="upload" title="Upload Bukti Transaksi">
      <label
        >File
        <input
          type="file"
          id="file"
          ref="file"
          v-on:change="handleFileUpload()"
        />
      </label>
      <button v-on:click="submitFile()">Submit</button>
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
      file: "",
    };
  },
  components: {
    // EditTransaction,
  },
  methods: {
    handleDelete: function(e, id) {
      e.preventDefault();
      console.log(id);
      const urlDel = `http://10.69.72.89:8081/pettycash/v1/add/delete-transaction?transactionId=${id}`;
      const config = {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      };
      // const urlGet =
      //   "http://10.69.72.89:8081/pettycash/view/getTransaction?userId=1&page=0";
      axios.get(urlDel, config).then((res) => console.log(res));
    },

    handleEdit(e, id) {
      e.preventDefault();
      this.transactionId = id;
    },

    handleUpload(e, id) {
      e.preventDefault();
      this.transactionId = id;
    },

    handleDownload(e, id) {
      e.preventDefault();
      this.transactionId = id;
      const url = `http://10.69.72.89:8081/pettycash/v1/file/download/${this.transactionId}`;
      const config = {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
          responseType: "blob",
        },
      };
      axios.get(url, config).then((res) => {
        const fileUrl = window.URL.createObjectURL(new Blob([res.data]));
        const fileLink = document.createElement("a");
        fileLink.href = fileUrl;

        fileLink.setAttribute("download", "image.jpg");
        document.body.appendChild(fileLink);
        fileLink.click();
      });
    },

    onSubmit(event) {
      event.preventDefault();
      const url = `http://10.69.72.89:8081/pettycash/v1/add/update-transaction?transactionId=${this.transactionId}`;
      console.log(this.form);
      const config = {
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
      };
      axios
        .post(url, this.form, config)
        .then((res) => console.log(res.data))
        .catch((err) => console.log(err));
      // alert(JSON.stringify(this.form));
      console.log(this.form);
      console.log(this.transactionId);
    },

    handleFileUpload() {
      this.file = this.$refs.file.files[0];
    },

    submitFile() {
      let formData = new FormData();
      formData.append("file", this.file);
      formData.append("transactionId", this.transactionId);
      const url = "http://10.69.72.89:8081/pettycash/v1/file/upload";
      const config = {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
          "Content-Type": "multipart/form-data",
        },
      };

      axios
        .post(url, formData, config)
        .then((res) => console.log(res.data))
        .catch((err) => console.log(err));
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

    checkFile() {},
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
</style>
