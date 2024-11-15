<script>

import {getConciseName, getConstructorPoints, getDriver} from "@/utils/common";

export default {
  name: 'ConstructorDisplay',
  props: {
    constructor: {
      default: () => ({drivers: {}}),
      type: Object,
      required: true
    },
    flagURL: {
      default: "",
      type: String,
      required: false
    },
  },
  methods: {
    getConciseName,
    getDriver,
    getConstructorPoints,
    edit() {
      this.$emit('edit', this.constructor);
    },
    graph() {
      this.$emit('graph', this.constructor);
    },
  }
};
</script>

<template>
  <div v-if="constructor.active"
       class="tw-w-56 tw-h-18 tw-text-f1-white tw-bg-gradient-to-bl tw-from-primary-dark tw-border-1 tw-border-primary-light tw-rounded hover-parent tw-flex tw-flex-col tw-justify-between"
       :class="[`tw-to-team-${constructor.shortName}`]">
    <div class="tw-flex tw-flex-row tw-gap-2 tw-items-center tw-font-medium tw-px-1">
      <div :title="constructor.country"
           class="flag-frame tw-border-1 tw-border-f1-white tw-overflow-hidden tw-my-1 tw-h-5 tw-align-middle tw-rounded-sm">
        <img :src="flagURL" alt="Flag" class="flag-image"/>
      </div>
      <div>
        {{ constructor.fullName }}
      </div>
      <div class="tw-ml-auto tw-w-9 tw-h-5 tw-rounded tw-relative">
        <div class="not-hover-child tw-text-right tw-absolute tw-w-full tw-h-full">
          {{ constructor.fantasyPoints }}
        </div>
        <div class="tw-absolute tw-w-full tw-h-full tw-flex tw-gap-2 tw-items-center tw-justify-end">
          <div v-if="constructor.raceResults" class="hover-child tw-transition-opacity tw-cursor-pointer tw-w-5 tw-h-5" title="Show Constructor Graph">
            <span @click="graph()" class="material-symbols-outlined tw-w-5 tw-h-5 tw-flex tw-items-center tw-justify-center">bar_chart</span>
          </div>
          <div class="hover-child tw-transition-opacity tw-cursor-pointer tw-w-5 tw-h-5" title="Edit Constructor">
            <span @click="edit()" class="material-symbols-outlined tw-w-5 tw-h-5 tw-flex tw-items-center tw-justify-center">tune</span>
          </div>
        </div>
      </div>
    </div>
    <div class="tw-bg-primary-dark tw-rounded-b tw-w-full tw-h-full tw-flex tw-flex-row tw-gap-2 tw-items-center tw-pl-2 tw-pr-1">
      <div
          class="tw-h-full tw-text-left tw-text-xs tw-flex tw-flex-col tw-place-content-between tw-py-1">
        <div v-for="driverId in constructor.drivers" :key="driverId">
          {{ getConciseName(getDriver(driverId).fullName) }}
        </div>
      </div>
      <div
          class="tw-h-full tw-text-left tw-text-xs tw-flex tw-flex-col tw-place-content-between tw-py-1">
        <div v-for="driverId in constructor.drivers" :key="driverId">
          {{ getDriver(driverId).points }}
        </div>
      </div>
      <div
          class="tw-h-full tw-ml-auto tw-font-medium tw-flex tw-items-end tw-justify-end">
        {{ getConstructorPoints(constructor.constructorId) }}
      </div>
    </div>

  </div>
</template>

<style scoped>

</style>