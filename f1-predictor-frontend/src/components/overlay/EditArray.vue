<script>
import ContinueButton from "@/components/common/ContinueButton.vue";
import {getDriver, getObjectType, isConstructor, isDriver, isGrandPrix, isUserTeam, sort} from "@/utils/common";
import {mapGetters} from "vuex";
import OverlayHeader from "@/components/common/OverlayHeader.vue";
import {OverlayData} from "@/utils/classes";

export default {
  name: 'EditArray',
  props: {
    overlayData: {
      type: OverlayData,
      required: true,
      default: () => (new OverlayData({}, [], null)),
    },
  },
  data() {
    return {
      grandPrixYear: 2023,
      sortMethod: 1,
      sortMethods: ['Points', 'Fantasy Points', 'Fantasy Price'],
      addedObject: false,
    }
  },
  components: {OverlayHeader, ContinueButton},
  computed: {
    ...mapGetters(['allDrivers', 'allConstructors', 'userTeams']),
    item() {
      return this.overlayData.overlayArray[0];
    },
    getEditArray() {
      if (this.overlayData.overlayArray.length === 0) return [];

      if (isGrandPrix(this.item)) {
        console.log(this.overlayData.overlayArray.filter(gp => gp.season === this.grandPrixYear));
        return this.overlayData.overlayArray.filter(gp => gp.season === this.grandPrixYear);
      }
      return this.overlayData.overlayArray;
    },
    maxCol() {
      return Math.ceil(this.getEditArray.length / 10);
    },
  },
  methods: {
    isGrandPrix,
    getObjectType,
    isUserTeam,
    isConstructor,
    isDriver,
    getDriver,
    edit(object) {
      this.$emit('openOverlay', new OverlayData(object, this.overlayData.overlayArray, 'EditObject'));
    },
    add() {
      this.$emit('openOverlay', new OverlayData(null, this.overlayData.overlayArray, 'AddObject'));
    },
    async save() {
      if (!isUserTeam(this.item)) {
        // Update sorting method
        sort(this.overlayData.overlayArray, this.sortMethods[this.sortMethod]);
      }
    },
    async close() {
      await this.save();
      // Exit
      this.exit();
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
  <div
      class="tw-w-96 tw-bg-gradient-to-bl tw-to-100% tw-from-primary-dark tw-to-f1-red tw-outline tw-outline-1 -tw-outline-offset-1 tw-outline-primary-light tw-drop-shadow-2xl tw-rounded-lg tw-flex tw-flex-col tw-text-f1-white">
    <OverlayHeader @exit="exitAll">Edit {{ getObjectType(item, true) }}</OverlayHeader>
    <div class="tw-border-primary-light tw-border-y-1 tw-flex tw-flex-col tw-gap-2 tw-py-2 tw-px-2 tw-bg-primary-dark">
      <select v-model="sortMethod" v-if="!(isUserTeam(item) || isGrandPrix(item))"
              class="tw-px-2 tw-appearance-none tw-border-1 tw-border-primary-light tw-bg-primary-light tw-bg-opacity-5 tw-text-f1-white tw-text-center">
        <option disabled>--- Sort By ---</option>
        <option :value="index" v-for="(sortMethod, index) in sortMethods" :key="index">
          {{ sortMethod }}
        </option>
      </select>
      <select v-model="grandPrixYear" v-if="isGrandPrix(item)"
              class="tw-px-2 tw-appearance-none tw-border-1 tw-border-primary-light tw-bg-primary-light tw-bg-opacity-5 tw-text-f1-white tw-text-center">
        <option disabled>--- Grands Prix Season ---</option>
        <option :value="season" v-for="season in [...new Set(overlayData.overlayArray.map(gp => gp.season))]" :key="season">
          {{ season }}
        </option>
      </select>
      <div class="tw-flex tw-gap-2 tw-w-full">
        <div v-for="col in maxCol" :key="col" class="tw-flex tw-flex-col tw-w-full tw-gap-2">
          <div class="tw-px-1 tw-flex tw-gap-1 tw-border-t-1 tw-border-l-1 tw-border-primary-light tw-rounded-tl"
               :class="`${Object.prototype.hasOwnProperty.call(object, 'active') && !object.active ? 'tw-opacity-50' : ''}`"
               v-for="object in getEditArray.slice((col - 1) * 10, col * 10)" :key="object.code">
            <div :title="`Edit ${getObjectType(object)}`">
              <span @click="edit(object)" class="material-symbols-outlined tw-flex tw-items-center tw-justify-center tw-cursor-pointer tw-text-xl">tune</span>
            </div>
            <div class="tw-flex tw-items-center" :class="`${maxCol > 2 ? 'tw-text-sm tw-mt-0.5' : ''}`">
              <div v-if="isUserTeam(object)">{{ object.name }} ({{ object.owner }})</div>
              <div v-else-if="isDriver(object)">{{ object.fullName.split(' ')[1] }}</div>
              <div v-else-if="isGrandPrix(object)" class="tw-text-xs">{{ object.fullName.replace(' Grand Prix', '') }}</div>
              <div v-else>{{ object.fullName }}</div>
            </div>
          </div>
          <ContinueButton v-if="!isGrandPrix(item) && !(getEditArray.length % 10 === 0 || getEditArray.length <= 10) && col === maxCol" @continue="add()">
            <span class="material-symbols-outlined tw-font-light">add</span>
          </ContinueButton>
        </div>
      </div>
      <ContinueButton v-if="!isGrandPrix(item) && (getEditArray.length % 10 === 0 || getEditArray.length <= 10)" @continue="add()">
        <span class="material-symbols-outlined tw-font-light">add</span>
      </ContinueButton>
    </div>
    <div class="tw-flex tw-border-primary-light tw-none tw-bg-primary-dark tw-p-2 tw-rounded-b-lg">
      <ContinueButton @continue="close" class="tw-ml-auto tw-px-2 tw-py-0.5">
        <div class="tw-mt-0.5" @continue="close">Close</div>
      </ContinueButton>
    </div>
  </div>
</template>

<style scoped>

</style>