<script>

import {getConstructor, getConstructorPoints, getDriver, getGrandPrix} from "@/utils/common";
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
    getNumberOfRaces() {
      const season = 2023;
      return this.allGrandsPrix.filter(gp => gp.season === season && gp.raceResults.length !== 0).length
    },
    calculateAverageFantasyPoints() {
      let totalFantasyPoints = 0;
      for (let driverId of this.team.drivers) totalFantasyPoints += getDriver(driverId).fantasyPoints;
      for (let constructorId of this.team.constructors) totalFantasyPoints += getConstructor(constructorId).fantasyPoints;
      return (totalFantasyPoints / this.getNumberOfRaces).toFixed(0);
    },
    calculateAverageRecentPoints() {
      const season = 2023;
      let averageRecentPoints = 0;

      function getObjectAverageRecentPoints(teamObject) {
        if (teamObject.raceResults.length === 0) return;
        const teamObjectRaceResults = teamObject.raceResults.filter(result => getGrandPrix(result.grandPrix).season === season).sort((a, b) => a.round - b.round);
        if (teamObjectRaceResults.length === 0) return;
        let teamObjectRecentPoints = 0;
        for (let i = 0; i < teamObjectRaceResults.length; i++) teamObjectRecentPoints += teamObject.raceResults[i].position * (i + 1);
        return teamObjectRecentPoints / teamObjectRaceResults.length;
      }

      for (let driverId of this.team.drivers) {
        averageRecentPoints += getObjectAverageRecentPoints(getDriver(driverId));
      }
      for (let constructorId of this.team.constructors) {
        averageRecentPoints += getObjectAverageRecentPoints(getConstructor(constructorId));
      }
      return (averageRecentPoints / this.getNumberOfRaces).toFixed(0);
    },
    calculateAveragePoints() {
      let totalPoints = 0;
      for (let driverId of this.team.drivers) totalPoints += getDriver(driverId).points;
      for (let constructorId of this.team.constructors) totalPoints += getConstructorPoints(constructorId);
      return (totalPoints / this.getNumberOfRaces).toFixed(0);
    },
    calculateConsistency() {
      const season = 2023;
      let totalDifference = 0;

      function getObjectDifference(teamObject, averagePoints) {
        if (teamObject.raceResults.length === 0) return;
        const teamObjectRaceResults = teamObject.raceResults.filter(result => getGrandPrix(result.grandPrix).season === season).sort((a, b) => a.round - b.round);
        if (teamObjectRaceResults.length === 0) return;
        let teamObjectDifference = 0;
        for (let i = 0; i < teamObjectRaceResults.length; i++) teamObjectDifference += Math.abs(teamObject.raceResults[i].position - averagePoints);
        return teamObjectDifference / teamObjectRaceResults.length;
      }

      for (let driverId of this.team.drivers) {
        totalDifference += getObjectDifference(getDriver(driverId), this.calculateAveragePoints)
      }
      for (let constructorId of this.team.constructors) {
        totalDifference += getObjectDifference(getConstructor(constructorId), this.calculateAveragePoints)
      }
      return ((1 - (totalDifference / (this.getNumberOfRaces * ((25 + 18 + 1) * 2)))) * 1000).toFixed(0);
    }
  }
}
</script>

<template>
  <div v-if="team" class="tw-flex tw-border-t-1 tw-border-l-1 tw-rounded-tl-lg tw-border-primary-light tw-py-1 tw-gap-2 tw-text-f1-white">
    <div class="tw-w-1/12 tw-border-r-1 tw-border-primary-light tw-flex tw-justify-center tw-items-center tw-px-1"
         :class="`${team.costCap - comparativeTeam.costCap > 0 ? 'tw-text-red-700' : Math.abs(team.costCap - comparativeTeam.costCap) > 1 ? 'tw-text-amber-500' : 'tw-text-green-600'}`">
      <div class="tw-pt-0.5 tw-font-medium">
        £{{ team.costCap }}m
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
      <div class="tw-mt-0.5 tw-w-1/5"> <!-- Average Fantasy Points -->
        {{ calculateAverageFantasyPoints }}
      </div>
      <div class="tw-mt-0.5 tw-w-1/5"> <!-- Average Recent Points -->
        {{ calculateAverageRecentPoints }}
      </div>
      <div class="tw-mt-0.5 tw-w-1/5"> <!-- Average Points -->
        {{ calculateAveragePoints }}
      </div>
      <div class="tw-mt-0.5 tw-w-1/5"> <!-- Consistency -->
        {{ calculateConsistency }}
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
      <div :class="`${calculateTransfers > 3 ? 'tw-text-red-700' : calculateTransfers > 1 ? 'tw-text-amber-500' : 'tw-text-green-600'}`"
           class="tw-w-1/12 tw-justify-center tw-flex tw-items-center">
        <div class="tw-mt-0.5">
          {{ calculateTransfers }}
        </div>
        <div class="tw-flex tw-items-center tw-mt-0.5">
          <span class="material-symbols-outlined tw-text-sm">shuffle</span>
        </div>
      </div>
    </div>

  </div>
</template>

<style scoped>

</style>