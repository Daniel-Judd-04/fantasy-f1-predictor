<script>

export default {
  name: 'DriverDisplay',
  props: {
    driver: {
      default: () => ({team: {}}),
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
};
</script>

<template>
  <div
      :class="['tw-rounded', 'tw-mb-1', 'tw-text-f1-black', 'tw-bg-primary-light', `tw-border-team-${driver.team.shortName}`, 'tw-border-2']">
    <div class="tw-flex tw-flex-row tw-gap-2 tw-items-center tw-font-medium tw-px-2 tw-text-left">
      <div v-if="showFlag" class="flag-frame tw-my-1 tw-h-5 tw-align-middle tw-border-f1-black">
        <img :src="flagURL" alt="Flag" class="flag-image"/>
      </div>
      {{ driver.name.substring(0, 1) }}. {{ driver.name.substring(driver.name.split(' ')[0].length + 1) }}
    </div>
  </div>
</template>

<style>
.flag-frame {
  aspect-ratio: 16 / 9; /* Set the desired aspect ratio, e.g., 4:3 */
  overflow: hidden;
  border: 1px solid; /* Optional border for visual consistency */
}

.flag-image {
  width: 100%;
  height: 100%;
  object-fit: cover; /* Ensures flags fill the frame without stretching */
}
</style>