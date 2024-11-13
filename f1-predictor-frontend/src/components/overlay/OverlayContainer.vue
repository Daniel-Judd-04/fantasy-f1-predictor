<script>
import EditObject from "@/components/overlay/EditObject.vue";
import EditArray from "@/components/overlay/EditArray.vue";
import ShowGraph from "@/components/overlay/ShowGraph.vue";

export default {
  name: 'OverlayContainer',
  props: {
    overlayArray: {
      type: Array,
      required: true,
    },
    overlayObject: {
      type: Object,
      required: true,
    },
    startIndex: {
      type: Number,
      required: true,
    },
    overlayType: {
      type: String,
      required: true,
    }
  },
  data() {
    return {
      success: false,
      error: false,
    }
  },
  components: {ShowGraph, EditObject, EditArray},
  computed: {
    isLoaded() {
      return this.overlayArray.length > 0 || this.overlayObject !== {};
    },
  },
  methods: {
    showSuccessMessage() {
      this.success = true;
      setTimeout(() => {
        this.success = false;
      }, 1000);
    },
    showErrorMessage() {
      this.error = true;
      setTimeout(() => {
        this.error = false;
      }, 1000);
    },
    exit() {
      this.$emit('exit');
    },
  }
}

</script>

<template>
  <div class="tw-absolute tw-h-screen tw-w-screen tw-top-0 tw-left-0 tw-backdrop-blur-md tw-bg-primary-light tw-bg-opacity-15">
    <div class="tw-absolute tw-top-4 tw-w-full">
      <div class="tw-flex tw-w-full tw-justify-center tw-items-center">
        <Transition>
          <div v-if="success" class="tw-bg-green-900 tw-border-2 tw-border-green-500 tw-px-2 tw-py-1 tw-rounded tw-text-green-500 tw-transition-opacity">
            Saved Successfully!
          </div>
        </Transition>
        <Transition>
          <div v-if="error" class="tw-bg-red-900 tw-border-2 tw-border-red-500 tw-px-2 tw-py-1 tw-rounded tw-text-red-500 tw-transition-opacity">
            Error Saving!
          </div>
        </Transition>
      </div>
    </div>
    <div class="tw-flex tw-w-full tw-h-full tw-justify-center tw-items-center">
      <EditObject v-if="isLoaded && overlayType === 'EditObject'" :overlayArray="overlayArray" :startIndex="startIndex"
                  @exit="exit" @success="showSuccessMessage" @error="showErrorMessage"/>
      <EditArray v-else-if="isLoaded && overlayType === 'EditArray'" :overlayArray="overlayArray"
                 @exit="exit" @success="showSuccessMessage" @error="showErrorMessage"/>
      <ShowGraph v-else-if="isLoaded && overlayType === 'ShowGraph'" :overlayObject="overlayObject"
                 @exit="exit"/>
    </div>
  </div>
</template>

<style scoped>
.v-leave-active {
  transition: opacity 0.5s ease;
}

.v-enter-from,
.v-leave-to {
  opacity: 0;
}
</style>