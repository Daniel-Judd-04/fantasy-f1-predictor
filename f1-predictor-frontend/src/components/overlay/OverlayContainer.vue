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
      success: false,
      error: false,
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
      <EditObject v-if="isLoaded && startIndex !== -1" :objectArray="objectArray" :startIndex="startIndex"
                  @exit="exit" @success="showSuccessMessage" @error="showErrorMessage"/>
      <EditArray v-else-if="isLoaded" :objectArray="objectArray"
                 @exit="exit" @success="showSuccessMessage" @error="showErrorMessage"/>
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