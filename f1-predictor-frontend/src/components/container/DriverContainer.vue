<script>

import DriverDisplay from "@/components/DriverDisplay.vue";
import {getFlagURL} from "@/utils/common";
import {mapGetters} from "vuex";

export default {
  name: 'DriverContainer',
  components: {DriverDisplay},
  computed: {
    ...mapGetters(["allDrivers"]),
  },
  methods: {
    getFlagURL,
  },
};

</script>

<template>
  <div class="tw-flex tw-flex-col tw-h-full tw-justify-between">
    <DriverDisplay v-for="driver in allDrivers" :key="driver.code" :driver="driver"
                   :flagURL="getFlagURL(driver.country)"/>
  </div>
</template>

<style scoped>

</style>