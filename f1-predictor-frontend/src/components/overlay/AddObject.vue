<script>
import UserInput from "@/components/common/UserInput.vue";
import {
  getConstructor,
  getConstructorFromShortName,
  getDriver,
  getDriverFromShortName,
  getObjectType,
  getTimezones,
  isConstructor,
  isDriver,
  isGrandPrix,
  isUserTeam
} from "@/utils/common";
import {mapGetters} from "vuex";
import ContinueButton from "@/components/common/ContinueButton.vue";
import OverlayHeader from "@/components/common/OverlayHeader.vue";
import {OverlayData} from "@/utils/classes";
import CloseButton from "@/components/common/CloseButton.vue";

export default {
  name: 'AddObject',
  components: {CloseButton, OverlayHeader, ContinueButton, UserInput},
  props: {
    overlayData: {
      type: OverlayData,
      required: true,
      default: () => (new OverlayData({}, [], null)),
    }
  },
  data() {
    return {
      tempSprintStatus: false,
      tempSelectedCircuit: '',
    }
  },
  methods: {
    getTimezones,
    getConstructor,
    getDriver,
    isGrandPrix,
    getObjectType,
    isUserTeam,
    isConstructor,
    isDriver,
    isNewCircuit() {
      if (isGrandPrix(this.item)) {
        const circuitSelect = document.getElementById('circuit');
        return circuitSelect && circuitSelect.value === 'NEW';
      }
      return false;
    },
    validInputs() {
      if (isDriver(this.item)) {
        return document.getElementById('fullName').value !== '' &&
            document.getElementById('shortName').value.length === 3 &&
            document.getElementById('country').value !== '' &&
            document.getElementById('fantasyPrice').value !== '';
      } else if (isConstructor(this.item)) {
        return document.getElementById('fullName').value !== '' &&
            document.getElementById('shortName').value.length === 3 &&
            document.getElementById('country').value !== '' &&
            document.getElementById('fantasyPrice').value !== '';
      } else if (isUserTeam(this.item)) {
        const drivers = document.getElementById('drivers').value.split(',');
        const constructors = document.getElementById('constructors').value.split(',');
        return document.getElementById('teamName').value !== '' &&
            document.getElementById('teamOwner').value !== '' &&
            document.getElementById('freeTransfers').value !== '' &&
            drivers.length === 5 && constructors.length === 2 &&
            document.getElementById('remainingBudget').value !== '';
      } else if (isGrandPrix(this.item)) {
        const round = parseInt(document.getElementById('round').value);
        const validGrandPrix =
            document.getElementById('fullName').value !== ''
            && round > 0 && round < 25
            && document.getElementById('circuit').value !== '';
        if (this.isNewCircuit(this.item)) {
          return validGrandPrix &&
              document.getElementById('circuitName').value !== '' &&
              document.getElementById('circuitLocation').value !== '' &&
              document.getElementById('circuitCountry').value !== '';
        }
        return validGrandPrix;
      }

      return false;
    },
    async save() {
      let newObject = {};
      if (isDriver(this.item)) {
        newObject = {
          objectType: 'drivers',
          fullName: document.getElementById('fullName').value,
          shortName: document.getElementById('shortName').value,
          country: document.getElementById('country').value,
          constructor: parseInt(document.getElementById('constructor').value),
          carNumber: parseInt(document.getElementById('carNumber').value),
          fantasyPrice: parseFloat(document.getElementById('fantasyPrice').value),
          active: true,
        };
      } else if (isConstructor(this.item)) {
        newObject = {
          objectType: 'constructors',
          fullName: document.getElementById('fullName').value,
          shortName: document.getElementById('shortName').value,
          country: document.getElementById('country').value,
          fantasyPrice: parseFloat(document.getElementById('fantasyPrice').value),
          active: true,
        };
      } else if (isUserTeam(this.item)) {
        newObject = {
          objectType: 'user-teams',
          name: document.getElementById('teamName').value,
          owner: document.getElementById('teamOwner').value,
          driverIds: document.getElementById('drivers').value.split(',').map(shortName => getDriverFromShortName(shortName).driverId),
          constructorIds: document.getElementById('constructors').value.split(',').map(shortName => getConstructorFromShortName(shortName).constructorId),
          freeTransfers: parseInt(document.getElementById('freeTransfers').value),
          remainingBudget: parseFloat(document.getElementById('remainingBudget').value),
        };
      } else if (isGrandPrix(this.item)) {
        let newCircuit = {};
        if (this.isNewCircuit(this.item)) {
          newCircuit = {
            fullName: document.getElementById('circuitName').value,
            locationName: document.getElementById('circuitLocation').value,
            country: document.getElementById('circuitCountry').value,
            timezone: document.getElementById('timezone').value,
          };
        } else {
          newCircuit = {
            circuitId: parseInt(document.getElementById('circuit').value),
          };
        }
        newObject = {
          objectType: 'grands-prix',
          fullName: document.getElementById('fullName').value,
          startTime: document.getElementById('startDate&Time').value + ':00Z',
          circuit: newCircuit,
          round: parseInt(document.getElementById('round').value),
          sprint: this.tempSprintStatus,
        };
      }

      if (await this.$store.dispatch('add', newObject)) {
        this.$emit('success', `${getObjectType(this.item)} added! Refresh to see changes.`);
      } else {
        this.$emit('error', `Failed to add ${getObjectType(this.item)}.`);
        console.log("Error saving object: ", newObject);
      }
    },
    async close() {
      if (this.validInputs()) {
        // Save
        await this.save();
        // Exit
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
  computed: {
    ...mapGetters(['allConstructors', 'allCircuits']),
    item() {
      return this.overlayData.overlayArray[0];
    },
  },
}
</script>

<template>
  <div
      class="tw-bg-gradient-to-bl tw-to-100% tw-from-primary-dark tw-to-f1-red tw-outline tw-outline-1 -tw-outline-offset-1 tw-outline-primary-light tw-drop-shadow-2xl tw-rounded-lg tw-flex tw-flex-col tw-text-f1-white">
    <OverlayHeader @exit="exitAll">Add {{ getObjectType(item) }}</OverlayHeader>
    <div class="tw-border-primary-light tw-border-y-1 tw-flex tw-flex-col tw-gap-2 tw-py-4 tw-px-4 tw-bg-primary-dark">
      <UserInput v-if="isDriver(item) || isConstructor(item) || isGrandPrix(item)" name="Full Name" :placeholder="item.fullName"/>
      <UserInput v-if="isDriver(item) || isConstructor(item)" name="Short Name" :placeholder="item.shortName"/>
      <UserInput v-if="isDriver(item) || isConstructor(item)" name="Country" :placeholder="item.country"/>
      <div class="tw-flex" v-if="isDriver(item)">
        <div>Constructor</div>
        <select id="constructor"
                class="tw-w-52 tw-px-2 tw-appearance-none tw-ml-auto tw-border-1 tw-border-primary-light tw-bg-primary-light tw-bg-opacity-5 tw-text-f1-white tw-text-center">
          <option :value="constructor.constructorId" v-for="constructor in allConstructors" :key="constructor.code">
            {{ constructor.fullName }}
          </option>
        </select>
      </div>
      <UserInput v-if="isDriver(item)" name="Car Number" type="number" :step="1" :default-value="item.carNumber"/>
      <UserInput v-if="isDriver(item) || isConstructor(item)" name="Fantasy Price" type="number" :step="0.1" :default-value="item.fantasyPrice.toFixed(1)"/>

      <UserInput v-if="isUserTeam(item)" name="Team Name" :placeholder="item.name"/>
      <UserInput v-if="isUserTeam(item)" name="Team Owner" :placeholder="item.owner"/>
      <UserInput v-if="isUserTeam(item)" name="Drivers" :placeholder="item.drivers.map(driverId => getDriver(driverId).shortName).join(',')"/>
      <UserInput v-if="isUserTeam(item)" name="Constructors" :placeholder="item.constructors.map(constructorId => getConstructor(constructorId).shortName).join(',')"/>
      <UserInput v-if="isUserTeam(item)" name="Free Transfers" type="number" :step="1" default-value="3"/>
      <UserInput v-if="isUserTeam(item)" name="Remaining Budget" type="number" :step="0.1" default-value="0.0"/>

      <UserInput v-if="isGrandPrix(item)" name="Start Date & Time" type="datetime-local"
                 :default-value="overlayData.overlayArray[overlayData.overlayArray.length-1].startTime.split(':').splice(0, 2).join(':')"/>
      <div class="tw-flex tw-justify-between" v-if="isGrandPrix(item)">
        <div>Circuit</div>
        <select id="circuit" v-model="tempSelectedCircuit"
                class="tw-w-52 tw-px-2 tw-appearance-none tw-border-1 tw-border-primary-light tw-bg-primary-light tw-bg-opacity-5 tw-text-f1-white tw-text-center">
          <option value="NEW">New Circuit</option>
          <option :value="circuit.circuitId" v-for="circuit in allCircuits" :key="circuit.code">
            {{ circuit.locationName }}
          </option>
        </select>
      </div>
      <div v-if="isNewCircuit(item)" class="tw-flex tw-flex-col tw-gap-2 tw-py-2 tw-border-y-1 tw-border-primary-light tw-text-sm">
        <UserInput name="Circuit Name" :placeholder="allCircuits[0].fullName"/>
        <UserInput name="Circuit Location" :placeholder="allCircuits[0].locationName"/>
        <UserInput name="Circuit Country" :placeholder="allCircuits[0].country"/>
        <div class="tw-flex tw-justify-between">
          <div>Timezone</div>
          <select id="timezone"
                  class="tw-w-52 tw-px-2 tw-appearance-none tw-border-1 tw-border-primary-light tw-bg-primary-light tw-bg-opacity-5 tw-text-f1-white tw-text-center">
            <option v-for="timezone in getTimezones" :value="timezone.value" :key="timezone.value">
              {{ timezone.label }}
            </option>
          </select>
        </div>
      </div>
      <UserInput v-if="isGrandPrix(item)" name="Round" type="number" :step="1"
                 :default-value="(overlayData.overlayArray.filter(gp => gp.season === overlayData.overlayArray[overlayData.overlayArray.length-1].season).length+1).toString()"/>

      <div v-if="isGrandPrix(item)" class="tw-flex tw-flex-row">
        <div class="tw-flex tw-items-center">Sprint</div>
        <div
            class="tw-ml-auto tw-w-16 tw-border-1 tw-border-primary-light tw-rounded tw-bg-primary-light tw-bg-opacity-15 hover:tw-bg-opacity-25 tw-text-f1-white tw-text-center tw-cursor-pointer"
            @click="tempSprintStatus = !tempSprintStatus">
          {{ tempSprintStatus ? 'Yes' : 'No' }}
        </div>
      </div>
    </div>
    <div class="tw-flex tw-border-primary-light tw-none tw-bg-primary-dark tw-p-2 tw-rounded-b-lg">
      <CloseButton @close="exit" class="tw-px-2 tw-py-0.5">
        <div class="tw-mt-0.5">Cancel</div>
      </CloseButton>
      <ContinueButton @continue="close" class="tw-ml-auto tw-px-2 tw-py-0.5">
        <div class="tw-mt-0.5">Add {{ getObjectType(item) }}</div>
      </ContinueButton>
    </div>
  </div>
</template>

<style scoped>

</style>