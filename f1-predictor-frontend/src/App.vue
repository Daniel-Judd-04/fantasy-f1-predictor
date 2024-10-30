<template>
  <div id="app" class="tw-min-h-screen tw-bg-f1-black tw-flex tw-items-center tw-justify-center">
    <div class="">
      <div class="tw-flex tw-flex-row">
        <DriverContainer/>
        <TeamContainer/>
      </div>
    </div>
  </div>
</template>

<script>
import DriverContainer from "@/components/DriverContainer.vue";
import {mapActions} from "vuex";
import TeamContainer from "@/components/TeamContainer.vue";

export default {
  name: 'App',
  created() {
    this.fetchDrivers();
    this.fetchTeams();
  },
  methods: {
    ...mapActions(['fetchDrivers', 'fetchTeams']),
  },
  components: {
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
