<script>

import {getConstructor} from "@/utils/common";

export default {
  name: 'DriverDisplay',
  props: {
    driver: {
      type: Object,
      required: true
    },
    flagURL: {
      default: "",
      type: String,
      required: false
    },
    collapsed: {
      default: false,
      type: Boolean,
      required: false
    }
  },
  methods: {
    getConstructor,
    edit() {
      this.$emit('edit', this.driver);
    },
    graph() {
      this.$emit('graph', this.driver);
    },
  }
};
</script>

<template>
  <div :class="[`${driver.active ? '' : 'tw-opacity-50'}`]"
       class="tw-flex tw-items-center hover-parent tw-rounded tw-text-f1-white tw-bg-primary-dark tw-outline tw-outline-1 tw-outline-primary-light -tw-outline-offset-1">
    <div class="tw-flex tw-w-full tw-items-center tw-justify-center tw-font-medium tw-px-1 tw-text-left">
      <div :class="[`${collapsed ? 'tw-invisible tw-w-0' : 'tw-mr-2'}`]" :title="driver.country"
           class="flag-frame tw-border-1 tw-overflow-hidden tw-h-5 tw-my-1 tw-align-middle tw-border-f1-white tw-rounded-sm">
        <img :src="flagURL" alt="Flag" class="flag-image"/>
      </div>
      <div class="tw-mt-0.5">
        <div>{{ collapsed ? driver.shortName : driver.fullName }}</div>
      </div>
      <div class="tw-ml-auto tw-relative">
        <div :class="`${collapsed ? 'tw-text-xs' : ''}`" class="not-hover-child tw-text-right tw-absolute tw-w-full tw-h-full tw-flex tw-justify-end tw-items-center">
          <div class="tw-mt-0.5 tw-mr-0.5">{{ driver.fantasyPoints }}</div>
        </div>
        <div class="hover-child tw-transition-opacity tw-absolute tw-w-full tw-h-full tw-flex tw-gap-1 tw-items-center tw-justify-end">
          <div v-if="driver.results" title="Show Driver Graph">
            <span @click="graph()" class="material-symbols-outlined tw-flex tw-items-center tw-justify-center tw-cursor-pointer tw-text-xl">bar_chart</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>