<script>
import ContinueButton from "@/components/common/ContinueButton.vue";
import {
  getCircuitById,
  getConstructor,
  getConstructorFromShortName,
  getDriver,
  getDriverFromShortName,
  getObjectType,
  getTeamColour,
  getTimezones,
  isConstructor,
  isDriver,
  isGrandPrix,
  isUserTeam
} from "@/utils/common";
import UserInput from "@/components/common/UserInput.vue";
import OverlayHeader from "@/components/common/OverlayHeader.vue";
import CloseButton from "@/components/common/CloseButton.vue";
import {OverlayData} from "@/utils/classes";
import {mapGetters} from "vuex";

export default {
  name: 'EditObject',
  props: {
    overlayData: {
      type: OverlayData,
      required: true,
      default: () => (new OverlayData({}, [], null)),
    },
  },
  data() {
    return {
      currentIndex: this.overlayData.overlayArray.findIndex((obj) => obj === this.overlayData.overlayObject),
      tempActiveStatus: this.overlayData.overlayArray[this.overlayData.overlayArray.findIndex((obj) => obj === this.overlayData.overlayObject)].active,
      tempSprintStatus: this.overlayData.overlayArray[this.overlayData.overlayArray.findIndex((obj) => obj === this.overlayData.overlayObject)].sprint,
    }
  },
  watch: {
    overlayData: {
      immediate: true,
      handler() {
        this.currentIndex = this.overlayData.overlayArray.findIndex((obj) => obj === this.overlayData.overlayObject);
        this.tempActiveStatus = this.overlayData.overlayArray[this.currentIndex].active;
        this.tempSprintStatus = this.overlayData.overlayArray[this.currentIndex].sprint;
      }
    },
    currentIndex: {
      immediate: true,
      handler() {
        this.tempActiveStatus = this.overlayData.overlayArray[this.currentIndex].active;
        this.tempSprintStatus = this.overlayData.overlayArray[this.currentIndex].sprint;
      }
    }
  },
  components: {CloseButton, OverlayHeader, UserInput, ContinueButton},
  computed: {
    ...mapGetters(['allConstructors']),
    isLoaded() {
      return this.overlayData.overlayArray.length > 0;
    },
    getObject() {
      if (!this.isLoaded) {
        return {};
      }
      return this.overlayData.overlayArray[this.currentIndex];
    },
    getTitle() {
      if (!this.isLoaded) {
        return 'No Data';
      }
      return `${this.getObject.fullName}`;
    }
  },
  methods: {
    getDriver,
    isUserTeam,
    getTimezones,
    getCircuitById,
    getTeamColour,
    isGrandPrix,
    getObjectType,
    isConstructor,
    isDriver,
    getConstructor,
    async previous() {
      if (this.currentIndex > 0) {
        // Save Data
        await this.save();
        // Decrement index
        this.currentIndex--;
      }
    },
    async next() {
      if (this.currentIndex < this.overlayData.overlayArray.length - 1) {
        // Save Data
        await this.save();
        // Increment index
        this.currentIndex++;
      }
    },
    isDifference() {
      if (this.isLoaded) {
        if (isDriver(this.getObject)) {
          return this.getObject.fullName !== document.getElementById('fullName').value ||
              this.getObject.country !== document.getElementById('country').value ||
              this.getObject.constructor !== parseInt(document.getElementById('constructor').value) ||
              this.getObject.points !== parseFloat(document.getElementById('points').value) ||
              this.getObject.fantasyPoints !== parseInt(document.getElementById('fantasyPoints').value) ||
              this.getObject.fantasyPrice !== parseFloat(document.getElementById('fantasyPrice').value) ||
              this.getObject.active !== this.tempActiveStatus;
        } else if (isConstructor(this.getObject)) {
          return this.getObject.fullName !== document.getElementById('fullName').value ||
              this.getObject.country !== document.getElementById('country').value ||
              this.getObject.fantasyPoints !== parseInt(document.getElementById('fantasyPoints').value) ||
              this.getObject.fantasyPrice !== parseFloat(document.getElementById('fantasyPrice').value) ||
              this.getObject.active !== this.tempActiveStatus;
        } else if (isGrandPrix(this.getObject)) {
          const circuit = getCircuitById(this.getObject.circuit);
          return this.getObject.fullName !== document.getElementById('fullName').value ||
              this.getObject.startTime !== document.getElementById('startDate&Time').value ||
              this.getObject.sprint !== this.tempSprintStatus ||
              circuit.fullName !== document.getElementById('circuitName').value ||
              circuit.locationName !== document.getElementById('circuitLocation').value ||
              circuit.country !== document.getElementById('circuitCountry').value ||
              circuit.timezone !== document.getElementById('timezone').value;
        } else if (isUserTeam(this.getObject)) {
          return this.getObject.name !== document.getElementById('teamName').value ||
              this.getObject.owner !== document.getElementById('teamOwner').value ||
              this.getObject.drivers.map(driverId => getDriver(driverId).shortName).join(',') !== document.getElementById('drivers').value ||
              this.getObject.constructors.map(constructorId => getConstructor(constructorId).shortName).join(',') !== document.getElementById('constructors').value ||
              this.getObject.freeTransfers !== parseInt(document.getElementById('freeTransfers').value) ||
              this.getObject.remainingBudget !== parseFloat(document.getElementById('remainingBudget').value);
        }
      }
      return false;
    },
    async save() {
      if (!this.isDifference()) {
        return;
      }
      let updatedObject = {};
      if (isDriver(this.getObject)) {
        updatedObject = {
          objectType: 'drivers',
          driverId: this.getObject.driverId,
          constructor: parseInt(document.getElementById('constructor').value),
          fullName: document.getElementById('fullName').value,
          country: document.getElementById('country').value,
          points: parseFloat(document.getElementById('points').value),
          fantasyPoints: parseInt(document.getElementById('fantasyPoints').value),
          fantasyPrice: parseFloat(document.getElementById('fantasyPrice').value),
          active: this.tempActiveStatus,
        }
      } else if (isConstructor(this.getObject)) {
        updatedObject = {
          objectType: 'constructors',
          constructorId: this.getObject.constructorId,
          fullName: document.getElementById('fullName').value,
          country: document.getElementById('country').value,
          fantasyPoints: parseInt(document.getElementById('fantasyPoints').value),
          fantasyPrice: parseFloat(document.getElementById('fantasyPrice').value),
          active: this.tempActiveStatus,
        }
      } else if (isGrandPrix(this.getObject)) {
        let updatedCircuit = {
          circuitId: this.getObject.circuit,
          fullName: document.getElementById('circuitName').value,
          locationName: document.getElementById('circuitLocation').value,
          country: document.getElementById('circuitCountry').value,
          timezone: document.getElementById('timezone').value,
        };
        updatedObject = {
          objectType: 'grands-prix',
          grandPrixId: this.getObject.grandPrixId,
          fullName: document.getElementById('fullName').value,
          startTime: document.getElementById('startDate&Time').value + ':00Z',
          sprint: this.tempSprintStatus,
          circuit: updatedCircuit,
        }
      } else if (isUserTeam(this.getObject)) {
        updatedObject = {
          objectType: 'user-teams',
          teamId: this.getObject.teamId,
          name: document.getElementById('teamName').value,
          owner: document.getElementById('teamOwner').value,
          freeTransfers: parseInt(document.getElementById('freeTransfers').value),
          remainingBudget: parseFloat(document.getElementById('remainingBudget').value),
          driverIds: document.getElementById('drivers').value.split(',').map(shortName => getDriverFromShortName(shortName).driverId),
          constructorIds: document.getElementById('constructors').value.split(',').map(shortName => getConstructorFromShortName(shortName).constructorId),
        }
      }

      // Save Data
      if (await this.$store.dispatch('update', updatedObject)) {
        this.$emit('success', `${getObjectType(this.getObject)} has been updated successfully`);
      } else {
        this.$emit('error', `Failed to update ${getObjectType(this.getObject)}.`);
        console.error('Error updating: ', updatedObject);
      }
    },
    async close() {
      // Save Data
      await this.save()
      // Exit
      this.exit();
    },
    deleteObject() {
      this.$emit('openOverlay', new OverlayData(this.getObject, this.overlayData.overlayArray, 'DeleteObject'));
    },
    exit() {
      this.$emit('exit');
    },
    exitAll() {
      this.$emit('exitAll');
    }
  }
}
</script>

