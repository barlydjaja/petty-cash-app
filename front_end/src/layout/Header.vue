<template>
  <b-container fluid class="p-0 navigation-bar">
    <b-container class="p-0">
      <div>
        <b-navbar toggleable="lg" class="px-0">
          <b-navbar-brand href="#" class="brand-text px-0">
            <router-link to="/">
              <div class="text-coloring">
                PETTY CASH
              </div>
            </router-link>
          </b-navbar-brand>

          <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

          <b-collapse id="nav-collapse" is-nav>
            <b-navbar-nav>
              <b-nav-item-dropdown text="Lang" right>
                <b-dropdown-item
                  v-for="entry in languages"
                  :key="entry.language"
                  @click="changeLocale(entry.language)"
                  >{{ entry.title }}</b-dropdown-item
                >
              </b-nav-item-dropdown>
            </b-navbar-nav>

            <!-- Right aligned nav items -->
            <b-navbar-nav class="ml-auto text-coloring">
              <div class="mx-3 text-config">{{ $t("about") }}</div>
              <router-link to="/demo">
                <div
                  class="mx-3 text-config"
                  v-if="!isLogin()"
                  v-on:login="() => console.log('received')"
                >
                  Demo
                </div>
              </router-link>

              <div class="mx-3 text-config" v-if="!isLogin()">
                <!-- <Login /> -->
                <div>
                  <div>
                    <span v-b-modal.modal-prevent-closing>Login</span>

                    <b-modal
                      id="modal-prevent-closing"
                      ref="modal"
                      title="Login Form"
                      @show="resetModal"
                      @hidden="resetModal"
                      @ok="handleOk"
                    >
                      <div class="text-danger text-center" v-if="!userFound">
                        account not found
                      </div>
                      <div
                        class="text-danger text-center"
                        v-if="!serverNotError"
                      >
                        Server Error
                      </div>
                      <form ref="form" @submit.stop.prevent="handleSubmit">
                        <b-form-group
                          label="Username"
                          label-for="username-input"
                          invalid-feedback="username is required"
                          :state="nameState"
                        >
                          <b-form-input
                            id="username-input"
                            v-model="form.username"
                            :state="nameState"
                            required
                          ></b-form-input>
                        </b-form-group>
                      </form>
                      <form ref="form" @submit.stop.prevent="handleSubmit">
                        <b-form-group
                          label="password"
                          label-for="password-input"
                          invalid-feedback="password is required"
                        >
                          <VuePassword
                            id="password-input"
                            disableStrength
                            v-model="form.password"
                          />
                          <!-- <b-form-input
                            :type="passwordFieldType"
                            id="password-input"
                            v-model="form.password"
                            required
                          >
                          </b-form-input> -->
                        </b-form-group>
                      </form>
                      <!-- <b-card class="mt-3" header="Form Data Result">
                        <pre class="m-0">{{ form }}</pre>
                      </b-card> -->
                    </b-modal>
                  </div>
                </div>
              </div>
              <div class="mx-3 text-config" v-if="!isLogin()">Register</div>
              <div class="mx-3 text-config" v-if="isLogin()">
                <!-- welcome back {{ username }} -->
                <div class="dropdown">
                  <button class="dropbtn">Hi, {{ this.username }}</button>
                  <div class="dropdown-content">
                    <router-link to="/approval"> Approval </router-link>
                    <router-link to="/edited">Edited</router-link>
                    <router-link to="/deleted">Deleted</router-link>
                    <router-link to="/transaction">Transaction</router-link>
                    <hr />
                    <a href="#" @click="handleLogout">Logout</a>
                  </div>
                </div>
              </div>
              <div
                class="mx-3 text-config btn"
                v-if="isLogin()"
                @click="handleLogout"
              >
                Logout
              </div>
            </b-navbar-nav>
          </b-collapse>
        </b-navbar>
      </div>
    </b-container>
  </b-container>
</template>

<script>
import axios from "axios";
import { i18n } from "../i18n";
import VuePassword from "vue-password";

export default {
  name: "Header",
  data() {
    return {
      form: {},
      username: localStorage.getItem("username"),
      password: "",
      nameState: null,
      languages: [
        { language: "en", title: "English" },
        { language: "id", title: "Indonesia" },
      ],
      userFound: true,
      serverNotError: true,
    };
  },
  components: {
    VuePassword,
  },

  methods: {
    checkFormValidity() {
      const valid = this.$refs.form.checkValidity();
      this.nameState = valid;
      return valid;
    },
    resetModal() {
      this.name = "";
      this.nameState = null;
    },
    handleOk(bvModalEvt) {
      // Prevent modal from closing
      bvModalEvt.preventDefault();
      // Trigger submit handler
      this.handleSubmit();
    },
    handleSubmit() {
      // Exit when the form isn't valid
      if (!this.checkFormValidity()) {
        return;
      }
      // Post to server
      const url = "http://10.69.72.89:8081/pettycash/secured/login";
      axios
        .post(url, this.form)
        .then((res) => {
          // console.log(res.data);
          localStorage.setItem("token", res.data.token);
          localStorage.setItem("roleId", res.data.role.roleId);
          localStorage.setItem("userId", res.data.userId);
          localStorage.setItem("username", res.data.role.roleName);
          this.username = this.form.username;
          // console.log(this.username);
          // console.log(this.$router.currentRoute.path);
          if (this.$router.currentRoute.path !== "/transaction")
            this.$router.push("/transaction");
          else {
            if (res.status === 200) this.$router.go();
          }
        })
        .catch((err) => {
          if (Number(err.response.status === 500)) {
            this.userFound = false;
            setTimeout(() => {
              this.userFound = true;
            }, 2000);
          }
        });
      // Hide the modal manually
      // this.$nextTick(() => {
      //   this.$bvModal.hide("modal-prevent-closing");
      // });
    },

    isLogin() {
      if (localStorage.getItem("token") === null) return false;
      else return true;
    },

    handleLogout() {
      localStorage.clear();
      // console.log(localStorage.getItem("token"));
      this.$router.push("/");
      // this.$router.go();
    },

    changeLocale(locale) {
      i18n.locale = locale;
    },
  },
};
</script>

<style scoped>
.navigation-bar {
  background-color: #1a3150;
}

.brand-text {
  font-size: 2em;
}

.text-coloring {
  color: #fdcb5a;
}

.text-config {
  font-size: 1.15em;
  color: #fdcb5a;
}

.navbar-light .navbar-nav .nav-link {
  color: white;
}

a:hover {
  text-decoration: none;
}

.btn {
  color: #fdcb5a;
  font-size: 1.05em;
  padding-top: 0;
  padding-bottom: 0;
}

.dropbtn {
  background-color: #1a3150;
  color: #fdcb5a;
  border: none;
  cursor: pointer;
}
.dropdown {
  position: relative;
  display: inline-block;
}

.dropdown-content {
  display: none;
  position: absolute;
  background-color: #f9f9f9;
  min-width: 160px;
  box-shadow: 0px 8px 16px 0px rgba(0, 0, 0, 0.2);
  z-index: 1;
}

.dropdown-content a {
  color: black;
  padding: 12px 16px;
  text-decoration: none;
  display: block;
}

.dropdown-content a:hover {
  background-color: #f1f1f1;
}

.dropdown:hover .dropdown-content {
  display: block;
}
</style>
