<script>
import EditObject from "@/components/overlay/EditObject.vue";
import EditArray from "@/components/overlay/EditArray.vue";

export default {
  name: 'OverlayContainer',
  props: {
    objectArray: {
      type: Array,
      required: true,
    },
    startIndex: { // -1 for array editing rather than a specific index
      type: Number,
      required: true,
    }
  },
  data() {
    return {
      saved: false,
    }
  },
  components: {EditObject, EditArray},
  computed: {
    isLoaded() {
      return this.objectArray.length > 0;
    },
  },
  methods: {
    showSuccessMessage() {
      this.saved = true;
      setTimeout(() => {
        this.saved = false;
      }, 500);
    },
    exit() {
      this.$emit('exit');
    },
  }
}

</script>

<template>
  <div
      class="tw-absolute tw-h-screen tw-w-screen tw-top-0 tw-left-0 tw-backdrop-blur-md tw-bg-f1-white tw-bg-opacity-15">
    <div class="tw-absolute tw-top-4 tw-w-full">
      <div class="tw-flex tw-w-full tw-justify-center tw-items-center">
        <div v-if="saved" class="tw-bg-green-900 tw-border-2 tw-border-green-500 tw-px-2 tw-py-1 tw-rounded tw-text-green-500 tw-transition-opacity">
          Success!
        </div>
      </div>
    </div>
    <div class="tw-flex tw-w-full tw-h-full tw-justify-center tw-items-center">
      <EditObject v-if="isLoaded && startIndex !== -1" :objectArray="objectArray" :startIndex="startIndex"
                  @exit="exit" @success="showSuccessMessage"/>
      <EditArray v-else-if="isLoaded" :objectArray="objectArray"
                 @exit="exit" @success="showSuccessMessage"/>
    </div>
  </div>
</template>

<style scoped>

</style>