<script>
import CompactTeamDisplay from "@/components/display/CompactTeamDisplay.vue";
import ContinueButton from "@/components/common/ContinueButton.vue";
import LoadingBar from "@/components/common/LoadingBar.vue";
import {mapActions, mapGetters} from "vuex";

export default {
  name: 'RecommendedTeamsContainer',
  components: {ContinueButton, CompactTeamDisplay, LoadingBar},
  props: {
    comparativeTeam: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      loadingMessage: {success: false, stage: 0, maxStage: 1},
      startIndex: 0,
      range: 10, // Keep same
      endIndex: 10, // Keep same
      visibleTeams: [],
      sortOptions: ['Value', 'Average Fantasy Points', 'Average Recent Points', 'Average Points', 'Consistency', 'Transfers'],
      sortDesc: true
    }
  },
  watch: {
    recommendedTeams() {
      this.resetVisibleTeams();
    },
    comparativeTeam() {
      this.resetVisibleTeams();
    }
  },
  computed: {
    ...mapGetters(['recommendedTeams']),
  },
  methods: {
    ...mapActions(['setRecommendedTeams']),
    async loadRecommendedTeams() {
      this.$store.dispatch('setRecommendedTeams', []);
      this.loadingMessage = {
        stage: 0
      }

      const eventSource = new EventSource(`http://localhost:8081/api/teams/recommended/limit=${50}`);

      eventSource.onmessage = (event) => {
        const response = JSON.parse(event.data).body;
        this.loadingMessage = {
          title: "New Message",
          message: response.message,
          success: true,
          stage: response.stage,
          maxStage: response.maxStage,
        };
        console.log(response);
        if (response.stage === response.maxStage && response.message === "RESULT") {
          console.log('Received result: ', response.teams);
          this.$store.dispatch('setRecommendedTeams', response.teams);
          eventSource.close();
        } else {
          console.log('Received message: ', response.message);
        }
      };

      eventSource.onerror = () => {
        console.error('Error receiving updates');
        this.loadingMessage = {
          title: "Error",
          message: "Error!",
          success: false,
          stage: 1,
          maxStage: 1,
        };
        eventSource.close();
      };

      // if (this.recommendedTeams.length === 0) {
      //   console.warn('Failed to load recommended teams: ', this.loadingMessage);
      // }
    },
    changeSortOrder() {
      this.sortDesc = !this.sortDesc;
      this.resetVisibleTeams();
    },
    previousPage() {
      if (this.startIndex > 1) {
        this.startIndex -= this.range;
        this.endIndex -= this.range;
        this.updateVisibleTeams();
      }
    },
    nextPage() {
      if (this.endIndex < this.recommendedTeams.length) {
        this.startIndex += this.range;
        this.endIndex += this.range;
        this.updateVisibleTeams();
      }
    },
    calculateTransfers(team) {
      let transfers = 0;
      for (let driverId of team.drivers) {
        if (!this.comparativeTeam.drivers.includes(driverId)) {
          transfers++;
        }
      }
      for (let constructorId of team.constructors) {
        if (!this.comparativeTeam.constructors.includes(constructorId)) {
          transfers++;
        }
      }
      return transfers;
    },
    resetVisibleTeams() {
      this.startIndex = 0;
      this.endIndex = this.range;
      this.updateVisibleTeams();
    },
    updateVisibleTeams() {
      const sortOption = document.getElementById('sortOption').value;
      let newVisibleTeams = [];
      switch (sortOption) {
        case 'Value':
          newVisibleTeams = Array.from(this.recommendedTeams).sort((a, b) => b.value - a.value);
          break;
        case 'Average Fantasy Points':
          newVisibleTeams = Array.from(this.recommendedTeams).sort((a, b) => b.averageFantasyPoints - a.averageFantasyPoints);
          break;
        case 'Average Recent Points':
          newVisibleTeams = Array.from(this.recommendedTeams).sort((a, b) => b.averageRecentPoints - a.averageRecentPoints);
          break;
        case 'Average Points':
          newVisibleTeams = Array.from(this.recommendedTeams).sort((a, b) => b.averagePoints - a.averagePoints);
          break;
        case 'Consistency':
          newVisibleTeams = Array.from(this.recommendedTeams).sort((a, b) => b.consistency - a.consistency);
          break;
        case 'Transfers':
          newVisibleTeams = Array.from(this.recommendedTeams).sort((a, b) => this.calculateTransfers(b) - this.calculateTransfers(a));
          break;
        default:
          console.warn('Invalid sort option: ', sortOption);
          newVisibleTeams = Array.from(this.recommendedTeams);
          break;
      }
      newVisibleTeams = this.sortDesc ? newVisibleTeams : newVisibleTeams.reverse();
      this.visibleTeams = newVisibleTeams.slice(this.startIndex, this.endIndex);
    },
  }
}
</script>

