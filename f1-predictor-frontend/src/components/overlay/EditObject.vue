<script>
import CloseButton from "@/components/common/CloseButton.vue";
import ContinueButton from "@/components/common/ContinueButton.vue";
import {getConstructor, getFlagURL} from "@/utils/common";
import UserInput from "@/components/common/UserInput.vue";

export default {
  name: 'EditObject',
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
  data() {
    return {
      currentIndex: this.startIndex,
      tempActiveStatus: this.objectArray[this.startIndex].active,
    }
  },
  watch: {
    startIndex: {
      immediate: true,
      handler() {
        this.currentIndex = this.startIndex;
        this.tempActiveStatus = this.objectArray[this.startIndex].active;
      }
    },
    currentIndex: {
      immediate: true,
      handler() {
        this.tempActiveStatus = this.objectArray[this.currentIndex].active;
      }
    }
  },
  components: {UserInput, CloseButton, ContinueButton},
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
      return `${this.getProperty.fullName}`;
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
    },
    getGradientColour() {
      if (this.isDriver || this.isConstructor) {
        const constructorShortName = this.isDriver ? getConstructor(this.getProperty.constructor).shortName : this.getProperty.shortName;
        return 'team-' + constructorShortName;
      }
      return 'f1-red';
    },
  },
  methods: {
    getConstructor,
    isDifference() {
      if (this.isLoaded) {
        if (this.getProperty.fullName !== document.getElementById('fullName').value) {
          return true;
        } else if (this.getProperty.country !== document.getElementById('country').value) {
          return true;
        } else if (this.isDriver && this.getProperty.points !== parseFloat(document.getElementById('points').value)) {
          return true;
        } else if (this.getProperty.fantasyPoints !== parseInt(document.getElementById('fantasyPoints').value)) {
          return true;
        } else if (this.getProperty.fantasyPrice !== parseFloat(document.getElementById('fantasyPrice').value)) {
          return true;
        } else if (this.getProperty.active !== this.tempActiveStatus) {
          return true;
        }
      }
      return false;
    },
    async save() {
      if (!this.isDifference()) {
        return;
      }
      this.getProperty.fullName = document.getElementById('fullName').value;
      this.getProperty.country = document.getElementById('country').value;
      this.getProperty.fantasyPoints = parseInt(document.getElementById('fantasyPoints').value);
      this.getProperty.fantasyPrice = parseFloat(document.getElementById('fantasyPrice').value);
      this.getProperty.active = this.tempActiveStatus;
      if (this.isDriver) {
        this.getProperty.points = parseFloat(document.getElementById('points').value);
      }

      // Save Data
      if (await this.$store.dispatch('update', this.getProperty)) {
        this.$emit('success');
      } else {
        this.$emit('error');
      }
    },
    async close() {
      // Save Data
      await this.save()
      // Exit
      this.$emit('exit');
    },
    exit() {
      this.$emit('exit');
    },
    async previous() {
      // Save Data
      await this.save();
      // Decrement index
      this.currentIndex--;
    },
    async next() {
      // Save Data
      await this.save();
      // Increment index
      this.currentIndex++;
    },
    getFlagURL,
  }
}
</script>

<template>
  <div :class="[`tw-to-${getGradientColour}`]"
       class="tw-w-96 tw-bg-gradient-to-bl tw-to-100% tw-from-primary-dark tw-drop-shadow-2xl tw-rounded-lg tw-flex tw-flex-col tw-text-f1-white">
    <div class="tw-p-2 tw-flex tw-justify-center tw-items-center tw-gap-2 hover-parent">
      <div class="flag-frame tw-border-1 tw-border-f1-white tw-overflow-hidden tw-h-10 tw-align-middle tw-rounded">
        <img :src="getFlagURL(getProperty.country)" alt="Flag" class="flag-image"/>
      </div>
      <div class="tw-text-2xl tw-font-bold tw-pl-2 tw-pt-0.5 tw-mr-auto">
        {{ getTitle }}
      </div>
      <CloseButton class="hover-child tw-transition-opacity" @close="exit">
        <span class="material-icons">close</span>
      </CloseButton>
    </div>
    <div class="tw-border-primary-light tw-border-y-1 tw-flex tw-flex-col tw-gap-2 tw-py-2 tw-px-4 tw-bg-primary-dark">
      <UserInput name="Full Name" v-if="isDriver || isConstructor" :default-value="getProperty.fullName"/>
      <UserInput name="Country" v-if="isDriver || isConstructor" :default-value="getProperty.country"/>
      <UserInput name="Points" type="number" :step="1" v-if="isDriver" :default-value="getProperty.points.toString()"/>
      <UserInput name="Fantasy Points" type="number" :step="1" v-if="isDriver || isConstructor" :default-value="getProperty.fantasyPoints.toString()"/>
      <UserInput name="Fantasy Price" type="number" :step="0.1" v-if="isDriver || isConstructor" :default-value="getProperty.fantasyPrice.toString()"/>
      <div class="tw-flex tw-flex-row">
        <div class="tw-flex tw-items-center">Active</div>
        <div class="tw-ml-auto tw-w-12 tw-border-1 tw-border-primary-light tw-rounded tw-bg-primary-light tw-bg-opacity-5 tw-text-f1-white tw-text-center tw-cursor-pointer"
             @click="tempActiveStatus = !tempActiveStatus" id="active">
          {{ tempActiveStatus ? 'Yes' : 'No' }}
        </div>
      </div>
    </div>
    <div class="tw-flex tw-justify-between tw-h-14 tw-gap-2 tw-border-primary-light tw-none tw-bg-primary-dark tw-p-2 tw-rounded-b-lg">
      <ContinueButton @continue="previous" :class="[`${currentIndex > 0 ? '' : 'tw-invisible'}`]">
        <span class="material-icons">arrow_back</span>
      </ContinueButton>
      <ContinueButton @continue="close">Save & Close</ContinueButton>
      <ContinueButton @continue="next" :class="`${currentIndex < objectArray.length-1 ? '' : 'tw-invisible'}`">
        <span class="material-icons">arrow_forward</span>
      </ContinueButton>
    </div>
  </div>
</template>

<style scoped>

</style>