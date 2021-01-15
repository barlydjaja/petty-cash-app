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
                <b-dropdown-item href="#">EN</b-dropdown-item>
                <b-dropdown-item href="#">ID</b-dropdown-item>
              </b-nav-item-dropdown>
            </b-navbar-nav>

            <!-- Right aligned nav items -->
            <b-navbar-nav class="ml-auto text-coloring">
              <div class="mx-3 text-config">Tentang</div>
              <router-link to="/demo">
                <div class="mx-3 text-config">
                  Demo
                </div>
              </router-link>
              <div class="mx-3 text-config">
                <Login />
              </div>
              <div class="mx-3 text-config">Register</div>
            </b-navbar-nav>
          </b-collapse>
        </b-navbar>
      </div>
    </b-container>
  </b-container>
</template>

<script>
import Login from "../components/Login";
export default {
  name: "Header",
  components: {
    Login,
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
</style>
