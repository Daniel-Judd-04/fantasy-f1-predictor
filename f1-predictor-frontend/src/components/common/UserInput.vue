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
    },
    placeholder: {
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
  <div class="tw-flex tw-gap-4">
    <div class="tw-mt-0.5">{{ name }}</div>
    <input :id="getId" :type="type" :value="defaultValue" :placeholder="placeholder"
           :step="step" :class="[`${type === 'number' ? 'tw-w-16 tw-text-center' : 'tw-w-52 tw-text-left'}`]"
           class="tw-ml-auto tw-px-2 tw-border-b-1 tw-border-l-1 tw-rounded-bl tw-border-primary-light tw-bg-primary-light tw-bg-opacity-25 tw-text-f1-white">
  </div>
</template>

<style scoped>

</style>