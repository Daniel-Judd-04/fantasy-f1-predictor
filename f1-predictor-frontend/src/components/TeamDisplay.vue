<script>

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
};
</script>

<template>
  <div
      :class="['tw-mb-1', 'tw-text-f1-black', 'tw-bg-primary-light', `tw-border-team-${team.shortName}`, 'tw-border-2']">
    <div class="tw-flex tw-flex-row tw-gap-2 tw-items-center tw-font-medium tw-px-2">
      {{ team.name }}
      <div class="flag-frame tw-my-1 tw-h-5 tw-ml-auto tw-align-middle tw-border-f1-black">
        <img :src="flagURL" alt="Flag" class="flag-image"/>
      </div>
    </div>
    <div v-for="driver in team.drivers" :key="driver.id">
      {{ driver.name }}
    </div>
  </div>
</template>

<style scoped>

</style>