import Vue from "vue";
import VueI18n from "vue-i18n";
Vue.use(VueI18n);

const messages = {
  en: {
    // about us
    about: "About Us",
    companyProfile: "Company Profile",
    companyHistory: "Company History",
    career: "karir",
    // sosmed
    sosmed: "Social Media",
    //qna
    howItWorks: "How It Works",
    misscalculation: "Misscalculation",
    others: "others",
    //others
    title: "Manage Your Money",
    subTitle: "make money management easier",
    procedure: "How Petty Cash Works",
    loginRegister: "Login/Register",
    income: "Input Income",
    expenses: "Input Expenses",
    transaction: "Check Transaction",
    startNow: "START USING NOW!",
    newTransaction: "New Transaction",
    date: "Tanggal",
    transaction2: "Transaction",
    description: "Description",
    mutation: "Mutation",
    endBalance: "End Balance",
  },
  id: {
    about: "Tentang",
    companyProfile: "Profil Perusahaan",
    companyHistory: "Sejarah Perusahaan",
    career: "Carrer",
    sosmed: "Media Sosial",
    howItWorks: "Cara Kerja",
    misscalculation: "Kendala Perhitungan",
    others: "Lainnya",
    title: "Atur Keuanganmu",
    subTitle: "Tak Perlu Repot Membuat Data Keuanganmu",
    procedure: "Cara Kerja Petty Cash",
    loginRegister: "Masuk/Daftar",
    income: "Masukan Pemasukan",
    expenses: "Masukan Pengeluaran",
    transaction: "Lihat Transaksi",
    startNow: "MULAI SEKARANG",
    newTransaction: "Transaksi Baru",
    date: "Tanggal",
    transaction2: "Transaksi",
    descrition: "Deskripsi",
    mutation: "Mutasi",
    endBalance: "Sisa Saldo",
  },
};

export const i18n = new VueI18n({
  locale: "en",
  fallbackLocale: "id",
  messages,
});
