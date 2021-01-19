<template>
  <div>
    <div
      class="transactions"
      v-for="(userApproval, index) in userApprovals"
      v-bind:key="index"
    >
      <div v-if="userApproval.isApproved === 'not_approved'">
        <b-row>
          <b-col>
            {{ userApproval.transactionDate.split("T")[0] }}
          </b-col>
          <b-col>
            {{ userApproval.transactionType.transactionTypeName }}
          </b-col>
          <b-col>
            {{ userApproval.description }}
          </b-col>
          <b-col
            v-bind:class="
              userApproval.receipt === 'income' ? 'income' : 'expenses'
            "
          >
            {{
              userApproval.receipt === "income"
                ? `+Rp.${userApproval.amount}`
                : `Rp.${userApproval.amount}`
            }}
          </b-col>

          <b-col>
            <b-button
              v-b-modal.modalApprove
              class="btn btn-sm mx-2"
              @click="(e) => handleApprove(e, userApproval.transactionId)"
            >
              <CheckboxMarked />
            </b-button>
            <b-button v-b-modal.modalClose class="btn btn-sm mx-2">
              <WindowClose />
            </b-button>
          </b-col>
        </b-row>
        <hr />
      </div>
    </div>
    <b-modal id="modalApprove">
      <h1 class="text-center">Approve Transaction?</h1>
    </b-modal>
  </div>
</template>

<script>
import CheckboxMarked from "vue-material-design-icons/CheckboxMarked";
import WindowClose from "vue-material-design-icons/WindowClose";

export default {
  name: "Approvals",
  props: ["userApprovals"],
  components: {
    CheckboxMarked,
    WindowClose,
  },
  methods: {
    handleApprove(e, id) {
      e.preventDefault();

      console.log(`id of ${id} approved`);
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
