<template>
  <div>
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
            class="btn btn-sm btn-danger "
            @click="(e) => handleDelete(e, userTransaction.transactionId)"
            ><Delete
          /></span>
        </b-col>
      </b-row>
      <!-- <b-row> -->
      <!-- </b-row> -->

      <hr />
    </div>
  </div>
</template>

<script>
export default {
  name: "Approvals",
  props: ["userApprovals"],
};
</script>
