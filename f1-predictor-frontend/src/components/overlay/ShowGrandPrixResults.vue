<script>

import OverlayHeader from "@/components/common/OverlayHeader.vue";
import {OverlayData} from "@/utils/classes";
import {formatDate, getConstructor, getDriver, getOrdinalSuffix} from "@/utils/common";

export default {
  name: 'ShowGrandPrixResults',
  components: {OverlayHeader},
  props: {
    overlayData: {
      type: OverlayData,
      required: true,
      default: () => (new OverlayData({}, [], null))
    }
  },
  methods: {
    formatDate,
    getConstructor,
    getDriver,
    getResults() {
      return this.overlayData.overlayObject.results
          .filter(result => result.sessionType === 'R') // Only want RACE results
          .map(result => ({
            ...result,
            position: result.position === 0 ? 100 : result.position, // 0 is DNF
          }))
          .sort((a, b) => a.position - b.position); // Sort by position
    },
    getTopResults() {
      console.log(this.getResults());
      const topResults = this.getResults().slice(0, 3);
      return [topResults[1], topResults[0], topResults[2]];
    },
    formatPosition(position) {
      if (position === 100) {
        return 'DNF';
      } else if (position === 1) {
        return 'Winner';
      }

      return getOrdinalSuffix(position);
    },
    getRemainingResults() {
      return this.getResults().slice(3);
    },
    getHalfResults(left) {
      let halfResults = [];
      for (let i = 0; i < this.getRemainingResults().length; i++) {
        if (i % 2 === 0 && left) {
          halfResults.push(this.getRemainingResults()[i]);
        } else if (i % 2 !== 0 && !left) {
          halfResults.push(this.getRemainingResults()[i]);
        }
      }
      return halfResults;
    },
    getHeadshotUrl(driverId) {
      const baseUrl = "https://media.formula1.com/d_driver_fallback_image.png/content/dam/fom-website/drivers/";
      const driver = this.getDriver(driverId);

      const letter_filter = driver.fullName.charAt(0).toUpperCase();
      const png = driver.fullName.toLowerCase().split(' ').map(name => name.slice(0, 3)).join('') + '01';
      const name_filter = png.toUpperCase() + "_" + driver.fullName.split(' ').join('_');

      console.log(`${baseUrl}${letter_filter}/${name_filter}/${png}.png`);

      return `${baseUrl}${letter_filter}/${name_filter}/${png}.png`;
    },
    calculatePositionsGained(driverId) {
      const qualifyingResult = this.overlayData.overlayObject.results.filter(result => result.sessionType === 'Q' && result.driver === driverId)[0];
      const raceResult = this.overlayData.overlayObject.results.filter(result => result.sessionType === 'R' && result.driver === driverId)[0];
      return qualifyingResult.position - raceResult.position;
    },
    exitAll() {
      this.$emit('exitAll');
    }
  }
}
</script>

