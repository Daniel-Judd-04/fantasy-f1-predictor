<script>

import {getConstructor, getDriver} from "@/utils/common";

export default {
  name: 'TeamDisplay',
  props: {
    userTeams: {
      type: Array,
      required: true
    },
  },
  data() {
    return {
      comparativeTeam: this.userTeams[0]
    }
  },
  watch: {
    userTeams() {
      this.comparativeTeam = this.userTeams[0];
    }
  },
  methods: {
    getConstructor,
    getDriver,
    updateComparativeTeam() {
      this.comparativeTeam = this.userTeams.find(team => `${team.teamName}-${team.teamOwner}` === document.getElementById('comparativeTeam').value);
      this.$emit('updateComparativeTeam', this.comparativeTeam);
    },
  },
}
</script>

<template>
  <div v-if="userTeams && comparativeTeam"
       class="container tw-flex tw-flex-col tw-h-64 tw-w-1/3 tw-outline tw-outline-1 tw-outline-primary-light -tw-outline-offset-1 tw-rounded-lg tw-text-f1-white tw-bg-gradient-to-bl tw-to-f1-red tw-to-200% tw-from-primary-dark">
    <div class="tw-flex tw-flex-row tw-pt-1 tw-px-2 tw-border-b-1 tw-border-primary-light">
      <select id="comparativeTeam" @change="updateComparativeTeam"
              class="tw-appearance-none tw-text-lg tw-border-0 tw-bg-primary-light tw-bg-opacity-0 tw-font-bold tw-text-f1-white">
        <option :value="`${team.teamName}-${team.teamOwner}`" v-for="team in userTeams" :key="team.code">
          {{ `${team.teamName} (${team.teamOwner})` }}
        </option>
      </select>
    </div>
    <div class="tw-h-full tw-flex tw-flex-col tw-gap-2 tw-bg-primary-dark tw-rounded-b-lg">
      <div class="tw-flex tw-justify-between tw-px-2 tw-pt-0.5">
        <div class="tw-font-medium">Budget: £{{ comparativeTeam.value }}m</div>
        <div class="tw-font-medium">Free Transfers: {{ comparativeTeam.freeTransfers }}</div>
      </div>
      <div class="tw-flex tw-justify-between tw-px-2 tw-pb-0.5 tw-text-sm tw-border-b-1 tw-border-primary-light">
        <div class="tw-flex tw-items-center tw-gap-0.5"><span
            class="material-symbols-outlined tw-text-base tw-mb-0.5">joystick</span>{{ comparativeTeam.averageFantasyPoints.toFixed(0) }}
        </div>
        <div class="tw-flex tw-items-center tw-gap-0.5"><span class="material-symbols-outlined tw-text-base tw-mb-0.5">timer</span>{{
            comparativeTeam.averageRecentPoints.toFixed(0)
          }}
        </div>
        <div class="tw-flex tw-items-center tw-gap-0.5"><span class="material-symbols-outlined tw-text-base tw-mb-0.5">trophy</span>{{ comparativeTeam.averagePoints.toFixed(0) }}
        </div>
        <div class="tw-flex tw-items-center tw-gap-0.5"><span class="material-symbols-outlined tw-text-base tw-mb-0.5">tools_level</span>{{
            comparativeTeam.consistency.toFixed(0)
          }}
        </div>
      </div>
      <div v-if="comparativeTeam.drivers" class="tw-w-full tw-mt-auto tw-flex tw-justify-center tw-px-1 tw-gap-2">
        <div class="tw-w-1/3 tw-border-1 tw-border-primary-light tw-rounded tw-font-medium" v-for="driverId in comparativeTeam.drivers.slice(0, 2)" :key="driverId">
          <div class="tw-pl-1 tw-pt-0.5 tw-text-left tw-text-sm">{{ getDriver(driverId).fullName.split(' ')[1] }}</div>
          <div class="tw-border-t-1 tw-pt-0.5 tw-bg-primary tw-rounded-b tw-border-primary-light tw-px-1 tw-flex tw-justify-between tw-text-xs">
            <div>£{{ getDriver(driverId).fantasyPrice }}m</div>
            <div>{{ getDriver(driverId).fantasyPoints }}</div>
          </div>
        </div>
      </div>
      <div v-if="comparativeTeam.drivers" class="tw-w-full tw-flex tw-justify-center tw-px-1 tw-gap-2">
        <div class="tw-w-1/3 tw-border-1 tw-border-primary-light tw-rounded tw-font-medium" v-for="driverId in comparativeTeam.drivers.slice(2, 5)" :key="driverId">
          <div class="tw-pl-1 tw-pt-0.5 tw-text-left tw-text-sm">{{ getDriver(driverId).fullName.split(' ')[1] }}</div>
          <div class="tw-border-t-1 tw-pt-0.5 tw-bg-primary tw-rounded-b tw-border-primary-light tw-px-1 tw-flex tw-justify-between tw-text-xs">
            <div>£{{ getDriver(driverId).fantasyPrice }}m</div>
            <div>{{ getDriver(driverId).fantasyPoints }}</div>
          </div>
        </div>
      </div>
      <div v-if="comparativeTeam.constructors" class="tw-w-full tw-mb-1 tw-flex tw-justify-center tw-px-1 tw-gap-2">
        <div class="tw-w-1/2 tw-border-1 tw-border-primary-light tw-rounded tw-font-medium" v-for="constructorId in comparativeTeam.constructors" :key="constructorId">
          <div class="tw-pl-1 tw-pt-0.5 tw-text-left tw-text-sm">{{ getConstructor(constructorId).fullName }}</div>
          <div class="tw-border-t-1 tw-pt-0.5 tw-bg-primary tw-rounded-b tw-border-primary-light tw-px-1 tw-flex tw-justify-between tw-text-xs">
            <div>£{{ getConstructor(constructorId).fantasyPrice }}m</div>
            <div>{{ getConstructor(constructorId).fantasyPoints }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  min-width: 300px;
}
</style>