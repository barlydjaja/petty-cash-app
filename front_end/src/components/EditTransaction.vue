<template>
  <div class="text-center">
    <div>
      <b-button v-b-modal.my-modal>Edit</b-button>

      <!-- The modal -->
      <b-modal id="my-modal">Hello From My Modal!</b-modal>
    </div>
  </div>
</template>

<script>
export default {
  name: "AddTransaction",
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
