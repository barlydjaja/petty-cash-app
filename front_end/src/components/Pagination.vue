<template>
  <div>
    <b-row class="justify-content-between">
      <b-col>
        <button
          class="btn btn-primary"
          @click="$emit('prev-page')"
          aria-label="Previous"
        >
          Back
        </button>
      </b-col>
      <b-col>
        <button
          class="btn btn-primary"
          @click="$emit('next-page')"
          aria-label="Next"
        >
          Next
        </button>
      </b-col>
    </b-row>
  </div>
</template>

<script>
export default {
  name: "Pagination",
  props: ["prevPage", "nextPage"],
};
</script>

<style scoped></style>
