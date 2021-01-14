<script>
export default {
  name: "Helper",
  methods: {
    checkLogin: function() {
      if (localStorage.getItem("token") === null) return false;
      else return true;
    },
    checkStaff: function() {
      if (localStorage.getItem("role" === "admin")) return true;
      else return false;
    },
    checkHead: function() {
      if (localStorage.getItem("role" === "head")) return true;
      else return false;
    },
  },
};
</script>
