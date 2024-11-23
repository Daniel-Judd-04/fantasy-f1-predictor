<script>
import CloseButton from "@/components/common/CloseButton.vue";
import ContinueButton from "@/components/common/ContinueButton.vue";
import UserInput from "@/components/common/UserInput.vue";
import {getConciseName, getDriver, sort} from "@/utils/common";
import {mapGetters} from "vuex";

export default {
  name: 'EditArray',
  props: {
    overlayArray: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      addingObject: false,
      tempAdditions: [],
      tempActiveStatuses: [],
    }
  },
  components: {UserInput, CloseButton, ContinueButton},
  computed: {
    ...mapGetters(['allDrivers', 'allConstructors', 'userTeams']),
    isLoaded() {
      return this.overlayArray.length > 0;
    },
    isDriver() {
      if (!this.isLoaded) {
        return false;
      }
      return Object.prototype.hasOwnProperty.call(this.overlayArray[0], 'driverId');
    },
    isConstructor() {
      if (!this.isLoaded) {
        return false;
      }
      return Object.prototype.hasOwnProperty.call(this.overlayArray[0], 'constructorId');
    },
    isUserTeam() {
      if (!this.isLoaded) {
        return false;
      }
      return Object.prototype.hasOwnProperty.call(this.overlayArray[0], 'teamName');
    }
  },
  beforeMount() {
    // Set active status
    if (!this.isUserTeam) {
      for (let i = 0; i < this.overlayArray.length; i++) {
        let id;
        if (this.isDriver) {
          id = this.overlayArray[i].driverId;
        } else {
          id = this.overlayArray[i].constructorId;
        }
        this.tempActiveStatuses[id] = this.overlayArray[i].active;
      }
    }
  },
  methods: {
    getConciseName,
    getDriver,
    getTitle() {
      if (!this.isLoaded) {
        return 'No Data';
      }
      let objectType = '???';
      if (this.isDriver) {
        objectType = 'Driver';
      } else if (this.isConstructor) {
        objectType = 'Constructor';
      } else if (this.isUserTeam) {
        objectType = 'Team';
      }
      return `${this.addingObject ? 'Add' : 'Edit'} ${objectType}${this.addingObject ? '' : 's'}`;
    },
    validInputs() {
      if (this.isDriver) {
        return document.getElementById('fullName').value !== '' &&
            document.getElementById('shortName').value.length === 3 &&
            document.getElementById('country').value !== '' &&
            document.getElementById('fantasyPrice').value !== '';
      } else if (this.isConstructor) {
        return document.getElementById('fullName').value !== '' &&
            document.getElementById('shortName').value.length === 3 &&
            document.getElementById('country').value !== '' &&
            document.getElementById('fantasyPrice').value !== '';
      } else if (this.isUserTeam) {
        const drivers = document.getElementById('drivers').value.split(',');
        const constructors = document.getElementById('constructors').value.split(',');
        return document.getElementById('teamName').value !== '' &&
            document.getElementById('teamOwner').value !== '' &&
            document.getElementById('freeTransfers').value !== '' &&
            drivers.length === 5 && constructors.length === 2 &&
            document.getElementById('remainingBudget').value !== '';
      }
    },
    addObject() {
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

        this.tempActiveStatuses.push(true);
      }
      if (this.isUserTeam) {
        newAddition.teamName = document.getElementById('teamName').value;
        newAddition.teamOwner = document.getElementById('teamOwner').value;
        newAddition.freeTransfers = parseInt(document.getElementById('freeTransfers').value);
        newAddition.drivers = document.getElementById('drivers').value.split(',');
        newAddition.constructors = document.getElementById('constructors').value.split(',');
        newAddition.remainingBudget = parseFloat(document.getElementById('remainingBudget').value);
      }

      this.tempAdditions.push(newAddition);
      console.log(this.tempAdditions);
    },
    async save() {
      // Add any new objects
      for (let i = 0; i < this.tempAdditions.length; i++) {
        await this.$store.dispatch('add', this.tempAdditions[i]);
      }

      if (!this.isUserTeam) {
        // Update any changes in active status
        for (let i = 0; i < this.overlayArray.length; i++) {
          const id = this.isDriver ? this.overlayArray[i].driverId : this.overlayArray[i].constructorId;
          if (this.overlayArray[i].active !== this.tempActiveStatuses[id]) {
            const clone = this.overlayArray[i];
            clone.active = this.tempActiveStatuses[id];
            await this.$store.dispatch('update', clone);
          }
        }

        // Update sorting method
        if (document.getElementById('sort-by-points').checked) {
          sort(this.overlayArray, 'points');
        } else if (document.getElementById('sort-by-fantasyPoints').checked) {
          sort(this.overlayArray, 'fantasyPoints');
        } else if (document.getElementById('sort-by-fantasyPrice').checked) {
          sort(this.overlayArray, 'fantasyPrice');
        }
      }
    },
    async close() {
      // Add new constructor to array
      if (this.addingObject) {
        if (this.validInputs()) {
          this.addObject();
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
      if (this.addingObject) {
        this.addingObject = false;
      } else {
        this.$emit('exit');
      }
    },
  }
}
</script>

