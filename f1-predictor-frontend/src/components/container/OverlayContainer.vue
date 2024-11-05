<script>

import CloseButton from "@/components/common/CloseButton.vue";
import ContinueButton from "@/components/common/ContinueButton.vue";
import {mapActions} from "vuex";

export default {
  name: 'OverlayContainer',
  props: {
    objectArray: {
      type: Array,
      required: true,
    },
    startIndex: {
      type: Number,
      required: true,
    }
  },
  watch: {
    startIndex: {
      immediate: true,
      handler() {
        this.currentIndex = this.startIndex;
      }
    },
    saved: {
      immediate: true,
      handler() {
        // Can be overloaded from multiple clicks (change)
        if (this.saved) {
          setTimeout(() => {
            this.saved = false;
          }, 2000);
        }
      }
    }
  },
  data() {
    return {
      currentIndex: this.startIndex,
      saved: false,
    }
  },
  components: {CloseButton, ContinueButton},
  computed: {
    isLoaded() {
      return this.objectArray.length > 0;
    },
    getProperty() {
      if (!this.isLoaded) {
        return {};
      }
      return this.objectArray[this.currentIndex];
    },
    getTitle() {
      if (!this.isLoaded) {
        return 'No Data';
      }
      return `${this.getProperty.fullName} (${this.currentIndex + 1}/${this.objectArray.length})`;
    },
    isDriver() {
      if (!this.isLoaded) {
        return false;
      }
      return Object.prototype.hasOwnProperty.call(this.getProperty, 'driverId');
    },
    isConstructor() {
      if (!this.isLoaded) {
        return false;
      }
      return Object.prototype.hasOwnProperty.call(this.getProperty, 'constructorId');
    }
  },
  methods: {
    ...mapActions(['updateDriver', 'updateConstructor']),
    next() {
      // Save Data
      this.save()
      // Increment index
      this.currentIndex += 1;
    },
    save() {
      this.getProperty.fullName = document.getElementById('fullName').value;
      this.getProperty.country = document.getElementById('country').value;
      this.getProperty.points = parseInt(document.getElementById('points').value);
      this.getProperty.fantasyPoints = parseInt(document.getElementById('fantasyPoints').value);
      this.getProperty.fantasyPrice = parseInt(document.getElementById('fantasyPrice').value);

      // Save Data
      if (this.isDriver) {
        this.saved = this.$store.dispatch('updateDriver', this.getProperty);
      } else if (this.isConstructor) {
        this.saved = this.$store.dispatch('updateConstructor', this.getProperty);
      }
    },
    close() {
      // Save Data
      this.save();
      // Exit
      this.exit();
    },
    exit() {
      // Add opacity transition
      this.$emit('exit');
    }
  }
}

</script>

<template>
  <div
      class="tw-absolute tw-h-screen tw-w-screen tw-top-0 tw-left-0 tw-backdrop-blur-md tw-bg-f1-white tw-bg-opacity-15">
    <div class="tw-absolute tw-top-4 tw-w-full">
      <div class="tw-flex tw-w-full tw-justify-center tw-items-center">
        <div :class="[`tw-opacity-${saved ? 100 : 0}`]"
             class="tw-bg-green-900 tw-border-2 tw-border-green-500 tw-px-2 tw-py-1 tw-rounded tw-text-green-500 tw-transition-opacity">
          Success!
        </div>
      </div>
    </div>
    <div class="tw-flex tw-w-full tw-h-full tw-justify-center tw-items-center ">
      <div
          class="tw-w-96 tw-bg-primary-dark tw-drop-shadow-2xl tw-rounded-lg tw-p-2 tw-flex tw-flex-col tw-gap-2 tw-text-f1-white">
        <div class="tw-h-10 tw-text-2xl tw-font-bold tw-border-b-1 tw-border-primary-light">
          {{ getTitle }}
        </div>
        <div class="tw-flex tw-flex-col tw-gap-2 tw-px-2">
          <div class="tw-flex tw-flex-row" v-if="isDriver || isConstructor">
            <div class="tw-flex tw-items-center">Full Name</div>
            <input id="fullName" type="text" :value="getProperty.fullName"
                   class="tw-w-44 tw-ml-auto tw-border-1 tw-border-primary-light tw-rounded tw-bg-primary-light tw-bg-opacity-15 tw-text-f1-white tw-text-center">
          </div>
          <div class="tw-flex tw-flex-row" v-if="isDriver || isConstructor">
            <div class="tw-flex tw-items-center">Country</div>
            <input id="country" type="text" :value="getProperty.country"
                   class="tw-w-44 tw-ml-auto tw-border-1 tw-border-primary-light tw-rounded tw-bg-primary-light tw-bg-opacity-15 tw-text-f1-white tw-text-center">
          </div>
          <div class="tw-flex tw-flex-row" v-if="isDriver">
            <div class="tw-flex tw-items-center">Points</div>
            <input id="points" type="number" :value="getProperty.points"
                   class="tw-w-16 tw-ml-auto tw-border-1 tw-border-primary-light tw-rounded tw-bg-primary-light tw-bg-opacity-15 tw-text-f1-white tw-text-center">
          </div>
          <div class="tw-flex tw-flex-row" v-if="isDriver || isConstructor">
            <div class="tw-flex tw-items-center">Fantasy Points</div>
            <input id="fantasyPoints" type="number" :value="getProperty.fantasyPoints"
                   class="tw-w-16 tw-ml-auto tw-border-1 tw-border-primary-light tw-rounded tw-bg-primary-light tw-bg-opacity-15 tw-text-f1-white tw-text-center">
          </div>
          <div class="tw-flex tw-flex-row" v-if="isDriver || isConstructor">
            <div class="tw-flex tw-items-center">Fantasy Price</div>
            <input id="fantasyPrice" type="number" :value="getProperty.fantasyPrice"
                   class="tw-w-16 tw-ml-auto tw-border-1 tw-border-primary-light tw-rounded tw-bg-primary-light tw-bg-opacity-15 tw-text-f1-white tw-text-center">
          </div>
        </div>
        <div class="tw-flex tw-justify-center tw-gap-2 tw-border-t-1 tw-border-primary-light tw-pt-2">
          <CloseButton @close="exit" class="tw-mr-auto">Exit</CloseButton>
          <ContinueButton @continue="next" v-if="currentIndex < objectArray.length-1">Save & Continue</ContinueButton>
          <ContinueButton @continue="close">Save & Exit</ContinueButton>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>