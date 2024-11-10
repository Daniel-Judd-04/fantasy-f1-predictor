<script>
import CloseButton from "@/components/common/CloseButton.vue";
import ContinueButton from "@/components/common/ContinueButton.vue";
import UserInput from "@/components/common/UserInput.vue";
import {getConciseName, getDriver, sort} from "@/utils/common";
import {mapActions, mapGetters} from "vuex";

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
      tempActiveStatuses: [],
    }
  },
  components: {UserInput, CloseButton, ContinueButton},
  computed: {
    ...mapGetters(['allDrivers', 'allConstructors']),
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
    }
  },
  beforeMount() {
    // Set active status
    for (let i = 0; i < this.objectArray.length; i++) {
      let id;
      if (this.isDriver) {
        id = this.objectArray[i].driverId;
      } else {
        id = this.objectArray[i].constructorId;
      }
      this.tempActiveStatuses[id] = this.objectArray[i].active;
    }
  },
  methods: {
    getConciseName,
    getDriver,
    ...mapActions(['fetchDrivers', 'fetchConstructors']),
    getTitle() {
      if (!this.isLoaded) {
        return 'No Data';
      }
      return (this.addingConstructor ? 'Add' : 'Edit') + ' ' + (this.isConstructor ? 'Constructor' : 'Driver') + (this.addingConstructor ? '' : 's');
    },
    validInputs() {
      if (this.isDriver) {
        return document.getElementById('fullName').value !== '' &&
            document.getElementById('shortName').value !== '' &&
            document.getElementById('country').value !== '' &&
            document.getElementById('fantasyPrice').value !== '';
      } else if (this.isConstructor) {
        return document.getElementById('fullName').value !== '' &&
            document.getElementById('shortName').value !== '' &&
            document.getElementById('country').value !== '' &&
            document.getElementById('fantasyPrice').value !== '';
      }
    },
    addConstructor() {
      // Temp add a constructor
      const newAddition = {};
      if (this.isDriver) {
        newAddition.carNumber = parseInt(document.getElementById('carNumber').value);
        newAddition.constructorId = parseInt(document.getElementById('constructor').value);
      }
      if (this.isDriver || this.isConstructor) {
        newAddition.fullName = document.getElementById('fullName').value;
        newAddition.shortName = document.getElementById('shortName').value;
        newAddition.country = document.getElementById('country').value;
        newAddition.fantasyPrice = parseFloat(document.getElementById('fantasyPrice').value);
        newAddition.active = true;
      }
      this.tempActiveStatuses.push(true);

      this.tempAdditions.push(newAddition);
      console.log(this.tempAdditions);
    },
    async save() {
      // Add any new objects
      for (let i = 0; i < this.tempAdditions.length; i++) {
        await this.$store.dispatch('add', this.tempAdditions[i]);
      }

      // Update any changes in active status
      for (let i = 0; i < this.objectArray.length; i++) {
        const id = this.isDriver ? this.objectArray[i].driverId : this.objectArray[i].constructorId;
        if (this.objectArray[i].active !== this.tempActiveStatuses[id]) {
          const clone = this.objectArray[i];
          clone.active = this.tempActiveStatuses[id];
          await this.$store.dispatch('update', clone);
        }
      }

      // Update sorting method
      if (document.getElementById('sort-by-points').checked) {
        sort(this.objectArray, 'points');
      } else if (document.getElementById('sort-by-fantasyPoints').checked) {
        sort(this.objectArray, 'fantasyPoints');
      } else if (document.getElementById('sort-by-fantasyPrice').checked) {
        sort(this.objectArray, 'fantasyPrice');
      }
    },
    async close() {
      // Add new constructor to array
      if (this.addingConstructor) {
        if (this.validInputs()) {
          this.addConstructor();
          // Exit
          this.exit();
        }
      } else {
        // Save Data
        await this.save();
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
  <div
      class="tw-w-96 tw-bg-gradient-to-bl tw-to-100% tw-from-primary-dark tw-to-f1-red tw-drop-shadow-2xl tw-rounded-lg tw-flex tw-flex-col tw-text-f1-white">
    <div class="hover-parent tw-p-2 tw-flex tw-justify-center tw-items-center tw-gap-2">
      <div class="tw-text-2xl tw-font-bold tw-pl-2 tw-pt-0.5 tw-mr-auto">
        {{ getTitle() }}
      </div>
      <CloseButton class="hover-child tw-transition-opacity" @close="exit">
        <span class="material-icons">close</span>
      </CloseButton>
    </div>
    <!--    Adding a constructor -->
    <div v-if="addingConstructor" class="tw-border-primary-light tw-border-b-1 tw-flex tw-flex-col tw-gap-2 tw-py-2 tw-px-4 tw-bg-primary-dark">
      <UserInput name="Full Name" v-if="isDriver || isConstructor"/>
      <UserInput name="Short Name" v-if="isDriver || isConstructor"/>
      <UserInput name="Country" v-if="isDriver || isConstructor"/>
      <div class="tw-flex" v-if="isDriver">
        <div>Constructor</div>
        <select id="constructor"
                class="tw-w-44 tw-px-2 tw-appearance-none tw-ml-auto tw-border-1 tw-border-primary-light tw-bg-primary-light tw-bg-opacity-5 tw-text-f1-white tw-text-center">
          <option :value="constructor.constructorId" v-for="constructor in allConstructors" :key="constructor.code">
            {{ constructor.fullName }}
          </option>
        </select>
      </div>
      <UserInput name="Car Number" type="number" :step="1" :default-value="'0'" v-if="isDriver"/>
      <UserInput name="Fantasy Price" type="number" :step="0.1" :default-value="'0.0'" v-if="isDriver || isConstructor"/>
    </div>
    <!--    Sorting constructors -->
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
    <!--    Deactivate constructors -->
    <div v-if="!addingConstructor" class="tw-border-primary-light tw-border-b-1 tw-flex tw-flex-col tw-gap-2 tw-py-2 tw-px-2 tw-bg-primary-dark">
      <div v-if="isDriver" class="tw-flex tw-flex-col tw-gap-2">
        <div class="tw-flex tw-gap-2"
             v-for="constructor in allConstructors" :key="constructor.code">
          <div :class="`tw-w-1/${constructor.drivers.length}`" class="tw-flex tw-pl-2 tw-border-1 tw-border-primary-light tw-bg-primary-light tw-bg-opacity-5 tw-rounded"
               v-for="driverId in constructor.drivers" :key="driverId">
            <div class="tw-mr-auto">
              {{ getDriver(driverId).shortName }}
            </div>
            <div class="tw-cursor-pointer tw-px-2 tw-w-20 tw-text-right"
                 @click="tempActiveStatuses[driverId] = !tempActiveStatuses[driverId]"
                 :id="getDriver(driverId).shortName + '-active'">
              {{ tempActiveStatuses[driverId] ? 'Active' : 'Inactive' }}
            </div>
          </div>
        </div>
      </div>
      <div v-else class="tw-flex tw-flex-col tw-gap-2">
        <div class="tw-pl-2 tw-flex tw-border-1 tw-border-primary-light tw-bg-primary-light tw-bg-opacity-5 tw-rounded"
             v-for="constructor in allConstructors" :key="constructor.code">
          <div class="tw-mr-auto">
            {{ constructor.fullName }}
          </div>
          <div class="tw-cursor-pointer tw-px-2 tw-w-20 tw-text-right"
               @click="tempActiveStatuses[constructor.constructorId] = !tempActiveStatuses[constructor.constructorId]"
               :id="constructor.shortName + '-active'">
            {{ tempActiveStatuses[constructor.constructorId] ? 'Active' : 'Inactive' }}
          </div>
        </div>
      </div>
      <ContinueButton @continue="addingConstructor = true">
        <span class="material-icons">add</span>
      </ContinueButton>
    </div>
    <div class="tw-flex tw-border-primary-light tw-none tw-bg-primary-dark tw-p-2 tw-rounded-b-lg">
      <ContinueButton class="tw-ml-auto tw-h-10" @continue="close">{{ addingConstructor ? 'Save' : 'Save & Close' }}</ContinueButton>
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