<template>
  <div>
    <Header />
    <div class="background">
      <b-container>
        <div class="main-text pt-5">{{ $t("title") }}</div>
        <hr class="hr-top" />
        <div class="sub-text mb-5">{{ $t("subTitle") }}</div>
      </b-container>
      <!-- jumbotron start -->
      <div>
        <b-jumbotron class="text-center pt-3 mt-5 w-100 mb-0">
          <h1 class="display-3">{{ $t("procedure") }}</h1>
          <hr class="hr-mid mb-4" />
          <b-row class="mb-5">
            <b-col class="font-weight-bold">
              <img
                src="../images/login-register.png"
                alt="login"
                style="width: 150px; height: 150px"
                class="mb-3"
              />
              <br />
              {{ $t("loginRegister") }}</b-col
            >
            <b-col class="font-weight-bold">
              <img
                src="../images/Income.png"
                alt="income"
                style="height: 150px; width: 150px"
                class="mb-3"
              />
              <br />
              {{ $t("income") }}</b-col
            >
            <b-col class="font-weight-bold">
              <img
                src="../images/Expenses.png"
                alt="expenses"
                style="height: 150px; width: 150px"
                class="mb-3"
              />
              <br />
              {{ $t("expenses") }}</b-col
            >
            <b-col class="font-weight-bold">
              <img
                src="../images/receipt.png"
                alt="receipt"
                style="height: 150px; width: 150px"
                class="mb-3"
              />
              <br />
              {{ $t("transaction") }}</b-col
            >
          </b-row>

          <div href="#" class="more-info-btn">
            <router-link to="/transaction">
              <span class="button-text-center">
                {{ $t("startNow") }}
              </span>
            </router-link>
          </div>
        </b-jumbotron>
        <!-- jumbotron end -->
      </div>
    </div>
    <Footer />
  </div>
</template>

<script>
import Header from "../layout/Header";
import Footer from "../layout/Footer";

export default {
  name: "Home",
  components: {
    Header,
    Footer,
  },
  data() {
    return {
      isLoading: false,
    };
  },
};
</script>

<style scoped>
.main-text,
.sub-text {
  text-align: center;
  color: white;
}

.main-text {
  font-size: 2em;
  font-weight: 700;
}

.sub-text {
  font-size: 0.8em;
}

.hr-top,
.hr-mid {
  margin: auto;
  width: 20vw;
}

.hr-top {
  border: 1px solid white;
}

.hr-mid {
  border: 1px solid black;
}

.display-3 {
  font-size: 2em;
}

.jumbotron {
  background-color: white;
}

.more-info-btn {
  margin: auto;
  position: relative;
  background-color: #1a3150;
  color: #fdcb5a;
  height: 4em;
  width: 30vw;
  border-radius: 1em;
}

.button-text-center {
  font-size: 1.5em;
  font-weight: bold;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

@media only screen and (max-width: 400px) {
  .button-text-center {
    font-size: 0.8em;
  }
}

a {
  color: #fdcb5a;
}

a:hover {
  color: #fdcb5a;
}
</style>
