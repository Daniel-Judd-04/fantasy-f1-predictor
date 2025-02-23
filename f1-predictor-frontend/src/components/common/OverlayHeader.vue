<script>
import {getFlagURL} from "@/utils/common";

export default {
  name: 'OverlayHeader',
  props: {
    country: {
      type: String,
      required: false,
      default: null,
    },
    deletable: {
      type: Boolean,
      required: false,
      default: false,
    },
  },
  methods: {
    getFlagURL,
    exit() {
      this.$emit('exit');
    },
    deleteObject() {
      this.$emit('deleteObject');
    },
  },
}
</script>

<template>
  <div class="hover-parent tw-p-2 tw-flex tw-justify-center tw-items-center tw-gap-2">
    <div v-if="country" class="flag-frame tw-border-1 tw-border-f1-white tw-overflow-hidden tw-h-10 tw-align-middle tw-rounded">
      <img :src="getFlagURL(country)" alt="Flag" class="flag-image"/>
    </div>
    <div class="tw-text-2xl tw-font-bold tw-pl-1 tw-pt-1">
      <slot></slot>
    </div>
    <div v-if="deletable" @click="deleteObject" class="tw-mt-1 tw-flex tw-items-center tw-cursor-pointer">
      <span class="hover-child tw-transition-opacity material-symbols-outlined tw-font-extralight tw-text-2xl tw-leading-none">delete</span>
    </div>
    <div @click="exit" class="tw-cursor-pointer tw-select-none tw-ml-auto tw-flex tw-justify-center tw-items-center tw-mr-2">
      <span class="material-symbols-outlined tw-font-light">close</span>
    </div>
  </div>
</template>

<style scoped>

</style>