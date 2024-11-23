<script>

import DriverDisplay from "@/components/display/DriverDisplay.vue";
import {getFlagURL} from "@/utils/common";
import {mapGetters} from "vuex";
import ContinueButton from "@/components/common/ContinueButton.vue";

export default {
  name: 'DriverContainer',
  components: {ContinueButton, DriverDisplay},
  data() {
    return {
      collapsed: false,
    }
  },
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
  <div :class="`${collapsed ? 'tw-w-20 tw-transition-all' : 'tw-w-56'}`" class="tw-flex tw-flex-col tw-h-full tw-gap-2">
    <div class="hover-parent tw-border-primary-light tw-text-f1-white tw-relative tw-flex tw-flex-row tw-justify-between tw-items-center">
      <ContinueButton @continue="collapsed = !collapsed" class="hover-child tw-transition-opacity tw-px-1">
        <span class="material-symbols-outlined tw-text-xl">{{ collapsed ? 'chevron_left' : 'chevron_right' }}</span>
      </ContinueButton>
      <div :class="`${collapsed ? 'not-hover-child tw-absolute tw-left-0 tw-right-0 tw-text-xs tw-pointer-events-none' : 'tw-text-xl'}`" class="tw-font-bold tw-mt-0.5">
        Drivers
      </div>
      <ContinueButton @continue="editDrivers" class="hover-child tw-transition-opacity tw-px-1">
        <span class="material-symbols-outlined tw-text-xl">edit_note</span>
      </ContinueButton>
    </div>
    <div :class="`${collapsed ? 'tw-w-20 tw-transition-all' : 'tw-w-56'}`" class="tw-flex tw-flex-col tw-h-full tw-justify-between">
      <DriverDisplay @graph="graph" @edit="edit" v-for="driver in allDrivers" :key="driver.code"
                     :driver="driver" :flagURL="getFlagURL(driver.country)" :collapsed="collapsed"/>
    </div>
  </div>
</template>

<style scoped>

</style>