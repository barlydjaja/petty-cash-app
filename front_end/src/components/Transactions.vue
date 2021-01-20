<template>
  <div>
    <hr />

    <div
      class="transactions"
      v-for="(userTransaction, index) in userTransactions"
      v-bind:key="index"
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
          sm="2"
        >
          {{
            userTransaction.receipt === "income"
              ? `+${userTransaction.amount}`
              : `${userTransaction.amount}`
          }}
        </b-col>
        <b-col sm="2"> Rp. {{ userTransaction.residue }} </b-col>
        <b-col sm="2">
          <b-button
            v-b-modal="'my-modal'"
            @click="(e) => handleEdit(e, userTransaction.transactionId)"
            class="btn btn-sm"
          >
            <ClipboardEdit />
          </b-button>

          <b-button
            v-if="!userTransaction.fileName"
            v-b-modal="'upload'"
            class="btn btn-sm mx-1"
            @click="(e) => handleUpload(e, userTransaction.transactionId)"
          >
            <CloudUpload />
          </b-button>

          <a
            v-auth-href="{ token: token }"
            v-bind:href="
              'http://10.69.72.89:8081/pettycash/v1/file/download/' +
                userTransaction.transactionId
            "
            v-if="userTransaction.fileName"
            class="btn btn-sm btn-secondary mx-1"
          >
            <Download />
          </a>

          <span
            v-b-modal="'delete'"
            class="btn btn-sm btn-danger"
            @click="(e) => onDelete(e, userTransaction.transactionId)"
          >
            <Delete />
          </span>
        </b-col>
      </b-row>

      <hr />
    </div>
    <b-modal id="my-modal" title="Edit Transaksi" hide-footer>
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
    <b-modal
      id="upload"
      title="Upload Bukti Transaksi"
      @ok="submitFile()"
      centered
    >
      <label
        >File
        <input
          type="file"
          id="file"
          ref="file"
          v-on:change="handleFileUpload()"
        />
      </label>
      <!-- <button v-on:click="submitFile()">Submit</button> -->
    </b-modal>
    <b-modal
      id="delete"
      @ok="handleDelete()"
      hide-header
      centered
      ok-variant="danger"
    >
      <h4 class="lead text-center">
        Delete Transaction?
      </h4>
      <!-- <button v-on:click="submitFile()">Submit</button> -->
    </b-modal>
  </div>
</template>

<script>
import axios from "axios";
import ClipboardEdit from "vue-material-design-icons/ClipboardEdit.vue";
import CloudUpload from "vue-material-design-icons/CloudUpload.vue";
import Download from "vue-material-design-icons/Download.vue";
import Delete from "vue-material-design-icons/Delete.vue";

// import EditTransaction from "../components/EditTransaction";
let authHeader = `Bearer + ${localStorage.getItem("token")}`;
axios.defaults.headers.common["Authorization"] = authHeader;
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
        userId: localStorage.getItem("userId"),
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
      token: localStorage.getItem("token"),
    };
  },
  components: {
    ClipboardEdit,
    CloudUpload,
    Download,
    Delete,
  },
  methods: {
    onDelete(e, id) {
      e.preventDefault();
      this.transactionId = id;
    },

    handleDelete: function() {
      const urlDel = `http://10.69.72.89:8081/pettycash/v1/transaction/delete?transactionId=${this.transactionId} `;
      const config = {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      };
      axios.get(urlDel, config).then((res) => {
        if (res.status === 200) this.$router.go();
      });
    },

    handleEdit(e, id) {
      e.preventDefault();
      this.transactionId = id;
    },

    handleUpload(e, id) {
      e.preventDefault();
      this.transactionId = id;
    },

    onSubmit(event) {
      event.preventDefault();
      const url = `http://10.69.72.89:8081/pettycash/v1/transaction/update?transactionId=${this.transactionId}`;
      // console.log(this.form);
      const config = {
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
      };
      axios
        .post(url, this.form, config)
        .then((res) => {
          if (res.status === 200) this.$router.go();
        })
        .catch((err) => console.log(err));
      // alert(JSON.stringify(this.form));
      // console.log(this.form);
      // console.log(this.transactionId);
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
        .then((res) => {
          console.log(res.data);
          if (res.status === 200) this.$router.go();
        })
        .catch((err) => console.log(err));
    },

    onReset(event) {
      event.preventDefault();
      // Reset our form values
      this.form.description = "";
      this.form.receipt = "";
      this.form.transactionTypeId = 0;
      this.form.amount = 0;
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
