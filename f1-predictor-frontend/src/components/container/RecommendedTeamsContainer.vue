<script>
import ContinueButton from "@/components/common/ContinueButton.vue";
import CompactTeamDisplay from "@/components/display/CompactTeamDisplay.vue";
import {mapGetters} from "vuex";
import {Sorter} from "@/utils/classes";
import {calculateTransfers} from "@/utils/common";


export default {
  name: 'RecommendedTeamsContainer',
  components: {ContinueButton, CompactTeamDisplay},
  props: {
    comparativeTeam: {
      type: Object,
      required: true
    },
    grandPrix: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      loading: false,
      difference: false,
      params: {
        loadLimit: false,
        chips: {
          wildcard: false,
          no_negative: false,
          final_fix: false,
          extra_drs: false,
          autopilot: false,
          limitless: false,
        }
      },
      startIndex: 0,
      range: 15, // Keep equal
      endIndex: 15, // Keep equal
      visibleTeams: [],
      sortedTeams: [],
      sorter: new Sorter(['Value', 'Average Fantasy Points', 'Average Recent Points', 'Average Points', 'Consistency', 'Transfers'], true),
      animator: {
        interval: null,
        currentValue: 0,
        maxValue: 100,
      },
    }
  },
  watch: {
    recommendedTeams() {
      this.resetVisibleTeams();
    },
    comparativeTeam() {
      this.difference = true;
      this.resetVisibleTeams();
    },
    grandPrix() {
      this.difference = true;
    },
    params: {
      immediate: true,
      handler() {
        this.difference = true;
      }
    }
  },
  computed: {
    ...mapGetters(['recommendedTeams', 'allDrivers', 'allConstructors']),
  },
  methods: {
    calculateTransfers,
    calculatePossibleTeams() {
      const rDrivers = 5; // Constant for number of drivers to pick
      const rConstructors = 2; // Constant for number of constructors to pick
      const totalDrivers = this.allDrivers.filter(driver => driver.active).length;
      const totalConstructors = this.allConstructors.filter(constructor => constructor.active).length;

      // Helper function to calculate nCr
      function combinations(n, r) {
        if (n < r) return 0; // Invalid case
        let numerator = 1; // n! / (n-r)!
        for (let i = 0; i < r; i++) {
          numerator *= (n - i);
        }
        let denominator = 1; // r!
        for (let i = 1; i <= r; i++) {
          denominator *= i;
        }
        return numerator / denominator;
      }

      // Calculate combinations for drivers and constructors
      const driverCombinations = combinations(totalDrivers, rDrivers);
      const constructorCombinations = combinations(totalConstructors, rConstructors);
      const chipsActive = (Object.values(this.params.chips).filter(chip => chip).length) + 1;

      // Total combinations
      return driverCombinations * constructorCombinations * chipsActive;

    },
    startAnimation() {
      if (this.animator.interval) return;

      this.animator.currentValue = 0;
      this.animator.maxValue = parseInt(this.calculatePossibleTeams());

      this.animator.interval = setInterval(() => {
        if (this.animator.currentValue >= this.animator.maxValue || !this.loading) {
          const total = this.animator.maxValue;
          this.animator.currentValue = Math.floor(total / (10 + "e" + (total.toString().length - 4))) * (10 + "e" + (total.toString().length - 4));
          clearInterval(this.animator.interval);
          this.animator.interval = null;
          return;
        }
        this.animator.currentValue = Math.round(Math.min(
            this.animator.currentValue + 1000,
            this.animator.maxValue
        ));
      }, 1000 / 60);
    },
    async loadRecommendedTeams() {
      this.loading = true;
      this.difference = false;
      this.startAnimation();

      // Convert chips to bits (First "1" is for "None" chip)
      const chipsAsBits = "1" + Object.values(this.params.chips).map(chip => chip ? 1 : 0).join('');

      const data = {
        limit: this.params.loadLimit ? 250 : -1,
        chips: chipsAsBits,
        teamId: this.comparativeTeam.teamId,
        grandPrixId: this.grandPrix.grandPrixId,
      };
      if (!await this.$store.dispatch('fetchRecommendedTeams', data)) {
        this.difference = true;
        console.error('Failed to load recommended teams');
      }

      this.loading = false;
    },
    changeSortOrder() {
      this.sorter.sortDesc = !this.sorter.sortDesc;
      this.resetVisibleTeams();
    },
    previousPage() {
      if (this.startIndex > 1) {
        this.startIndex -= this.range;
        this.endIndex -= this.range;
        this.updateVisibleTeams(false);
      }
    },
    nextPage() {
      if (this.endIndex < this.recommendedTeams.length) {
        this.startIndex += this.range;
        this.endIndex += this.range;
        this.updateVisibleTeams(false);
      }
    },
    resetVisibleTeams() {
      this.startIndex = 0;
      this.endIndex = this.range;
      this.updateVisibleTeams();
    },
    updateVisibleTeams(sort = true) {
      let newSortedTeams = [];
      if (sort) {
        const sortOption = document.getElementById('sortOption').value;
        switch (sortOption) {
          case 'Value':
            this.sortedTeams = Array.from(this.recommendedTeams).sort((a, b) => b.totalCost - a.totalCost);
            break;
          case 'Average Fantasy Points':
            this.sortedTeams = Array.from(this.recommendedTeams).sort((a, b) => b.averageFantasyPoints - a.averageFantasyPoints);
            break;
          case 'Average Recent Points':
            this.sortedTeams = Array.from(this.recommendedTeams).sort((a, b) => b.averageRecentPoints - a.averageRecentPoints);
            break;
          case 'Average Points':
            this.sortedTeams = Array.from(this.recommendedTeams).sort((a, b) => b.averagePoints - a.averagePoints);
            break;
          case 'Consistency':
            this.sortedTeams = Array.from(this.recommendedTeams).sort((a, b) => b.consistency - a.consistency);
            break;
          case 'Transfers':
            this.sortedTeams = Array.from(this.recommendedTeams).sort((a, b) => b.transfers - a.transfers);
            break;
          default:
            console.warn('Invalid sort option: ', sortOption);
            this.sortedTeams = Array.from(this.recommendedTeams);
            break;
        }
        newSortedTeams = this.sorter.sortDesc ? this.sortedTeams : this.sortedTeams.reverse();
      } else {
        newSortedTeams = this.sortedTeams;
      }
      this.visibleTeams = newSortedTeams.slice(this.startIndex, this.endIndex);
    },
  },
}
</script>

