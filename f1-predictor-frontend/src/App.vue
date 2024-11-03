<template>
  <div id="app" class="tw-min-h-screen tw-bg-f1-black">
    <div class="tw-absolute tw-left-0">
      <div class="tw-flex tw-flex-row tw-gap-4 tw-m-4">
        <TeamContainer/>
        <DriverContainer/>
        <GrandPrixDisplay :grand-prix="allGrandsPrix[0]"/>
        <GrandPrixDisplay :grand-prix="allGrandsPrix[1]"/>
      </div>
    </div>
  </div>
</template>

<script>
import DriverContainer from "@/components/DriverContainer.vue";
import {mapActions, mapGetters} from "vuex";
import TeamContainer from "@/components/ConstructorContainer.vue";
import GrandPrixDisplay from "@/components/GrandPrixDisplay.vue";

export default {
  name: 'App',
  created() {
    this.fetchDrivers();
    this.fetchConstructors();
    this.fetchCircuits();
    this.fetchGrandsPrix();
  },
  methods: {
    ...mapActions(['fetchDrivers', 'fetchConstructors', 'fetchCircuits', 'fetchGrandsPrix']),
  },
  computed: {
    ...mapGetters(['allCircuits', 'allGrandsPrix']),
  },
  components: {
    GrandPrixDisplay,
    DriverContainer,
    TeamContainer,
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
}
</style>
