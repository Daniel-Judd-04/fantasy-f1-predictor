<script>

import DriverDisplay from "@/components/display/DriverDisplay.vue";
import {getFlagURL} from "@/utils/common";
import {mapGetters} from "vuex";
import ContinueButton from "@/components/common/ContinueButton.vue";

export default {
  name: 'DriverContainer',
  components: {ContinueButton, DriverDisplay},
  computed: {
    ...mapGetters(["allDrivers"]),
  },
  methods: {
    getFlagURL,
    edit(driver) {
      this.$emit('editObject', driver, this.allDrivers);
    },
    graph(driver) {
      this.$emit('showGraph', driver);
    },
    editDrivers() {
      this.$emit('editArray', this.allDrivers);
    }
  },
};

</script>

<template>
  <div class="tw-flex tw-flex-col tw-h-full tw-gap-2">
    <div class="hover-parent tw-border-primary-light tw-text-f1-white tw-relative tw-flex tw-flex-row tw-justify-center tw-items-center">
      <div class="tw-font-medium tw-text-xl">Drivers</div>
      <ContinueButton @continue="editDrivers" class="hover-child tw-absolute tw-right-0 tw-h-8">
        Edit
      </ContinueButton>
    </div>
    <div class="tw-w-56 tw-flex tw-flex-col tw-h-full tw-justify-between">
      <DriverDisplay @graph="graph" @edit="edit" v-for="driver in allDrivers" :key="driver.code" :driver="driver"
                     :flagURL="getFlagURL(driver.country)"/>
    </div>
  </div>
</template>

<style scoped>

</style>