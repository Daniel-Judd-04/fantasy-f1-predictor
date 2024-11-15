<script>

import {getConstructor, getDriver} from "@/utils/common";

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
    }
  }
}
</script>

<template>
  <div v-if="team" class="tw-flex tw-border-t-1 tw-border-x-1 tw-rounded-t-lg tw-border-primary-light tw-bg-f1-black tw-py-1 tw-gap-2 tw-text-f1-white">
    <div class="tw-w-1/12 tw-border-r-1 tw-border-primary-light tw-flex tw-justify-center tw-items-center tw-px-1"
         :class="`${team.costCap - comparativeTeam.costCap > 0 ? 'tw-text-red-700' : Math.abs(team.costCap - comparativeTeam.costCap) > 1 ? 'tw-text-amber-500' : 'tw-text-green-600'}`">
      <div class="tw-pt-0.5">
        £{{ team.costCap }}m
      </div>
    </div>
    <div class="tw-w-5/12 tw-flex tw-justify-between">
      <div class="tw-flex tw-items-center tw-justify-center tw-w-5">
        <span v-if="team.activeChip === 'Final Fix'" class="material-symbols-outlined">handyman</span>
        <span v-else-if="team.activeChip === 'Auto Pilot'" class="material-symbols-outlined">travel</span>
        <span v-else-if="team.activeChip === 'Limitless'" class="material-symbols-outlined-sm">all_inclusive</span>
        <span v-else-if="team.activeChip === 'Wildcard'" class="material-symbols-outlined tw-text-base">shuffle_on</span>
        <span v-else-if="team.activeChip === 'No Negative'" class="material-symbols-outlined">add_box</span>
        <span v-else-if="team.activeChip === 'Extra DRS'" class="material-symbols-outlined">ifl</span>
        <span v-else class="material-symbols-outlined tw-text-sm"></span>
      </div>
      <div class="tw-mt-0.5">
        ARP
      </div>
      <div class="tw-mt-0.5">
        AP
      </div>
      <div class="tw-mt-0.5">
        CON
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