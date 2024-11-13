import {createApp} from "vue";
import App from './App.vue';
import store from './store';
import PrimeVue from 'primevue/config';
import Chart from 'primevue/chart';
import './assets/tailwind.css';

const app = createApp(App);
app.use(store);
app.use(PrimeVue);
app.component('CustomChart', Chart);
app.mount('#app');