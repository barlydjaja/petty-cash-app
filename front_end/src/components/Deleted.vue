<template>
  <div>
    <div
      class="transactions"
      v-for="(userApproval, index) in userApprovals"
      v-bind:key="index"
    >
      <!-- <div v-if="userApproval.isApproved === 'not_approved'"> -->
      <b-row>
        <b-col sm="2" class="text-center">
          {{ userApproval.transactionDate.split("T")[0] }}
        </b-col>
        <b-col sm="2" class="text-center">
          {{ userApproval.transactionType.transactionTypeName }}
        </b-col>
        <b-col sm="3" class="text-center">
          {{ userApproval.description }}
        </b-col>
        <b-col
          v-bind:class="
            userApproval.receipt === 'income' ? 'income' : 'expenses'
          "
          sm="2"
          class="text-center"
        >
          {{
            userApproval.receipt === "income"
              ? `+Rp.${userApproval.amount}`
              : `Rp.${userApproval.amount}`
          }}
        </b-col>

        <b-col class="text-center" sm="3">
          <!-- <b-button
            v-b-modal.modalApprove
            class="btn btn-sm"
            @click="(e) => handleApprove(e, userApproval.notTransactionId)"
          >
            Delete
          </b-button> -->

          <el-popconfirm
            v-if="Number(role) === 1"
            confirm-button-text="OK"
            cancel-button-text="No, Thanks"
            icon="el-icon-info"
            icon-color="red"
            title="Are you sure to delete this?"
            @confirm="() => handleDelete(userApproval.transactionId)"
          >
            <el-button slot="reference">
              <i class="el-icon-delete-solid"></i>
            </el-button>
          </el-popconfirm>
          <b-button
            v-b-modal.modalReject
            class="btn btn-sm mx-1"
            @click="(e) => handleReject(e, userApproval.transactionId)"
          >
            <CloseBox />
          </b-button>
        </b-col>
      </b-row>
      <hr />
      <!-- </div> -->
    </div>
    <b-modal id="modalReject" @ok="handleOkReject" hide-header centered>
      <h1 class="text-center">Cancel Delete?</h1>
      <!-- <b-card class="mt-3" header="Form Data Result">
        <pre class="m-0">{{ form }}</pre>
      </b-card> -->
    </b-modal>
  </div>
</template>

<script>
import CloseBox from "vue-material-design-icons/CloseBox";
import axios from "axios";

export default {
  name: "Deleted",
  props: ["userApprovals"],
  data() {
    return {
      transactionId: 0,
      role: localStorage.getItem("roleId"),
    };
  },

  components: { CloseBox },
  methods: {
    handleReject(e, id) {
      e.preventDefault();
      this.transactionId = id;
    },

    handleOkReject(e) {
      e.preventDefault();
      const url = `http://10.69.72.89:8081/pettycash/v1/transaction/reject-delete?transactionId=${this.transactionId}`;
      console.log(this.transactionId);
      const config = {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      };

      axios
        .get(url, config)
        .then((res) => {
          console.log(res.data);
          if (res.status === 200) this.$router.go();
        })
        .catch((err) => {
          console.log(err.message);
          this.$emit("unathorized", 1);
        });
    },

    handleDelete(id) {
      console.log("deleted");
      const url = `http://10.69.72.89:8081/pettycash/v1/transaction/delete?userId=${localStorage.getItem(
        "userId"
      )}&transactionId=${id}`;
      const config = {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      };
      axios.get(url, config).then((res) => {
        console.log(res.data);
        if (res.status === 200) this.$router.go();
      });
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
