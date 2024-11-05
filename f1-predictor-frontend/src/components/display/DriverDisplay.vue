<script>

import {getConstructor} from "@/utils/common";

export default {
  name: 'DriverDisplay',
  props: {
    driver: {
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
  methods: {
    getConstructor,
    edit() {
      this.$emit('edit', this.driver);
    },
  }
};
</script>

<template>
  <div
      class="hover-parent tw-w-56 tw-rounded tw-h-8 tw-text-white tw-bg-gradient-to-b tw-from-primary-dark tw-to-200% tw-border-1 tw-border-primary-light"
      :class="`tw-to-team-${getConstructor(driver.constructor).shortName}`">
    <div class="tw-flex tw-flex-row tw-gap-2 tw-items-center tw-font-medium tw-px-1 tw-text-left">
      <div v-if="showFlag" class="flag-frame tw-my-1 tw-h-5 tw-align-middle tw-border-f1-white tw-rounded">
        <img :src="flagURL" alt="Flag" class="flag-image"/>
      </div>
      <div>
        {{ driver.fullName }}
      </div>
      <div class="tw-ml-auto tw-w-9 tw-h-5 tw-rounded tw-relative">
        <div class="not-hover-child tw-text-right tw-absolute tw-w-full tw-h-full">
          {{ driver.fantasyPoints }}
        </div>
        <div
            class="hover-child tw-absolute tw-transition-opacity tw-opacity-0 tw-w-full tw-h-full tw-flex tw-items-center tw-justify-end">
          <span @click="edit()"
                class="material-icons tw-w-5 tw-h-5 tw-flex tw-items-center tw-justify-center tw-cursor-pointer">tune</span>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>


<style>
/* Custom hover styles */
.hover-parent:hover .hover-child {
  opacity: 1;
}

.hover-parent:hover .not-hover-child {
  opacity: 0;
}

.flag-frame {
  aspect-ratio: 4 / 3;
  overflow: hidden;
  border: 1px solid;
}

.flag-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>