<template>
  <div :class="[`tw-to-${getTeamColour(getObject)}`]"
       class="tw-bg-gradient-to-bl tw-to-100% tw-from-primary-dark tw-drop-shadow-2xl tw-outline tw-outline-1 -tw-outline-offset-1 tw-outline-primary-light tw-rounded-lg tw-flex tw-flex-col tw-text-f1-white">
    <OverlayHeader :deletable="isConstructor(getObject) || isDriver(getObject)" :country="getObject.country"
                   @exit="exitAll" @deleteObject="deleteObject">{{ getTitle }}
    </OverlayHeader>
    <div class="tw-flex tw-gap-1 tw-px-1 tw-items-center tw-bg-primary-dark tw-border-primary-light tw-border-t-1">
      <div @click="previous" :class="[`${currentIndex > 0 ? '' : 'tw-opacity-50 tw-pointer-events-none'}`]"
           class="tw-cursor-pointer tw-transition-opacity tw-flex tw-items-center">
        <span class="material-symbols-outlined tw-text-4xl tw-font-light tw-leading-none">keyboard_arrow_left</span>
      </div>
      <div class="tw-flex tw-flex-col tw-gap-2 tw-py-2">
        <!--        User Team:  Remaining Budget, Free Transfers, Con, Dri -->
        <UserInput name="Full Name" v-if="isDriver(getObject) || isConstructor(getObject) || isGrandPrix(getObject)" :default-value="getObject.fullName"/>
        <UserInput name="Country" v-if="isDriver(getObject) || isConstructor(getObject)" :default-value="getObject.country"/>
        <div class="tw-flex" v-if="isDriver(getObject)">
          <div>Constructor</div>
          <select id="constructor"
                  class="tw-w-52 tw-px-2 tw-appearance-none tw-ml-auto tw-border-1 tw-border-primary-light tw-bg-primary-light tw-bg-opacity-5 tw-text-f1-white tw-text-center">
            <option :selected="constructor.constructorId === getObject.constructor" :value="constructor.constructorId" v-for="constructor in allConstructors"
                    :key="constructor.code">
              {{ constructor.fullName }}
            </option>
          </select>
        </div>
        <UserInput name="Points" type="number" :step="1" v-if="isDriver(getObject)" :default-value="getObject.points.toString()"/>
        <UserInput name="Fantasy Points" type="number" :step="1" v-if="isDriver(getObject) || isConstructor(getObject)"
                   :default-value="getObject.fantasyPoints.toString()"/>
        <UserInput name="Fantasy Price" type="number" :step="0.1" v-if="isDriver(getObject) || isConstructor(getObject)"
                   :default-value="getObject.fantasyPrice.toString()"/>
        <div class="tw-flex tw-flex-row" v-if="isDriver(getObject) || isConstructor(getObject)">
          <div class="tw-flex tw-items-center">Active</div>
          <div class="tw-ml-auto tw-w-12 tw-border-1 tw-border-primary-light tw-rounded tw-bg-primary-light tw-bg-opacity-5 tw-text-f1-white tw-text-center tw-cursor-pointer"
               @click="tempActiveStatus = !tempActiveStatus">
            {{ tempActiveStatus ? 'Yes' : 'No' }}
          </div>
        </div>

        <UserInput v-if="isUserTeam(getObject)" name="Team Name" :default-value="getObject.name"/>
        <UserInput v-if="isUserTeam(getObject)" name="Team Owner" :default-value="getObject.owner"/>
        <UserInput v-if="isUserTeam(getObject)" name="Drivers"
                   :default-value="getObject.drivers.map(driverId => getDriver(driverId).shortName).join(',')"/>
        <UserInput v-if="isUserTeam(getObject)" name="Constructors"
                   :default-value="getObject.constructors.map(constructorId => getConstructor(constructorId).shortName).join(',')"/>
        <UserInput v-if="isUserTeam(getObject)" name="Free Transfers" type="number" :step="1" :default-value="getObject.freeTransfers.toString()"/>
        <UserInput v-if="isUserTeam(getObject)" name="Remaining Budget" type="number" :step="0.1" :default-value="getObject.remainingBudget.toString()"/>

        <UserInput v-if="isGrandPrix(getObject)" name="Start Date & Time" type="datetime-local"
                   :default-value="getObject.startTime.split(':').splice(0, 2).join(':')"/>
        <div v-if="isGrandPrix(getObject) && true" class="tw-flex tw-flex-col tw-gap-2 tw-py-2 tw-border-y-1 tw-border-primary-light tw-text-sm">
          <UserInput name="Circuit Name" :default-value="getCircuitById(getObject.circuit).fullName"/>
          <UserInput name="Circuit Location" :default-value="getCircuitById(getObject.circuit).locationName"/>
          <UserInput name="Circuit Country" :default-value="getCircuitById(getObject.circuit).country"/>
          <div class="tw-flex tw-justify-between">
            <div>Timezone</div>
            <select id="timezone"
                    class="tw-w-52 tw-px-2 tw-appearance-none tw-border-1 tw-border-primary-light tw-bg-primary-light tw-bg-opacity-5 tw-text-f1-white tw-text-center">
              <option v-if="!getTimezones().map(tz => tz.value).includes(getCircuitById(getObject.circuit).timezone)"
                      :value="getCircuitById(getObject.circuit).timezone">
                {{ getCircuitById(getObject.circuit).timezone }}
              </option>
              <option v-for="timezone in getTimezones()" :value="timezone.value" :key="timezone.value"
                      :selected="timezone.label.includes(getCircuitById(getObject.circuit).timezone)">
                {{ timezone.label }}
              </option>
            </select>
          </div>
        </div>
        <div v-if="isGrandPrix(getObject)" class="tw-flex tw-flex-row">
          <div class="tw-flex tw-items-center">Sprint</div>
          <div
              class="tw-ml-auto tw-w-16 tw-border-1 tw-border-primary-light tw-rounded tw-bg-primary-light tw-bg-opacity-15 hover:tw-bg-opacity-25 tw-text-f1-white tw-text-center tw-cursor-pointer"
              @click="tempSprintStatus = !tempSprintStatus">
            {{ tempSprintStatus ? 'Yes' : 'No' }}
          </div>
        </div>

      </div>
      <div @click="next" :class="[`${currentIndex < overlayData.overlayArray.length-1 ? '' : 'tw-opacity-50 tw-pointer-events-none'}`]"
           class="tw-cursor-pointer tw-transition-opacity tw-flex tw-items-center">
        <span class="material-symbols-outlined tw-text-4xl tw-font-light tw-leading-none">keyboard_arrow_right</span>
      </div>
    </div>
    <div class="tw-flex tw-justify-between tw-bg-primary-dark tw-p-2 tw-border-primary-light tw-border-t-1 tw-rounded-b-lg">
      <CloseButton @close="exit" class="tw-px-2 tw-py-0.5">
        <div class="tw-mt-0.5">Cancel</div>
      </CloseButton>
      <ContinueButton @continue="close" class="tw-px-2 tw-py-0.5">
        <div class="tw-mt-0.5">Update {{ getObjectType(getObject) }}</div>
      </ContinueButton>
    </div>
  </div>
</template>

<style scoped>

</style>