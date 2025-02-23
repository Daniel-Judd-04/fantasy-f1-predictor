<script>
import EditObject from "@/components/overlay/EditObject.vue";
import EditArray from "@/components/overlay/EditArray.vue";
import ShowGraph from "@/components/overlay/ShowGraph.vue";
import AddObject from "@/components/overlay/AddObject.vue";
import {OverlayData} from "@/utils/classes";
import DeleteObject from "@/components/overlay/DeleteObject.vue";
import ShowGrandPrixResults from "@/components/overlay/ShowGrandPrixResults.vue";

export default {
  name: 'OverlayContainer',
  props: {
    overlayData: {
      type: OverlayData,
      required: true,
      default: () => (new OverlayData({}, [], null)),
    },
  },
  data() {
    return {
      overlayQueue: [],
      success: false,
      error: false,
      notificationDescription: 'This is a description of the notification',
    }
  },
  watch: {
    overlayData: {
      immediate: true,
      handler() {
        let newOverlay = this.overlayData;
        newOverlay.overlayDepth = this.overlayQueue.length + 1;
        this.overlayQueue.push(newOverlay);
        console.log(this.overlayQueue);
      }
    },
  },
  components: {ShowGrandPrixResults, DeleteObject, AddObject, ShowGraph, EditObject, EditArray},
  computed: {
    isLoaded() {
      return this.overlayData.overlayArray.length > 0 || this.overlayData.overlayObject !== {};
    },
  },
  methods: {
    openOverlay(data) {
      this.overlayQueue.push(data);
      console.log(this.overlayQueue);
    },
    showSuccessMessage(desc = "Data saved successfully.") {
      this.notificationDescription = desc;

      this.success = true;
      setTimeout(() => {
        this.success = false;
      }, 2000);
    },
    showErrorMessage(desc = "Error saving data.") {
      this.notificationDescription = desc;

      this.error = true;
      setTimeout(() => {
        this.error = false;
      }, 4000);
    },
    exit() {
      this.overlayQueue.pop();
      if (this.overlayQueue.length === 0) {
        this.$emit('exit');
      }
    },
    exitAll() {
      this.overlayQueue = [];
      this.$emit('exit');
    }
  }
}

</script>

<template>
  <div class="tw-absolute tw-h-screen tw-w-screen tw-top-0 tw-left-0 tw-backdrop-blur-md tw-bg-primary-light tw-bg-opacity-15">
    <div class="tw-absolute tw-top-4 tw-w-full">
      <div class="tw-flex tw-w-full tw-justify-center tw-items-center">
        <Transition>
          <div id="overlay-success" v-if="success"
               class="tw-flex tw-flex-col tw-items-center tw-gap-1 tw-max-w-52 tw-bg-green-900 tw-border-2 tw-border-green-500 tw-p-2 tw-pt-1 tw-rounded-lg tw-text-green-500 tw-transition-opacity">
            <div class="tw-flex tw-gap-1 tw-items-center">
              <span class="material-symbols-outlined tw-text-2xl tw-font-light tw-leading-none">verified_user</span>
              <div class="tw-mt-0.5 tw-font-medium tw-leading-none">
                Saved Successfully!
              </div>
            </div>
            <div class="tw-text-sm tw-font-light tw-leading-none tw-italic">
              {{ notificationDescription }}
            </div>
          </div>
        </Transition>
        <Transition>
          <div id="overlay-error" v-if="error"
               class="tw-flex tw-flex-col tw-items-center tw-gap-1 tw-max-w-52 tw-bg-red-900 tw-border-2 tw-border-red-500 tw-p-2 tw-pt-1 tw-rounded-lg tw-text-red-400 tw-transition-opacity">
            <div class="tw-flex tw-gap-1 tw-items-center">
              <span class="material-symbols-outlined tw-text-2xl tw-font-light tw-leading-none">gpp_maybe</span>
              <div class="tw-mt-0.5 tw-font-medium tw-leading-none">
                Error Saving!
              </div>
            </div>
            <div class="tw-text-sm tw-font-light tw-leading-none tw-italic">
              {{ notificationDescription }}
            </div>
          </div>
        </Transition>
      </div>
    </div>
    <div v-for="overlayData in overlayQueue" :key="overlayData.code" :class="[overlayData === overlayQueue[overlayQueue.length-1] ? '' : 'tw-hidden']"
         class="tw-flex tw-w-full tw-h-full tw-justify-center tw-items-center">
      <AddObject v-if="isLoaded && overlayData.overlayType === 'AddObject'"
                 :overlay-data="overlayData"
                 @exit="exit" @exitAll="exitAll" @success="showSuccessMessage" @error="showErrorMessage" @openOverlay="openOverlay"/>
      <EditObject v-else-if="isLoaded && overlayData.overlayType === 'EditObject'"
                  :overlay-data="overlayData"
                  @exit="exit" @exitAll="exitAll" @success="showSuccessMessage" @error="showErrorMessage" @openOverlay="openOverlay"/>
      <EditArray v-else-if="isLoaded && overlayData.overlayType === 'EditArray'"
                 :overlay-data="overlayData"
                 @exit="exit" @exitAll="exitAll" @success="showSuccessMessage" @error="showErrorMessage" @openOverlay="openOverlay"/>
      <ShowGraph v-else-if="isLoaded && overlayData.overlayType === 'ShowGraph'"
                 :overlay-data="overlayData"
                 @exit="exit" @exitAll="exitAll" @success="showSuccessMessage" @error="showErrorMessage" @openOverlay="openOverlay"/>
      <ShowGrandPrixResults v-else-if="isLoaded && overlayData.overlayType === 'ShowGrandPrixResults'"
                            :overlay-data="overlayData"
                            @exit="exit" @exitAll="exitAll" @success="showSuccessMessage" @error="showErrorMessage" @openOverlay="openOverlay"/>
      <DeleteObject v-else-if="isLoaded && overlayData.overlayType === 'DeleteObject'"
                    :overlay-data="overlayData"
                    @exit="exit" @exitAll="exitAll" @success="showSuccessMessage" @error="showErrorMessage" @openOverlay="openOverlay"/>
    </div>
  </div>
</template>

<style scoped>
.v-leave-active {
  transition: opacity .5s ease;
}

.v-enter-from,
.v-leave-to {
  opacity: 0;
}
</style>