<template>
  <div
      class="tw-w-full tw-h-full tw-flex tw-flex-col tw-bg-gradient-to-bl tw-to-f1-red tw-from-primary-dark tw-to-200% tw-outline tw-outline-1 tw-outline-primary-light -tw-outline-offset-1 tw-rounded-lg">
    <div class="tw-flex tw-gap-2 tw-border-b-1 tw-border-primary-light tw-p-2 tw-text-f1-white">
      <div class="tw-font-bold tw-mt-0.5 tw-text-xl tw-mr-auto">
        Recommended Fantasy Teams
      </div>
      <div class="tw-flex tw-items-center tw-gap-2">
        Sort by
        <select id="sortOption" @change="resetVisibleTeams"
                class="tw-px-2 tw-appearance-none tw-border-1 tw-border-primary-light tw-bg-primary-light tw-bg-opacity-5 tw-text-f1-white tw-text-center">
          <option :value="sortOption" v-for="sortOption in sortOptions" :key="sortOption.code">
            {{ sortOption }}
          </option>
        </select>
        <div
            class="tw-flex tw-items-center tw-px-1 tw-border-1 tw-border-primary-light tw-rounded tw-bg-primary-light tw-bg-opacity-5 tw-text-f1-white tw-cursor-pointer"
            @click="changeSortOrder()" id="sortOrder" :title="`Sorted in ${sortDesc ? 'descending' : 'ascending'} order`">
          <span class="material-symbols-outlined tw-text-base">{{ sortDesc ? 'south_east' : 'north_east' }}</span>
        </div>
        <ContinueButton @continue="loadRecommendedTeams"
                        class="tw-ml-2">
          <span class="material-symbols-outlined tw-mx-1 tw-text-base">refresh</span>
        </ContinueButton>
      </div>
    </div>
    <div class="tw-overflow-hidden tw-h-full tw-bg-primary-dark tw-rounded-b-lg tw-text-f1-white">
      <LoadingBar v-if="recommendedTeams.length === 0" :success="loadingMessage.success" :max-stage="loadingMessage.maxStage" :stage="loadingMessage.stage"/>
      <div v-if="recommendedTeams.length === 0 && loadingMessage.stage > 0" class="tw-text-xl tw-mt-12">
        Loading... {{ loadingMessage.message }}
      </div>
      <div class="tw-h-full tw-flex tw-flex-col tw-px-1" v-if="recommendedTeams.length > 0 && comparativeTeam">
        <div class="tw-flex tw-gap-2 tw-border-l-1 tw-border-l-primary-dark tw-py-1 tw-font-bold">
          <div class="tw-w-1/12 tw-border-r-1 tw-border-primary-light">Value</div>
          <div class="hover-parent tw-relative tw-w-5/12">
            <div class="not-hover-child tw-w-full tw-absolute tw-transition-opacity">Statistics</div>
            <div class="hover-child tw-absolute tw-w-full tw-flex tw-justify-between tw-tw-transition-opacity">
              <div class="tw-w-9" title="Applied Chip?"><span class="material-symbols-outlined">poker_chip</span></div>
              <div class="tw-w-1/5" title="Average Fantasy Points Per Grand Prix"><span class="material-symbols-outlined">joystick</span></div>
              <div class="tw-w-1/5" title="Average Recent Points Per Grand Prix"><span class="material-symbols-outlined">timer</span></div>
              <div class="tw-w-1/5" title="Average Points Per Grand Prix"><span class="material-symbols-outlined">trophy</span></div>
              <div class="tw-w-1/5" title="Consistency"><span class="material-symbols-outlined">tools_level</span></div>
            </div>
          </div>
          <div class="tw-flex tw-w-6/12 tw-gap-1">
            <div class="tw-w-7/12 tw-border-l-1 tw-border-primary-light">Drivers</div>
            <div class="tw-w-4/12 tw-border-x-1 tw-border-primary-light">Constructors</div>
            <div class="tw-w-1/12">Diff</div>
          </div>
        </div>
        <div class="tw-h-full tw-flex tw-flex-col tw-gap-2">
          <CompactTeamDisplay v-for="team in visibleTeams" :key="team.code" :team="team" :comparative-team="comparativeTeam"/>
        </div>
        <div class="tw-flex tw-justify-between tw-items-center tw-py-1 tw-px-1 tw-text-f1-white">
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
.hide-scrollbar {
  /*FireFox*/
  scrollbar-width: none;
  /*IE10+*/
  -ms-overflow-style: -ms-autohiding-scrollbar;
}

.hide-scrollbar::-webkit-scrollbar {
  /*Chrome, Safari, Edge*/
  display: none;
}
</style>