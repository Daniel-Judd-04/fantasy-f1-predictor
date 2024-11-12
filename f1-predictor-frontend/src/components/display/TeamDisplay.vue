<script>

import {getConstructor, getDriver} from "@/utils/common";

export default {
  name: 'TeamDisplay',
  methods: {getConstructor, getDriver},
  props: {
    team: {
      type: Object,
      required: true
    },
  },
}
</script>
<!-- transfer, cost cap-->
<template>
  <div v-if="team"
       class="container tw-h-64 tw-w-1/3 tw-border-primary-light tw-border-2 tw-rounded-lg tw-text-f1-white tw-bg-gradient-to-bl tw-to-f1-red tw-to-200% tw-from-primary-dark">
    <div class="tw-h-1/4 tw-flex tw-flex-row tw-py-1 tw-px-2 tw-border-b-1 tw-border-primary-light">
      <div class="tw-mr-auto tw-text-left">
        <div class="tw-font-bold tw-text-xl">{{ team.teamName }}</div>
        <div>{{ team.teamOwner }}</div>
      </div>
      <div v-if="team.activeChip !== 'None'" class="tw-h-full">
        <span class="material-symbols-outlined tw-mt-1">sprint</span>
      </div>
    </div>
    <div class="tw-h-3/4 tw-justify-between tw-flex tw-flex-col tw-gap-2 tw-bg-primary-dark tw-rounded-b-lg">
      <div class="tw-w-full tw-flex tw-px-2 tw-pt-1">
        <div class="tw-mr-auto">£{{ team.costCap }}m</div>
        <div>{{ team.freeTransfers }} Transfers</div>
      </div>
      <div v-if="team.drivers" class="tw-mt-auto tw-w-full tw-flex tw-justify-center tw-px-1 tw-gap-2">
        <div class="tw-w-1/3 tw-border-1 tw-border-primary-light tw-rounded tw-font-medium" v-for="driverId in team.drivers.slice(0, 2)" :key="driverId">
          <div class="tw-pl-1 tw-pt-0.5 tw-text-left tw-text-sm">{{ getDriver(driverId).fullName.split(' ')[1] }}</div>
          <div class="tw-border-t-1 tw-pt-0.5 tw-bg-primary tw-rounded-b tw-border-primary-light tw-px-1 tw-flex tw-justify-between tw-text-xs">
            <div>£{{ getDriver(driverId).fantasyPrice }}m</div>
            <div>{{ getDriver(driverId).fantasyPoints }}</div>
          </div>
        </div>
      </div>
      <div v-if="team.drivers" class="tw-w-full tw-flex tw-justify-center tw-px-1 tw-gap-2">
        <div class="tw-w-1/3 tw-border-1 tw-border-primary-light tw-rounded tw-font-medium" v-for="driverId in team.drivers.slice(2, 5)" :key="driverId">
          <div class="tw-pl-1 tw-pt-0.5 tw-text-left tw-text-sm">{{ getDriver(driverId).fullName.split(' ')[1] }}</div>
          <div class="tw-border-t-1 tw-pt-0.5 tw-bg-primary tw-rounded-b tw-border-primary-light tw-px-1 tw-flex tw-justify-between tw-text-xs">
            <div>£{{ getDriver(driverId).fantasyPrice }}m</div>
            <div>{{ getDriver(driverId).fantasyPoints }}</div>
          </div>
        </div>
      </div>
      <div v-if="team.constructors" class="tw-w-full tw-mb-1 tw-flex tw-justify-center tw-px-1 tw-gap-2">
        <div class="tw-w-1/2 tw-border-1 tw-border-primary-light tw-rounded tw-font-medium" v-for="constructorId in team.constructors" :key="constructorId">
          <div class="tw-pl-1 tw-pt-0.5 tw-text-left tw-text-sm">{{ getConstructor(constructorId).fullName }}</div>
          <div class="tw-border-t-1 tw-pt-0.5 tw-bg-primary tw-rounded-b tw-border-primary-light tw-px-1 tw-flex tw-justify-between tw-text-xs">
            <div>£{{ getConstructor(constructorId).fantasyPrice }}m</div>
            <div>{{ getConstructor(constructorId).fantasyPoints }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  min-width: 300px;
}
</style>