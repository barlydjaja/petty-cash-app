import Vue from "vue";
import VueI18n from "vue-i18n";
Vue.use(VueI18n);

const messages = {
  en: {
    title: "Manage Your Money",
    subTitle: "make money management easier",
    procedure: "How Petty Cash Works",
    loginRegister: "Login/Register",
    income: "Input Income",
    expenses: "Input Expenses",
    transaction: "Check Transaction",
    startNow: "START USING NOW!",
  },
  id: {
    title: "Atur Keuanganmu",
    subTitle: "Tak Perlu Repot Membuat Data Keuanganmu",
    procedure: "Cara Kerja Petty Cash",
    loginRegister: "Masuk/Daftar",
    income: "Masukan Pemasukan",
    expenses: "Masukan Pengeluaran",
    transaction: "Lihat Transaksi",
    startNow: "MULAI SEKARANG",
  },
};

export const i18n = new VueI18n({
  locale: "en",
  fallbackLocale: "id",
  messages,
});
