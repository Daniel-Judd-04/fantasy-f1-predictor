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
          class="tw-px-4 tw-h-10 tw-border-1 tw-border-primary-light tw-rounded tw-bg-primary-light tw-bg-opacity-15 hover:tw-bg-opacity-25 tw-transition-opacity">
    <slot></slot>
  </button>
</template>

<style scoped>

</style>