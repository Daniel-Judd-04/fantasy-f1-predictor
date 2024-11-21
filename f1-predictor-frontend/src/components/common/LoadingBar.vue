<script>

export default {
  name: 'LoadingBar',
  props: {
    stage: {
      type: Number,
      required: true,
    },
    maxStage: {
      type: Number,
      required: true,
    },
    success: {
      type: Boolean,
      required: true,
    }
  }
}

</script>

<template>
  <div :style="{ width: `${(stage / maxStage) * 90}%` }"
       :class="`${success ? 'tw-from-primary-light' : 'tw-from-red-600'}`"
       class="tw-transition-all tw-top-0 tw-left-0 tw-absolute tw-z-0 tw-h-1 tw-bg-gradient-to-l tw-to-primary-dark tw-rounded-r-full">
    <span class="tw-bg-primary-light tw-bg-red-600"></span>
  </div>
</template>

<style scoped>

</style>