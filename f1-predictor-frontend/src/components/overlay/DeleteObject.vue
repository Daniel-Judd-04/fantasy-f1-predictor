<script>

import OverlayHeader from "@/components/common/OverlayHeader.vue";
import CloseButton from "@/components/common/CloseButton.vue";
import ContinueButton from "@/components/common/ContinueButton.vue";
import {getObjectId, getObjectType} from "@/utils/common";
import {OverlayData} from "@/utils/classes";

export default {
  name: 'DeleteObject',
  props: {
    overlayData: {
      type: OverlayData,
      required: true,
      default: () => (new OverlayData({}, [], null)),
    },
  },
  methods: {
    getObjectType,
    async deleteObject() {
      const object = {
        id: getObjectId(this.overlayData.overlayObject),
        objectType: getObjectType(this.overlayData.overlayObject, true).split(' ').join('-').toLowerCase(),
      }
      console.log("Deleting with this data: ", object);
      if (await this.$store.dispatch('delete', object)) {
        this.$emit('success', `${getObjectType(object)} deleted successfully!`);
        this.exitAll();
      } else {
        this.$emit('error', `Failed to delete ${getObjectType(object)}`);
        console.log("Failed to delete object: ", object);
        this.exit();
      }
    },
    exit() {
      this.$emit('exit');
    },
    exitAll() {
      this.$emit('exitAll');
    }
  },
  components: {ContinueButton, OverlayHeader, CloseButton},
}
</script>

<template>
  <div
      class="tw-w-96 tw-bg-gradient-to-bl tw-to-100% tw-from-primary-dark tw-to-f1-red tw-outline tw-outline-1 -tw-outline-offset-1 tw-outline-primary-light tw-drop-shadow-2xl tw-rounded-lg tw-flex tw-flex-col tw-text-f1-white">
    <OverlayHeader @exit="exitAll">Delete {{ overlayData.overlayObject.fullName }}?</OverlayHeader>
    <div class="tw-rounded-b tw-bg-primary-dark tw-flex tw-flex-col">
      <div class="tw-p-4 tw-gap-2 tw-flex tw-flex-col tw-items-center">
        <span class="material-symbols-outlined tw-font-extralight tw-text-4xl">delete</span>
        <div class="tw-w-2/3 tw-font-light">Are you sure you want to delete this {{ getObjectType(overlayData.overlayObject) }}?</div>
      </div>
      <div class="tw-flex tw-gap-2 tw-p-2 tw-border-t-1 tw-border-primary-light">
        <ContinueButton class="tw-mr-auto tw-py-1 tw-px-2" @continue="exit">Cancel</ContinueButton>
        <CloseButton @close="deleteObject">Delete</CloseButton>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>