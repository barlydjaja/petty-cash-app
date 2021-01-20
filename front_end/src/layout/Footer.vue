<template>
  <b-container fluid class="p-0 footer-bar">
    <b-container class="px-0">
      <b-row align-h="center" class="text-center pb-3">
        <b-col sm="7" md="4" class="px-0 ">
          <span class="text-title-config ">{{ $t("about") }}</span>
          <hr />
          <span class="text-subtitle-config ">{{ $t("companyProfile") }}</span>
          <br />
          <span class="text-subtitle-config ">{{ $t("companyHistory") }}</span>
          <br />
          <span class="text-subtitle-config ">{{ $t("career") }}</span>
        </b-col>
        <b-col sm="7" md="4">
          <span class="text-title-config">{{ $t("sosmed") }}</span>
          <hr />
          <span class="text-subtitle-config ">Facebook</span>
          <br />
          <span class="text-subtitle-config ">Instagram</span>
          <br />
          <span class="text-subtitle-config ">Twitter</span>
          <br />
          <span class="text-subtitle-config ">LinkedIn</span>
        </b-col>
        <b-col sm="7" md="4">
          <span class="text-title-config ">QnA</span>
          <hr />
          <span class="text-subtitle-config">{{ $t("howItWorks") }}</span>
          <br />
          <span class="text-subtitle-config">{{ $t("misscalculation") }} </span>
          <br />
          <span class="text-subtitle-config">{{ $t("others") }}</span>
          <br />
        </b-col>
      </b-row>
    </b-container>
  </b-container>
</template>

<script>
export default {
  name: "Footer",
};
</script>

<style scoped>
.footer-bar {
  background-color: #1a3150;
}

.text-title-config {
  font-size: 1.2em;
  color: #fdcb5a;
}

.text-subtitle-config {
  font-size: 0.9em;
  color: #fdcb5a;
}

hr {
  border: 1px solid white;
  margin-top: 0.2em;
  margin-bottom: 0.2em;
  width: 35%;
  margin: auto;
}
</style>
