<script>

import {getConciseName} from "@/utils/common";

export default {
  name: 'TeamDisplay',
  props: {
    team: {
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
  }
};
</script>

<template>
  <div
      class="tw-w-50 tw-h-18 tw-text-f1-white tw-bg-gradient-to-br tw-from-primary-dark tw-to-80% tw-border-2 tw-border-f1-white tw-rounded hover-parent tw-flex tw-flex-col tw-justify-between"
      :class="[`tw-to-team-${team.shortName}`]">
    <div class="tw-flex tw-flex-row tw-gap-2 tw-items-center tw-font-medium tw-px-1">
      <div class="flag-frame tw-my-1 tw-h-5 tw-align-middle tw-border-f1-white tw-rounded">
        <img :src="flagURL" alt="Flag" class="flag-image"/>
      </div>
      {{ team.name }}
      <div class="tw-ml-auto tw-w-5 tw-h-5 tw-rounded">
        <span
            class="material-icons tw-transition-opacity tw-opacity-0 tw-w-full tw-h-full tw-flex tw-items-center tw-justify-center hover-child">
          tune
        </span>
      </div>
    </div>
    <div class="tw-bg-amber-300 tw-w-full tw-h-full">

    </div>
  </div>
</template>

<style scoped>

</style>