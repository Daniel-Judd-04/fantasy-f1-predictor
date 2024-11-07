<script>

export default {
  name: 'UserInput',
  props: {
    name: {
      type: String,
      required: true,
    },
    type: {
      default: 'text',
      type: String,
      required: false,
    },
    defaultValue: {
      type: String,
      required: false,
    },
    step: {
      default: 1,
      type: Number,
      required: false,
    },
    uniqueKey: {
      default: 0,
      type: Number,
      required: false,
    }
  },
  computed: {
    getId() {
      return this.name.charAt(0).toLowerCase() + this.name.substring(1).split(' ').join('');
    }
  }
}

</script>

<template>
  <div class="tw-flex tw-flex-row">
    <div class="tw-flex tw-items-center">{{ name }}</div>
    <input :id="getId" :type="type" :value="defaultValue"
           :step="step" :checked="defaultValue" :key="uniqueKey"
           :class="`${type === 'text' ? 'tw-w-44' : type === 'number' ? 'tw-w-16' : 'tw-h-6'}`"
           class="tw-ml-auto tw-border-1 tw-border-primary-light tw-rounded tw-bg-primary-light tw-bg-opacity-5 tw-text-f1-white tw-text-center">
  </div>
</template>

<style scoped>

</style>