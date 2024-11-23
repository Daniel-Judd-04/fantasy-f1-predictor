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
    collapsed: {
      default: false,
      type: Boolean,
      required: false
    }
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
       class="tw-h-18 tw-text-f1-white tw-bg-gradient-to-bl tw-from-primary-dark tw-outline tw-outline-1 tw-outline-primary-light -tw-outline-offset-1 tw-rounded hover-parent tw-flex tw-flex-col tw-justify-between"
       :class="[`tw-to-team-${constructor.shortName}`]">
    <div class="tw-flex tw-flex-row tw-items-center tw-font-medium tw-px-1">
      <div :class="`${collapsed ? 'tw-invisible tw-w-0' : 'tw-mr-2'}`" :title="constructor.country"
           class="flag-frame tw-border-1 tw-border-f1-white tw-overflow-hidden tw-my-1 tw-h-5 tw-align-middle tw-rounded-sm">
        <img :src="flagURL" alt="Flag" class="flag-image"/>
      </div>
      <div class="tw-mt-0.5">
        {{ collapsed ? constructor.shortName : constructor.fullName }}
      </div>
      <div class="tw-ml-auto tw-relative">
        <div :class="`${collapsed ? 'tw-text-xs' : 'not-hover-child'}`"
             class="tw-text-right tw-mt-0.5 tw-absolute tw-w-full tw-h-full tw-flex tw-justify-end tw-items-center">
          <div class="tw-mr-0.5">{{ constructor.fantasyPoints }}</div>
        </div>
        <div v-if="!collapsed" class="hover-child tw-transition-opacity tw-absolute tw-w-full tw-h-full tw-flex tw-gap-1 tw-items-center tw-justify-end">
          <div v-if="constructor.raceResults" title="Show Constructor Graph">
            <span @click="graph()" class="material-symbols-outlined tw-flex tw-items-center tw-justify-center tw-cursor-pointer tw-text-xl">bar_chart</span>
          </div>
          <div title="Edit Constructor">
            <span @click="edit()" class="material-symbols-outlined tw-flex tw-items-center tw-justify-center tw-cursor-pointer tw-text-xl">tune</span>
          </div>
        </div>
      </div>
    </div>
    <div class="tw-bg-primary-dark tw-rounded-b tw-w-full tw-h-full tw-flex tw-items-center tw-pl-2 tw-pr-1">
      <div class="tw-h-full tw-w-1/2 tw-text-left tw-text-xs tw-flex tw-flex-col tw-place-content-between tw-py-1">
        <div class="tw-w-full tw-flex tw-justify-between"
             v-for="driverId in constructor.drivers.filter(id => getDriver(id).active).sort((a, b) => getDriver(b).points - getDriver(a).points)" :key="driverId">
          <div>{{ collapsed ? getDriver(driverId).shortName : getDriver(driverId).fullName.split(' ')[1] }}</div>
          <div v-if="!collapsed">{{ getDriver(driverId).points }}</div>
        </div>
      </div>
      <div :class="`${collapsed ? 'tw-pr-0.5' : 'tw-text-lg tw-pr-1'}`" class="tw-h-full tw-ml-auto tw-font-normal tw-flex tw-items-end tw-justify-end">
        {{ getConstructorPoints(constructor.constructorId) }}
      </div>
    </div>

  </div>
</template>

<style scoped>

</style>