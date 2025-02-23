<script>

import {getConstructor, getDriver} from "@/utils/common";
import {mapGetters} from "vuex";

export default {
  name: 'TeamDisplay',
  data() {
    return {
      chipIcons: {
        NONE: 'remove',
        WILDCARD: 'shuffle',
        NO_NEGATIVE: 'add_box',
        FINAL_FIX: 'handyman',
        EXTRA_DRS_BOOST: 'ifl',
        AUTOPILOT: 'travel',
        LIMITLESS: 'all_inclusive'
      },
    }
  },
  methods: {
    getConstructor,
    getDriver,
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
      required: true
    }
  },
  computed: {
    ...mapGetters(['allGrandsPrix', 'recommendedTeams']),
    findComparativeTeam() {
      return this.recommendedTeams.filter((t) => t.transfers === 0)[0];
    }
  }
}
</script>

<template>
  <div hidden>
    <!--    Pre-load tailwind colours -->
    <span class="tw-text-red-600"></span>
    <span class="tw-text-amber-600"></span>
    <span class="tw-text-green-600"></span>
  </div>
  <tr :class="team.transfers === 0 && team.activeChip === 'NONE' && 'tw-bg-primary/50'">
    <td v-for="(value, key) in team" :key="key + '-' + value"
        class="tw-text-sm tw-leading-none tw-border tw-border-primary-light tw-py-0.5 tw-px-1">
      <div class="tw-w-fit">
        <div v-if="Array.isArray(value)" class="tw-flex">
          <div v-for="(v, index) in value" :key="v" class="tw-flex tw-gap-0.5">
            <div v-if="index !== 0">,</div>
            <div v-if="key === 'drivers'" :class="`${findComparativeTeam.drivers.includes(v) ? 'tw-text-white/40' : ''}`">{{ getDriver(v).shortName }}</div>
            <div v-else :class="`${findComparativeTeam.constructors.includes(v) ? 'tw-text-white/40' : ''}`">{{ getConstructor(v).shortName }}</div>
          </div>
        </div>
        <div v-else-if="key === 'totalCost'" :class="getRecommendationColor(value, (findComparativeTeam[key] + comparativeTeam.remainingBudget), 0.1)">
          £{{ value.toFixed(1) }}m
        </div>
        <div v-else-if="key === 'transfers'" class="tw-flex tw-items-center">
          <div class="tw-w-3">{{ value }}</div>
          <span class="material-symbols-outlined tw-text-sm tw-font-light">shuffle</span>
        </div>
        <div v-else-if="key === 'activeChip'" class="tw-flex tw-items-center" :class="value !== 'NONE' && 'tw-text-blue-400'">
          <span class="material-symbols-outlined tw-text-sm tw-leading-none">{{ chipIcons[value] }}</span>
        </div>
        <div v-else-if="typeof value === 'number'" :class="`${getRecommendationColor(value, findComparativeTeam[key], 0.01, true, true)}`">
          {{ value.toFixed(0) }}
        </div>
        <div v-else>{{ value }}</div>
      </div>
    </td>
  </tr>
</template>

<style scoped>

</style>