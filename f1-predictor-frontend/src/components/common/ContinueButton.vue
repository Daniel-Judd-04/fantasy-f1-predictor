<script>

export default {
  name: 'ContinueButton',
  methods: {
    emitContinue() {
      this.$emit('continue');
    }
  }
}

</script>

<template>
  <button @click="emitContinue()"
          class="tw-select-none tw-flex tw-justify-center tw-items-center tw-px-2 tw-border-1 tw-border-primary-light tw-rounded tw-bg-primary-light tw-bg-opacity-15 hover:tw-bg-opacity-25">
    <slot></slot>
  </button>
</template>

<style scoped>

</style>