<template>
  <div
      class="tw-bg-primary-dark tw-w-1/2 tw-bg-gradient-to-bl tw-to-200% tw-from-primary-dark tw-to-f1-red tw-drop-shadow-2xl tw-outline tw-outline-1 -tw-outline-offset-1 tw-outline-primary-light tw-rounded-lg tw-flex tw-flex-col tw-text-f1-white">
    <OverlayHeader @exit="exitAll" :country="overlayData.overlayObject.country">
      <div class="tw-flex tw-flex-col tw-gap-1">
        <div class="tw-leading-none">
          {{ overlayData.overlayObject.fullName }}
        </div>
        <div class="tw-font-light tw-text-left tw-text-base tw-leading-none">
          {{ formatDate(overlayData.overlayObject.startTime) }}
        </div>
      </div>
    </OverlayHeader>
    <div class="tw-flex tw-flex-col tw-gap-2 tw-bg-primary-dark tw-border-t-1 tw-border-primary-light tw-p-2 tw-pt-2 tw-rounded-b-lg">
      <div class="tw-flex tw-items-end tw-gap-2">
        <div v-for="result in getTopResults()" :key="result"
             class="tw-flex tw-flex-col tw-w-1/3 tw-bg-gradient-to-b tw-from-primary-dark tw-to-[150%] tw-outline tw-outline-1 tw-outline-primary-light -tw-outline-offset-1 tw-rounded"
             :class="['tw-to-team-' + getConstructor(result.constructor).shortName]">
          <div class="tw-flex tw-flex-col tw-pt-1 tw-justify-between tw-h-full tw-bg-primary-dark tw-bg-opacity-50">
            <div class="tw-font-bold tw-text-2xl">
              {{ formatPosition(result.position) }}
            </div>
            <div class="tw-bg-contain tw-bg-bottom tw-aspect-square tw-mx-auto"
                 :class="[result.position === 1 ? 'tw-w-32' : 'tw-w-24']"
                 :style="{ backgroundImage: `url(${getHeadshotUrl(result.driver)})` }"></div>
          </div>
          <div class="tw-flex tw-mt-auto tw-p-2 tw-border-t-1 tw-rounded-b tw-border-primary-light tw-bg-primary tw-bg-opacity-85">
            <div class="tw-flex tw-flex-col tw-gap-1 tw-text-left">
              <div class="tw-font-medium tw-leading-none">{{ getDriver(result.driver).fullName }}</div>
              <div class="tw-font-light tw-text-sm tw-leading-none">{{ getConstructor(result.constructor).fullName }}</div>
            </div>
            <div v-if="result.position !== 100 && calculatePositionsGained(result.driver) !== 0" class="tw-flex tw-ml-auto tw-items-center">
              <span v-if="calculatePositionsGained(result.driver) > 5" class="material-symbols-outlined tw-text-green-700">keyboard_double_arrow_up</span>
              <span v-else-if="calculatePositionsGained(result.driver) > 0" class="material-symbols-outlined tw-text-green-700">keyboard_arrow_up</span>
              <span v-else-if="calculatePositionsGained(result.driver) < 0" class="material-symbols-outlined tw-text-red-800">keyboard_arrow_down</span>
              <span v-else-if="calculatePositionsGained(result.driver) < -5" class="material-symbols-outlined tw-text-red-800">keyboard_double_arrow_down</span>
              <div class="tw-w-3">{{ Math.abs(calculatePositionsGained(result.driver)) }}</div>
            </div>
          </div>
        </div>
      </div>
      <div class="tw-flex tw-gap-2">
        <div v-for="side in ['LEFT', 'RIGHT']" :key="side"
             class="tw-w-1/2 tw-flex tw-flex-col tw-gap-2">
          <div v-for="result in getHalfResults(side === 'LEFT')" :key="result"
               :class="`${result.position === 100 ? 'tw-opacity-50' : ''}`"
               class="tw-float-left tw-flex tw-items-center tw-px-2 tw-border-1 tw-border-primary-light tw-rounded">
            <div class="tw-w-10 tw-text-left tw-text-sm">{{ formatPosition(result.position) }}</div>
            <div>{{ getDriver(result.driver).fullName }}</div>
            <div v-if="result.position !== 100 && calculatePositionsGained(result.driver) !== 0" class="tw-flex tw-ml-auto">
              <span v-if="calculatePositionsGained(result.driver) > 5" class="material-symbols-outlined tw-text-green-700">keyboard_double_arrow_up</span>
              <span v-else-if="calculatePositionsGained(result.driver) > 0" class="material-symbols-outlined tw-text-green-700">keyboard_arrow_up</span>
              <span v-else-if="calculatePositionsGained(result.driver) < 0" class="material-symbols-outlined tw-text-red-800">keyboard_arrow_down</span>
              <span v-else-if="calculatePositionsGained(result.driver) < -5" class="material-symbols-outlined tw-text-red-800">keyboard_double_arrow_down</span>
              <div class="tw-w-3">{{ Math.abs(calculatePositionsGained(result.driver)) }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>