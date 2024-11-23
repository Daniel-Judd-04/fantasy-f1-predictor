<script>

export default {
  name: 'UserInput',
  props: {
    name: {
      default: '',
      type: String,
      required: false,
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
    },
    fallBackId: {
      default: '',
      type: String,
      required: false,
    }
  },
  computed: {
    getId() {
      if (this.name) {
        return this.name.charAt(0).toLowerCase() + this.name.substring(1).split(' ').join('');
      }
      return this.fallBackId;
    }
  }
}

</script>

<template>
  <div class="tw-flex">
    <div class="">{{ name }}</div>
    <input :id="getId" :type="type" :value="defaultValue"
           :step="step" :class="`${type === 'text' ? 'tw-w-52' : 'tw-w-16'}`"
           class="tw-ml-auto tw-border-1 tw-border-primary-light tw-rounded tw-bg-primary-light tw-bg-opacity-5 tw-text-f1-white tw-text-center">
  </div>
</template>

<style scoped>

</style>