<template>
  <div
      class="tw-w-96 tw-bg-gradient-to-bl tw-to-100% tw-from-primary-dark tw-to-f1-red tw-outline tw-outline-1 -tw-outline-offset-1 tw-outline-primary-light tw-drop-shadow-2xl tw-rounded-lg tw-flex tw-flex-col tw-text-f1-white">
    <div class="hover-parent tw-p-2 tw-flex tw-justify-center tw-items-center tw-gap-2">
      <div class="tw-text-2xl tw-font-bold tw-pl-2 tw-pt-0.5 tw-mr-auto">
        {{ getTitle() }}
      </div>
      <CloseButton class="hover-child tw-transition-opacity" @close="exit">
        <span class="material-symbols-outlined tw-font-light">close</span>
      </CloseButton>
    </div>
    <!--    Adding a constructor -->
    <div v-if="addingObject" class="tw-border-primary-light tw-border-b-1 tw-flex tw-flex-col tw-gap-2 tw-py-2 tw-px-4 tw-bg-primary-dark">
      <UserInput name="Full Name" v-if="isDriver || isConstructor"/>
      <UserInput name="Short Name" v-if="isDriver || isConstructor"/>
      <UserInput name="Team Name" v-if="isUserTeam"/>
      <UserInput name="Team Owner" v-if="isUserTeam"/>
      <UserInput name="Free Transfers" type="number" :step="1" :default-value="'3'" v-if="isUserTeam"/>
      <UserInput name="Drivers" :default-value="'VER,PER,NOR,PIA,ALO'" v-if="isUserTeam"/>
      <UserInput name="Constructors" :default-value="'RBR,MCL'" v-if="isUserTeam"/>
      <UserInput name="Remaining Budget" type="number" :step="0.1" :default-value="'0.0'" v-if="isUserTeam"/>
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
    <div v-if="!addingObject && !isUserTeam" class="tw-border-primary-light tw-border-y-1 tw-flex tw-flex-col tw-p-2 tw-bg-primary-dark">
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
    <div v-if="!addingObject" class="tw-border-primary-light tw-border-b-1 tw-flex tw-flex-col tw-gap-2 tw-py-2 tw-px-2 tw-bg-primary-dark">
      <div v-if="isDriver" class="tw-flex tw-flex-col tw-gap-2">
        <div class="tw-flex tw-gap-2"
             v-for="constructor in allConstructors.filter(c => c.drivers.length > 0)" :key="constructor.code">
          <div :class="`tw-w-1/${constructor.drivers.length}`"
               class="tw-flex tw-items-center tw-justify-between tw-px-2 tw-border-1 tw-border-primary-light tw-bg-primary-light tw-bg-opacity-5 tw-rounded"
               v-for="driverId in constructor.drivers" :key="driverId">
            <div>
              {{ getDriver(driverId).shortName }}
            </div>
            <div class="tw-cursor-pointer tw-text-right tw-text-sm"
                 @click="tempActiveStatuses[driverId] = !tempActiveStatuses[driverId]"
                 :id="getDriver(driverId).shortName + '-active'">
              {{ tempActiveStatuses[driverId] ? 'Active' : 'Inactive' }}
            </div>
          </div>
        </div>
      </div>
      <div v-else-if="isConstructor" class="tw-flex tw-flex-col tw-gap-2">
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
      <div v-else-if="isUserTeam" class="tw-flex tw-flex-col tw-gap-2">
        <div class="tw-px-2 tw-flex tw-border-1 tw-border-primary-light tw-bg-primary-light tw-bg-opacity-5 tw-rounded"
             v-for="team in userTeams" :key="team.code">
          <div class="tw-mr-auto">
            {{ team.teamName }} ({{ team.teamOwner }})
          </div>
        </div>
      </div>
      <ContinueButton @continue="addingObject = true">
        <span class="material-symbols-outlined tw-font-light">add</span>
      </ContinueButton>
      <div v-if="tempAdditions.length > 0" class="tw-flex tw-flex-col tw-gap-2">
        <div class="tw-px-2 tw-flex tw-border-1 tw-border-dashed tw-border-primary-light tw-text-primary-light tw-bg-primary-light tw-bg-opacity-5 tw-rounded"
             v-for="newObject in tempAdditions" :key="newObject.code">
          <div class="tw-mr-auto">
            {{ isUserTeam ? newObject.teamName + ' (' + newObject.teamOwner + ')' : newObject.fullName }}
          </div>
          <div>NEW</div>
        </div>
      </div>
    </div>
    <div class="tw-flex tw-border-primary-light tw-none tw-bg-primary-dark tw-p-2 tw-rounded-b-lg">
      <ContinueButton class="tw-ml-auto tw-h-10 tw-px-2" @continue="close">{{ addingObject ? 'Save' : 'Save & Close' }}</ContinueButton>
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