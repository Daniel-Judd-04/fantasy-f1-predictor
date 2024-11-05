<script>

export default {
  name: 'CloseButton',
  methods: {
    emitClose() {
      this.$emit('close');
    }
  }
}

</script>

<template>
  <button @click="emitClose()"
          class="tw-px-2 tw-h-10 tw-border-1 tw-border-f1-red tw-rounded tw-bg-f1-red tw-bg-opacity-15 hover:tw-bg-opacity-25 tw-transition-opacity">
    <slot></slot>
  </button>
</template>