<script>
import CloseButton from "@/components/common/CloseButton.vue";
import ContinueButton from "@/components/common/ContinueButton.vue";
import UserInput from "@/components/common/UserInput.vue";
import {sort} from "@/utils/common";

export default {
  name: 'EditArray',
  props: {
    objectArray: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      addingConstructor: false,
      tempAdditions: [],
      tempRemovals: [],
    }
  },
  components: {UserInput, CloseButton, ContinueButton},
  computed: {
    isLoaded() {
      return this.objectArray.length > 0;
    },
    isDriver() {
      if (!this.isLoaded) {
        return false;
      }
      return Object.prototype.hasOwnProperty.call(this.objectArray[0], 'driverId');
    },
    isConstructor() {
      if (!this.isLoaded) {
        return false;
      }
      return Object.prototype.hasOwnProperty.call(this.objectArray[0], 'constructorId');
    },
  },
  methods: {
    getTitle() {
      if (!this.isLoaded) {
        return 'No Data';
      }
      return (this.addingConstructor ? 'Add' : 'Edit') + ' ' + (this.isConstructor ? 'Constructor' : 'Driver') + (this.addingConstructor ? '' : 's');
    },
    validInputs() {
      if (this.isDriver) {
        return document.getElementById('points').value !== '' &&
            document.getElementById('fullName').value !== '' &&
            document.getElementById('shortName').value !== '' &&
            document.getElementById('country').value !== '' &&
            document.getElementById('fantasyPoints').value !== '' &&
            document.getElementById('fantasyPrice').value !== '';
      } else if (this.isConstructor) {
        return document.getElementById('fullName').value !== '' &&
            document.getElementById('shortName').value !== '' &&
            document.getElementById('country').value !== '' &&
            document.getElementById('fantasyPoints').value !== '' &&
            document.getElementById('fantasyPrice').value !== '';
      }
    },
    addConstructor() {
      // Temp add a constructor
      const newAddition = {};
      if (this.isDriver) {
        newAddition.points = parseFloat(document.getElementById('points').value);
      }
      if (this.isDriver || this.isConstructor) {
        newAddition.fullName = document.getElementById('fullName').value;
        newAddition.shortName = document.getElementById('shortName').value;
        newAddition.country = document.getElementById('country').value;
        newAddition.fantasyPoints = parseInt(document.getElementById('fantasyPoints').value);
        newAddition.fantasyPrice = parseFloat(document.getElementById('fantasyPrice').value);
      }

      this.tempAdditions.push(newAddition);
      console.log(this.tempAdditions);
    },
    removeConstructor(object) {
      this.tempRemovals.push(object);
      console.log(this.tempRemovals);
    },
    save() {
      // Save any changes
      for (let i = 0; i < this.tempRemovals.length; i++) {
        if (this.isDriver) {
          this.$store.dispatch('remove', 'driver', this.tempRemovals[i]);
        } else if (this.isConstructor) {
          this.$store.dispatch('remove', 'constructor', this.tempRemovals[i]);
        }
      }
      for (let i = 0; i < this.tempAdditions.length; i++) {
        if (this.isDriver) {
          this.$store.dispatch('addDriver', this.tempAdditions[i]);
        } else if (this.isConstructor) {
          this.$store.dispatch('addConstructor', this.tempAdditions[i]);
        }
      }

      if (document.getElementById('sort-by-points').checked) {
        sort(this.objectArray, 'points');
      } else if (document.getElementById('sort-by-fantasyPoints').checked) {
        sort(this.objectArray, 'fantasyPoints');
      } else if (document.getElementById('sort-by-fantasyPrice').checked) {
        sort(this.objectArray, 'fantasyPrice');
      }

      console.log('After saving:', this.objectArray);
    },
    close() {
      // Add new constructor to array
      if (this.addingConstructor) {
        if (this.validInputs()) {
          this.addConstructor();
          // Exit
          this.exit();
        }
      } else {
        // Save Data
        this.save();
        // Exit
        this.exit();
      }
    },
    exit() {
      if (this.addingConstructor) {
        this.addingConstructor = false;
      } else {
        this.$emit('exit');
      }
    },
  }
}
</script>

