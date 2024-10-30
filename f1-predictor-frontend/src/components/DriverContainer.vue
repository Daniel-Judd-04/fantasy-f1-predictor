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
  <div>
    <div class="tw-flex tw-flex-row">
      <div class="tw-p-4 tw-bg-primary">
        <div>Drivers:</div>
        <DriverDisplay v-for="driver in allDrivers" :key="driver.code" :driver="driver"
                       :flagURL="getFlagURL(driver.country)"/>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>