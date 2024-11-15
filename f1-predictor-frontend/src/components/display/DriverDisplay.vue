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
    showFlag: {
      default: true,
      type: Boolean,
      required: false
    },
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
  <div class="tw-flex tw-items-center hover-parent tw-w-56 tw-rounded tw-h-8 tw-text-white tw-bg-primary-dark tw-border-1 tw-border-primary-light">
    <div :class="[`${driver.active ? '' : 'tw-text-primary-light'}`]"
         class="tw-flex tw-w-full tw-gap-2 tw-items-center tw-justify-center tw-font-medium tw-px-1 tw-text-left">
      <div v-if="showFlag" :class="[`tw-opacity-${driver.active ? '100' : '50'}`]" :title="driver.country"
           class="flag-frame tw-border-1 tw-overflow-hidden tw-h-5 tw-my-1 tw-align-middle tw-border-f1-white tw-rounded-sm">
        <img :src="flagURL" alt="Flag" class="flag-image"/>
      </div>
      <div class="tw-mt-0.5">
        {{ driver.fullName }}
      </div>
      <div class="tw-ml-auto tw-w-9 tw-h-5 tw-rounded tw-relative tw-mb-0.5">
        <div class="not-hover-child tw-text-right tw-absolute tw-w-full tw-h-full">
          {{ driver.fantasyPoints }}
        </div>
        <div class="tw-absolute tw-w-full tw-h-full tw-flex tw-gap-2 tw-items-center tw-justify-end">
          <div v-if="driver.raceResults" class="hover-child tw-transition-opacity tw-cursor-pointer tw-w-5 tw-h-5" title="Show Driver Graph">
            <span @click="graph()" class="material-symbols-outlined tw-w-5 tw-h-5 tw-flex tw-items-center tw-justify-center">bar_chart</span>
          </div>
          <div class="hover-child tw-transition-opacity tw-cursor-pointer tw-w-5 tw-h-5" title="Edit Driver">
            <span @click="edit()" class="material-symbols-outlined tw-w-5 tw-h-5 tw-flex tw-items-center tw-justify-center">tune</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>


<style>

</style>