<template>
  <div class="tw-w-96 tw-bg-gradient-to-bl tw-to-100% tw-from-primary-dark tw-to-f1-red tw-drop-shadow-2xl tw-rounded-lg tw-flex tw-flex-col tw-text-f1-white">
    <div class="tw-p-2 tw-flex tw-justify-center tw-items-center tw-gap-2">
      <div class="tw-text-2xl tw-font-bold tw-pl-2 tw-pt-0.5 tw-mr-auto">
        {{ getTitle() }}
      </div>
    </div>
    <div v-if="!addingConstructor" class="tw-border-primary-light tw-border-y-1 tw-flex tw-flex-col tw-p-2 tw-bg-primary-dark">
      <div class="segmented-control tw-rounded tw-full tw-border-1 tw-border-primary-light">
        <input type="radio" id="sort-by-points" name="options">
        <label for="sort-by-points">Points</label>
        <input type="radio" id="sort-by-fantasyPoints" name="options" checked>
        <label for="sort-by-fantasyPoints">Fantasy Points</label>
        <input type="radio" id="sort-by-fantasyPrice" name="options">
        <label for="sort-by-fantasyPrice">Fantasy Price</label>
        <div class="highlight tw-bg-primary tw-rounded"></div>
      </div>
    </div>
    <div v-if="addingConstructor" class="tw-border-primary-light tw-border-b-1 tw-flex tw-flex-col tw-gap-2 tw-p-2 tw-bg-primary-dark">
      <UserInput name="Full Name" v-if="isDriver || isConstructor"/>
      <UserInput name="Short Name" v-if="isDriver || isConstructor"/>
      <UserInput name="Country" v-if="isDriver || isConstructor"/>
      <UserInput name="Points" type="number" :step="1" v-if="isDriver"/>
      <UserInput name="Fantasy Points" type="number" :step="1" v-if="isDriver || isConstructor"/>
      <UserInput name="Fantasy Price" type="number" :step="0.1" v-if="isDriver || isConstructor"/>
      <UserInput name="Active" type="checkbox" v-if="isDriver"/>
    </div>
    <div v-if="!addingConstructor" class="tw-border-primary-light tw-border-b-1 tw-flex tw-flex-col tw-gap-2 tw-p-2 tw-bg-primary-dark">
      <div class="tw-px-2 tw-flex tw-border-1 tw-border-primary-light tw-bg-primary-light tw-bg-opacity-5 tw-rounded"
           v-for="object in objectArray" :key="object.code">
        <div class="tw-mr-auto">
          {{ object.fullName }}
        </div>
        <span @click="removeConstructor(object)" class="material-icons tw-cursor-pointer">remove</span>
      </div>
      <ContinueButton @continue="addingConstructor = true">
        <span class="material-icons">add</span>
      </ContinueButton>
    </div>
    <div class="hover-parent tw-flex tw-border-primary-light tw-none tw-bg-primary-dark tw-p-2 tw-rounded-b-lg">
      <CloseButton class="hover-child tw-transition-opacity" @close="exit">
        <span class="material-icons">close</span>
      </CloseButton>
      <ContinueButton class="tw-ml-auto" @continue="close">{{ addingConstructor ? 'Save' : 'Save & Close' }}</ContinueButton>
    </div>
  </div>
</template>

<style scoped>
.segmented-control {
  display: flex;
  position: relative;
  overflow: hidden;
}

.segmented-control label {
  padding-inline: 8px;
  cursor: pointer;
  text-align: center;
  flex: 1;
  transition: color 0.3s;
  position: relative;
  z-index: 1;
}

.segmented-control input[type="radio"] {
  display: none;
}


.segmented-control .highlight {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  width: calc(100% / 3);
  transition: transform 0.3s ease;
  z-index: 0;
}

#sort-by-points:checked ~ .highlight {
  transform: translateX(0%);
}

#sort-by-fantasyPoints:checked ~ .highlight {
  transform: translateX(100%);
}

#sort-by-fantasyPrice:checked ~ .highlight {
  transform: translateX(200%);
}
</style>