<script>

import {calculateTransfers, getConstructor, getDriver, getTeamColour, getUserTeam} from "@/utils/common";
import {mapGetters} from "vuex";

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
    getTeamColour,
    getConstructor,
    getDriver,
    updateComparativeTeam() {
      this.comparativeTeam = getUserTeam(document.getElementById('comparativeTeam').value);
      console.log("Comparative Team Updated: ", this.comparativeTeam);
      console.log("Comparative Team Drivers: ", document.getElementById('comparativeTeam').value);
      this.$emit('updateComparativeTeam', this.comparativeTeam);
    },
    editTeams() {
      this.$emit('editArray', this.userTeams);
    }
  },
  computed: {
    ...mapGetters(['recommendedTeams']),
    findComparativeTeam() {
      return this.recommendedTeams.filter((t) => calculateTransfers(t, this.comparativeTeam) === 0)[0];
    }
  }
}
</script>

<template>
  <div v-if="userTeams && comparativeTeam"
       class="container tw-flex tw-flex-col tw-h-64 tw-w-1/3 tw-outline tw-outline-1 tw-outline-primary-light -tw-outline-offset-1 tw-rounded-lg tw-text-f1-white tw-bg-gradient-to-bl tw-to-f1-red tw-to-200% tw-from-primary-dark">
    <div class="tw-flex tw-flex-row tw-items-center tw-pl-2 tw-pr-1 tw-border-b-1 tw-border-primary-light">
      <select id="comparativeTeam" @change="updateComparativeTeam"
              class="tw-appearance-none tw-text-lg tw-py-0.5 tw-border-0 tw-bg-primary-light tw-bg-opacity-0 tw-font-medium tw-text-f1-white">
        <option :value="team.teamId" v-for="team in userTeams" :key="team.code">
          {{ `${team.name} (${team.owner.split(' ')[0]})` }}
        </option>
      </select>
      <div class="tw-mt-0.5 tw-mr-0.5 tw-transition-opacity tw-cursor-pointer tw-ml-auto tw-flex tw-items-center">
        <span @click="editTeams()" class="material-symbols-outlined tw-font-light">edit_note</span>
      </div>
    </div>
    <div class="tw-h-full tw-flex tw-flex-col tw-gap-2 tw-bg-primary-dark tw-rounded-b-lg">
      <div class="tw-flex tw-justify-between tw-px-2 tw-pt-0.5">
        <div v-if="findComparativeTeam">Budget: <span class="tw-font-light">£{{ (findComparativeTeam.totalCost + comparativeTeam.remainingBudget).toFixed(1) }}m</span></div>
        <div v-else>Cash: <span class="tw-font-light">£{{ comparativeTeam.remainingBudget.toFixed(1) }}m</span></div>
        <div>Max Transfers: <span class="tw-font-light">{{ comparativeTeam.freeTransfers }}</span></div>
      </div>
      <div v-if="findComparativeTeam" class="tw-flex tw-justify-between tw-px-2 tw-pb-0.5 tw-text-sm tw-border-b-1 tw-border-primary-light">
        <div class="tw-flex tw-items-center tw-gap-0.5">
          <span class="material-symbols-outlined tw-text-base tw-font-light">joystick</span>
          {{ findComparativeTeam.averageFantasyPoints.toFixed(0) }}
        </div>
        <div class="tw-flex tw-items-center tw-gap-0.5">
          <span class="material-symbols-outlined tw-text-base tw-font-light">timer</span>
          {{ findComparativeTeam.averageRecentPoints.toFixed(0) }}
        </div>
        <div class="tw-flex tw-items-center tw-gap-0.5">
          <span class="material-symbols-outlined tw-text-base tw-font-light">trophy</span>
          {{ findComparativeTeam.averagePoints.toFixed(0) }}
        </div>
        <div class="tw-flex tw-items-center tw-gap-0.5">
          <span class="material-symbols-outlined tw-text-base tw-font-light">tools_level</span>
          {{ findComparativeTeam.consistency.toFixed(0) }}
        </div>
      </div>
      <div v-if="comparativeTeam.drivers" class="tw-mt-auto tw-w-full tw-flex tw-justify-center tw-px-2 tw-gap-2">
        <div :class="`tw-to-${getTeamColour(getDriver(driverId))}`"
             class="tw-w-1/3 tw-outline tw-outline-1 tw-outline-primary-light -tw-outline-offset-1 tw-bg-gradient-to-b tw-to-[150%] tw-from-primary-dark tw-rounded tw-font-medium"
             v-for="driverId in comparativeTeam.drivers.slice(0, 2)" :key="driverId">
          <div class="tw-pl-1 tw-text-left tw-text-sm tw-bg-primary-dark tw-rounded-t tw-pt-[1px]">{{ getDriver(driverId).fullName.split(' ')[1] }}</div>
          <div class="tw-border-t-1 tw-py-0.5 tw-bg-primary tw-bg-opacity-50 tw-rounded-b tw-border-primary-light tw-px-1 tw-flex tw-justify-between tw-text-xs tw-font-light">
            <div>£{{ getDriver(driverId).fantasyPrice }}m</div>
            <div>{{ getDriver(driverId).fantasyPoints }}</div>
          </div>
        </div>
      </div>
      <div v-if="comparativeTeam.drivers" class="tw-w-full tw-flex tw-justify-center tw-px-2 tw-gap-2">
        <div :class="`tw-to-${getTeamColour(getDriver(driverId))}`"
             class="tw-w-1/3 tw-outline tw-outline-1 tw-outline-primary-light -tw-outline-offset-1 tw-bg-gradient-to-b tw-to-[150%] tw-from-primary-dark tw-rounded tw-font-medium"
             v-for="driverId in comparativeTeam.drivers.slice(2, 5)" :key="driverId">
          <div class="tw-pl-1 tw-text-left tw-text-sm tw-bg-primary-dark tw-rounded-t tw-pt-[1px]">{{ getDriver(driverId).fullName.split(' ')[1] }}</div>
          <div class="tw-border-t-1 tw-py-0.5 tw-bg-primary tw-bg-opacity-50 tw-rounded-b tw-border-primary-light tw-px-1 tw-flex tw-justify-between tw-text-xs tw-font-light">
            <div>£{{ getDriver(driverId).fantasyPrice }}m</div>
            <div>{{ getDriver(driverId).fantasyPoints }}</div>
          </div>
        </div>
      </div>
      <div v-if="comparativeTeam.constructors" class="tw-w-full tw-pb-2 tw-flex tw-justify-center tw-px-2 tw-gap-2">
        <div :class="`tw-to-${getTeamColour(getConstructor(constructorId))}`"
             class="tw-w-1/2 tw-outline tw-outline-1 tw-outline-primary-light -tw-outline-offset-1 tw-bg-gradient-to-b tw-to-[150%] tw-from-primary-dark tw-rounded tw-font-medium"
             v-for="constructorId in comparativeTeam.constructors" :key="constructorId">
          <div class="tw-pl-1 tw-text-left tw-text-sm tw-bg-primary-dark tw-rounded-t tw-pt-[1px]">{{ getConstructor(constructorId).fullName }}</div>
          <div class="tw-border-t-1 tw-py-0.5 tw-bg-primary tw-bg-opacity-50 tw-rounded-b tw-border-primary-light tw-px-1 tw-flex tw-justify-between tw-text-xs tw-font-light">
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