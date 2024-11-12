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

<template>
  <div v-if="team" class="tw-flex tw-border-1 tw-border-primary-light tw-rounded-lg tw-px-1 tw-py-1 tw-gap-2 tw-text-f1-white">
    <div class="tw-pl-2 tw-mt-0.5 tw-font-medium tw-mr-auto">
      {{ team.teamName }}
    </div>
    <div class="tw-mt-0.5">
      £{{ team.costCap }}m
    </div>
    <div class="tw-flex tw-gap-1">
      <div :class="`tw-to-team-${getConstructor(getDriver(driverId).constructor).shortName}`"
           class="tw-border-1 tw-border-primary-light tw-bg-gradient-to-bl tw-from-primary-dark tw-to-200% tw-rounded tw-w-16 tw-pt-1 tw-text-sm"
           v-for="driverId in team.drivers" :key="driverId">
        {{ getDriver(driverId).shortName }}
      </div>
    </div>
    <div class="tw-flex tw-gap-1">
      <div :class="`tw-to-team-${getConstructor(constructorId).shortName}`"
           class="tw-border-1 tw-border-primary-light tw-bg-gradient-to-bl tw-from-primary-dark tw-to-200% tw-rounded tw-w-20 tw-pt-1 tw-text-sm"
           v-for="constructorId in team.constructors"
           :key="constructorId">
        {{ getConstructor(constructorId).shortName }}
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>