<template>
  <div
      class="tw-w-full tw-h-full tw-flex tw-flex-col tw-bg-gradient-to-bl tw-to-f1-red tw-from-primary-dark tw-to-200% tw-outline tw-outline-1 tw-outline-primary-light -tw-outline-offset-1 tw-rounded-lg">
    <div class="tw-flex tw-gap-2 tw-border-b-1 tw-border-primary-light tw-p-2 tw-text-f1-white">
      <div class="tw-flex tw-items-center tw-gap-2 tw-mr-auto">
        <div @click="params.loadLimit = !params.loadLimit" title="Limit Load to 250?"
             class="tw-flex tw-items-center tw-gap-0.5 tw-px-1 tw-py-0.5 tw-border-1 tw-border-primary-light tw-rounded tw-bg-primary-light tw-bg-opacity-15 hover:tw-bg-opacity-25 tw-text-f1-white tw-text-center tw-cursor-pointer tw-text-sm">
          <span id="loadLimit" class="material-symbols-outlined tw-text-lg tw-leading-none">{{ params.loadLimit ? 'check' : 'close' }}</span>
          <div class="tw-mr-0.5 tw-leading-none">Load Limit</div>
        </div>
        <div title="Extra DRS" @click="params.chips.extra_drs = !params.chips.extra_drs"
             class="tw-text-blue-400 tw-flex tw-items-center tw-px-0.5 tw-py-0.5 tw-border-1 tw-border-primary-light tw-rounded tw-bg-primary-light tw-bg-opacity-15 hover:tw-bg-opacity-25 tw-text-center tw-cursor-pointer tw-text-sm">
          <span :class="`${params.chips.extra_drs ? '' : 'tw-opacity-50'}`" class="material-symbols-outlined tw-text-lg tw-leading-none tw-transition-opacity">ifl</span>
        </div>
        <div title="Unlimited" @click="params.chips.limitless = !params.chips.limitless"
             class="tw-text-blue-400 tw-flex tw-items-center tw-px-0.5 tw-py-0.5 tw-border-1 tw-border-primary-light tw-rounded tw-bg-primary-light tw-bg-opacity-15 hover:tw-bg-opacity-25 tw-text-center tw-cursor-pointer tw-text-sm">
          <span :class="`${params.chips.limitless ? '' : 'tw-opacity-50'}`" class="material-symbols-outlined tw-text-lg tw-leading-none">all_inclusive</span>
        </div>
        <div title="Final Fix" @click="params.chips.final_fix = !params.chips.final_fix"
             class="tw-text-blue-400 tw-flex tw-items-center tw-px-0.5 tw-py-0.5 tw-border-1 tw-border-primary-light tw-rounded tw-bg-primary-light tw-bg-opacity-15 hover:tw-bg-opacity-25 tw-text-center tw-cursor-pointer tw-text-sm">
          <span :class="`${params.chips.final_fix ? '' : 'tw-opacity-50'}`" class="material-symbols-outlined tw-text-lg tw-leading-none">handyman</span>
        </div>
        <div title="Autopilot" @click="params.chips.autopilot = !params.chips.autopilot"
             class="tw-text-blue-400 tw-flex tw-items-center tw-px-0.5 tw-py-0.5 tw-border-1 tw-border-primary-light tw-rounded tw-bg-primary-light tw-bg-opacity-15 hover:tw-bg-opacity-25 tw-text-center tw-cursor-pointer tw-text-sm">
          <span :class="`${params.chips.autopilot ? '' : 'tw-opacity-50'}`" class="material-symbols-outlined tw-text-lg tw-leading-none">travel</span>
        </div>
        <div title="Wildcard" @click="params.chips.wildcard = !params.chips.wildcard"
             class="tw-text-blue-400 tw-flex tw-items-center tw-px-0.5 tw-py-0.5 tw-border-1 tw-border-primary-light tw-rounded tw-bg-primary-light tw-bg-opacity-15 hover:tw-bg-opacity-25 tw-text-center tw-cursor-pointer tw-text-sm">
          <span :class="`${params.chips.wildcard ? '' : 'tw-opacity-50'}`" class="material-symbols-outlined tw-text-lg tw-leading-none">shuffle</span>
        </div>
        <div title="No Negative" @click="params.chips.no_negative = !params.chips.no_negative"
             class="tw-text-blue-400 tw-flex tw-items-center tw-px-0.5 tw-py-0.5 tw-border-1 tw-border-primary-light tw-rounded tw-bg-primary-light tw-bg-opacity-15 hover:tw-bg-opacity-25 tw-text-center tw-cursor-pointer tw-text-sm">
          <span :class="`${params.chips.no_negative ? '' : 'tw-opacity-50'}`" class="material-symbols-outlined tw-text-lg tw-leading-none">add_box</span>
        </div>

        <Transition>
          <div v-if="!loading && (recommendedTeams.length !== 0) && difference" @click="loadRecommendedTeams"
               class="glow-button tw-cursor-pointer tw-rounded tw-transition-opacity">
            <div class="tw-m-[1px] tw-flex tw-items-center tw-font-bold tw-p-0.5 tw-bg-primary-dark tw-bg-opacity-80 tw-backdrop-blur-lg tw-rounded">
              <span class="material-symbols-outlined tw-text-lg tw-font-light tw-leading-none">refresh</span>
            </div>
          </div>
        </Transition>
      </div>
      <div class="tw-flex">
        <select id="sortOption" @change="resetVisibleTeams"
                class="tw-px-2 tw-appearance-none tw-rounded-r-none tw-border-1 tw-border-primary-light tw-bg-primary-light tw-bg-opacity-5 tw-leading-none tw-text-sm tw-text-f1-white tw-text-center">
          <option disabled>--- Sort By ---</option>
          <option :value="sortOption" v-for="sortOption in sorter.sortOptions" :key="sortOption.code" :selected="sortOption === 'Average Recent Points'">
            {{ sortOption }}
          </option>
        </select>
        <div
            class="tw-flex tw-items-center tw-px-1 tw-border-l-0 tw-border-1 tw-border-primary-light tw-rounded-r tw-bg-primary-light tw-bg-opacity-5 tw-text-f1-white tw-cursor-pointer"
            @click="changeSortOrder()" id="sortOrder" :title="`Sorted in ${sorter.sortDesc ? 'descending' : 'ascending'} order`">
          <span class="material-symbols-outlined tw-text-lg tw-font-light tw-leading-none">{{ sorter.sortDesc ? 'south_east' : 'north_east' }}</span>
        </div>
      </div>
    </div>
    <div v-if="comparativeTeam" class="tw-flex tw-flex-col tw-gap-2 tw-justify-between tw-p-2 tw-h-full tw-bg-primary-dark tw-rounded-b-lg tw-text-f1-white">
      <div v-if="recommendedTeams.length === 0" class="tw-relative tw-h-full tw-flex tw-items-center tw-justify-center">
        <div v-if="loading" class="tw-flex tw-flex-col tw-items-center tw-text-xl">
          <div>Generating teams...</div>
          <div class="tw-text-primary-light tw-text-lg">Analysing over {{ animator.currentValue.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") }} teams</div>
          <div class="loading tw-h-[2px] tw-w-1/2 tw-bg-primary-light tw-rounded-full"/>
        </div>
        <div v-else @click="loadRecommendedTeams" class="glow-button tw-p-[1px] tw-cursor-pointer tw-rounded-full">
          <div
              class="tw-leading-none tw-text-xl tw-font-bold tw-py-3 tw-px-4 tw-bg-primary-dark tw-bg-opacity-80 tw-backdrop-blur-xl tw-rounded-full">
            Generate Teams
          </div>
        </div>
        <div class="tw-text-primary-light tw-absolute tw-bottom-4 tw-border-1 tw-border-primary-light tw-rounded-xl">
          <div class="tw-border-b-1 tw-border-primary-light tw-p-1 tw-font-bold tw-pt-1">Generating Recommended Teams For:</div>
          <div class="tw-flex tw-border-b-1 tw-border-primary-light">
            <div class="tw-w-1/2 tw-py-1 tw-px-3 tw-font-medium tw-pt-1">Team: <span class="tw-font-normal">{{ comparativeTeam.name }}</span></div>
            <div class="tw-w-1/2 tw-py-1 tw-ml-auto tw-border-l-1 tw-border-primary-light tw-font-medium tw-pt-1">{{ grandPrix.fullName }}</div>
          </div>
        </div>
      </div>
      <div v-else class="tw-flex tw-flex-col tw-gap-2 tw-justify-between tw-h-full">
        <div class="tw-flex tw-flex-col tw-max-h-[55vh] tw-overflow-y-auto">
          <table class="tw-w-full">
            <thead>
            <tr>
              <th v-for="key in Object.keys(recommendedTeams[0])" :key="key"
                  class="tw-bg-primary tw-text-sm tw-border tw-border-primary-light tw-py-0.5 tw-px-1">
                <div class="tw-w-fit">
                  <div v-if="key === 'activeChip'">Chip</div>
                  <div v-else-if="['averageFantasyPoints', 'averageRecentPoints', 'averagePoints', 'consistency'].includes(key)">
                    {{ key.split(/(?=[A-Z])/).map((word) => word.charAt(0).toUpperCase()).join('') }}
                  </div>
                  <div v-else>{{ key.split(/(?=[A-Z])/).map((word) => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase()).join(' ') }}</div>
                </div>
              </th>
            </tr>
            </thead>
            <tbody>
            <CompactTeamDisplay v-for="team in visibleTeams" :key="team.code" :team="team" :comparative-team="comparativeTeam"/>
            </tbody>
          </table>
        </div>
        <div class="tw-flex tw-justify-between tw-items-center tw-text-f1-white">
          <ContinueButton :class="`${startIndex > 1 ? '' : 'tw-invisible'}`" @continue="previousPage">
            <span class="material-symbols-outlined tw-text-base tw-mx-1">arrow_back</span>
          </ContinueButton>
          <div class="tw-mt-0.5 tw-text-sm">
            Showing {{ startIndex + 1 }} - {{ Math.min(endIndex, recommendedTeams.length) }} of {{ recommendedTeams.length }} Recommended Teams
          </div>
          <ContinueButton :class="`${endIndex < recommendedTeams.length ? '' : 'tw-invisible'}`" @continue="nextPage">
            <span class="material-symbols-outlined tw-text-base tw-mx-1">arrow_forward</span>
          </ContinueButton>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

/* Button container */
.glow-button {
  background: linear-gradient(45deg, #ff3d3d, #e852e8, #0099ff, #3f57f1);
  background-size: 300%;
  will-change: background-position;
  box-shadow: #ff3d3d 0 0 25px -10px;
}

.glow-button:hover {
  animation: moveGradient 2s ease-in-out infinite;
}

/* Rotate gradient animation */
@keyframes moveGradient {
  0% {
    background-position: 0 50%;
    box-shadow: #ff3d3d 0 0 25px -10px;
  }
  25% {
    background-position: 50% 50%;
    box-shadow: #e852e8 0 0 25px -10px;
  }
  50% {
    background-position: 100% 50%;
    box-shadow: #0099ff 0 0 25px -10px;

  }
  75% {
    background-position: 50% 50%;
    box-shadow: #e852e8 0 0 25px -10px;
  }
  100% {
    background-position: 0 50%;
    box-shadow: #ff3d3d 0 0 25px -10px;
  }
}

.v-enter-active {
  transition: opacity .5s ease;
}

.v-enter-from {
  opacity: 0;
}

.loading {
  animation: bounce 2s ease-in-out infinite;
}

@keyframes bounce {
  0% {
    transform: translateX(-100%);
  }
  50% {
    transform: translateX(100%);
  }
  100% {
    transform: translateX(-100%);
  }
}
</style>