<script>

import {getConstructor, getDriver} from "@/utils/common";
import {mapGetters} from "vuex";

export default {
  name: 'TeamDisplay',
  methods: {
    getConstructor,
    getDriver,
    isTransfer(type, id) {
      if (type === 'driver') {
        return this.comparativeTeam.drivers.includes(id);
      } else if (type === 'constructor') {
        return this.comparativeTeam.constructors.includes(id);
      }
    },
    getRecommendationColor(value, limit, freedom, inverseLimit = false, inverseFreedom = false) {
      let color = null;
      if (inverseLimit && value < limit) {
        color = 'red'; // : Math.abs(value - limit) < (value * freedom) ? 'amber' : 'green';
      } else if (!inverseLimit && value > limit) {
        color = 'red'; // : Math.abs(value - limit) > (value * freedom) ? 'amber' : 'green';
      } else if (inverseFreedom) {
        color = Math.abs(value - limit) < (value * freedom) ? 'amber' : 'green';
      } else {
        color = Math.abs(value - limit) > (value * freedom) ? 'amber' : 'green';
      }

      return 'tw-text-' + color + '-600';
    }
  },
  props: {
    team: {
      type: Object,
      required: true
    },
    comparativeTeam: {
      type: Object,
      required: false
    }
  },
  computed: {
    ...mapGetters(['allGrandsPrix']),
    calculateTransfers() {
      let transfers = 0;
      for (let driverId of this.team.drivers) {
        if (!this.comparativeTeam.drivers.includes(driverId)) {
          transfers++;
        }
      }
      for (let constructorId of this.team.constructors) {
        if (!this.comparativeTeam.constructors.includes(constructorId)) {
          transfers++;
        }
      }
      return transfers;
    },
  }
}
</script>

<template>
  <div v-if="false">
    <!--    Pre-load tailwind colours -->
    <span class="tw-text-red-600"></span>
    <span class="tw-text-amber-600"></span>
    <span class="tw-text-green-600"></span>
  </div>
  <div v-if="team" class="tw-flex tw-border-t-1 tw-border-l-1 tw-rounded-tl-lg tw-border-primary-light tw-py-1 tw-gap-2 tw-text-f1-white">
    <div class="tw-w-1/12 tw-border-r-1 tw-border-primary-light tw-flex tw-justify-center tw-items-center tw-px-1">
      <div class="tw-pt-0.5 tw-font-medium" :class="`${getRecommendationColor(team.value, comparativeTeam.value, 0.1)}`">
        £{{ team.value.toFixed(1) }}m
      </div>
    </div>
    <div class="tw-w-5/12 tw-flex tw-justify-between tw-font-medium">
      <div class="tw-flex tw-items-center tw-justify-center tw-w-9"> <!-- Applied Chip (?) -->
        <span v-if="team.activeChip === 'Final Fix'" class="material-symbols-outlined">handyman</span>
        <span v-else-if="team.activeChip === 'Auto Pilot'" class="material-symbols-outlined">travel</span>
        <span v-else-if="team.activeChip === 'Limitless'" class="material-symbols-outlined-sm">all_inclusive</span>
        <span v-else-if="team.activeChip === 'Wildcard'" class="material-symbols-outlined tw-text-base">shuffle_on</span>
        <span v-else-if="team.activeChip === 'No Negative'" class="material-symbols-outlined">add_box</span>
        <span v-else-if="team.activeChip === 'Extra DRS'" class="material-symbols-outlined">ifl</span>
      </div>
      <div class="tw-mt-0.5 tw-w-1/5" :class="`${getRecommendationColor(team.averageFantasyPoints, comparativeTeam.averageFantasyPoints, 0.1, true, true)}`">
        {{ team.averageFantasyPoints.toFixed(0) }}
      </div>
      <div class="tw-mt-0.5 tw-w-1/5" :class="`${getRecommendationColor(team.averageRecentPoints, comparativeTeam.averageRecentPoints, 0.1, true, true)}`">
        {{ team.averageRecentPoints.toFixed(0) }}
      </div>
      <div class="tw-mt-0.5 tw-w-1/5" :class="`${getRecommendationColor(team.averagePoints, comparativeTeam.averagePoints, 0.1, true, true)}`">
        {{ team.averagePoints.toFixed(0) }}
      </div>
      <div class="tw-mt-0.5 tw-w-1/5" :class="`${getRecommendationColor(team.consistency, comparativeTeam.consistency, 0.1, true, true)}`">
        {{ team.consistency.toFixed(0) }}
      </div>
    </div>
    <div class="tw-w-6/12 tw-flex tw-gap-1">
      <div class="tw-flex tw-w-7/12">
        <div :class="`${isTransfer('driver', driverId) ? 'tw-text-primary-light' : ''}`"
             class="tw-border-l-1 tw-border-primary-light tw-w-1/5 tw-pt-1 tw-text-sm"
             v-for="driverId in team.drivers" :key="driverId">
          {{ getDriver(driverId).shortName }}
        </div>
      </div>
      <div class="tw-border-r-1 tw-border-primary-light tw-flex tw-w-4/12">
        <div :class="`${isTransfer('constructor', constructorId) ? 'tw-text-primary-light' : ''}`"
             class="tw-border-l-1 tw-border-primary-light tw-w-1/2 tw-pt-1 tw-text-sm"
             v-for="constructorId in team.constructors"
             :key="constructorId">
          {{ getConstructor(constructorId).shortName }}
        </div>
      </div>
      <div :class="`${getRecommendationColor(calculateTransfers, comparativeTeam.freeTransfers, 0.5, false, true)}`" class="tw-w-1/12 tw-justify-center tw-flex tw-items-center">
        <div class="tw-mt-0.5">
          {{ calculateTransfers }}
        </div>
        <div class="tw-flex tw-items-center tw-mt-0.5">
          <span class="material-symbols-outlined tw-text-sm tw-font-light">shuffle</span>
        </div>
      </div>
    </div>

  </div>
</template>

<style scoped>

</style>