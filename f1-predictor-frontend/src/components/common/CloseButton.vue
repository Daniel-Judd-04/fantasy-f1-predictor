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
          class="tw-select-none tw-flex tw-justify-center tw-items-center tw-px-2 tw-border-1 tw-border-f1-red tw-rounded tw-bg-f1-red tw-bg-opacity-15 hover:tw-bg-opacity-25">
    <slot></slot>
  </button>
</template>