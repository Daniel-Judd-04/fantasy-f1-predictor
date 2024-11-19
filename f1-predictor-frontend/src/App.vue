<template>
  <div id="app"
       class="tw-h-screen tw-w-screen tw-bg-f1-black tw-flex tw-flex-row tw-justify-between tw-gap-2 tw-p-1 tw-select-none tw-text-f1-white tw-border-primary-light tw-outline-primary-light">
    <ConstructorContainer v-if="true" @showGraph="showGraph" @editObject="editObject" @editArray="editArray"/>
    <div class="tw-w-full tw-h-full tw-flex tw-flex-col tw-gap-2 tw-overflow-hidden">
      <div class="tw-h-8 tw-text-xl tw-font-extrabold tw-text-f1-white">
        Fantasy F1 Predictor v2
      </div>
      <div class="tw-h-64 tw-flex tw-flex-row tw-gap-2">
        <GrandPrixContainer :start-index="grandPrixStartIndex"/>
        <TeamDisplay @updateComparativeTeam="updateComparativeTeam" :user-teams="userTeams"/>
      </div>
      <RecommendedTeamsContainer :recommended-teams="recommendedTeams" :comparative-team="currentTeam"/>
    </div>
    <DriverContainer v-if="true" @showGraph="showGraph" @editObject="editObject" @editArray="editArray"/>
  </div>
  <OverlayContainer @exit="closeOverlay()" v-if="showOverlay" :overlay-object="overlayObject"
                    :overlay-array="overlayArray" :start-index="overlayIndex" :overlay-type="overlayType"/>
  <div :style="{ height: `${appLoading ? '100' : '0'}vh` }"
       class="tw-w-screen tw-overflow-hidden tw-transition-all tw-absolute tw-flex tw-items-center tw-justify-center tw-bg-f1-black tw-top-0 tw-text-f1-white">
    <div>
      <div class="tw-font-light tw-text-2xl tw-flex tw-flex-col tw-gap-2 tw-items-center">
        <span v-if="loadingMessage.success" class="material-symbols-outlined tw-text-5xl">verified_user</span>
        <span v-else class="material-symbols-outlined tw-text-red-700 tw-text-5xl">gpp_maybe</span>
        <div class="tw-mt-0.5">{{ loadingMessage.title }}</div>
      </div>
      <div class="tw-font-extralight tw-text-lg tw-text-primary-light">{{ loadingMessage.message }}</div>
    </div>
    <div :style="{ width: `${(loadingMessage.stage / loadingMessage.maxStage) * 90}%` }"
         :class="`${loadingMessage.success ? 'tw-from-primary-light' : 'tw-from-red-600'}`"
         class="tw-transition-all tw-top-0 tw-left-0 tw-absolute tw-z-0 tw-h-1 tw-bg-gradient-to-l tw-to-primary-dark tw-rounded-r-full">
      <span></span>
    </div>
  </div>


</template>

<script>
import DriverContainer from "@/components/container/DriverContainer.vue";
import {mapActions, mapGetters} from "vuex";
import ConstructorContainer from "@/components/container/ConstructorContainer.vue";
import GrandPrixContainer from "@/components/container/GrandPrixContainer.vue";
import OverlayContainer from "@/components/overlay/OverlayContainer.vue";
import TeamDisplay from "@/components/display/TeamDisplay.vue";
import RecommendedTeamsContainer from "@/components/container/RecommendedTeamsContainer.vue";

export default {
  name: 'App',
  async created() {
    this.loadingMessage = await this.fetchDrivers();
    if (this.allDrivers.length === 0) return;
    this.loadingMessage = await this.fetchConstructors();
    if (this.allConstructors.length === 0) return;
    this.loadingMessage = await this.fetchCircuits();
    if (this.allCircuits.length === 0) return;
    this.loadingMessage = await this.fetchGrandsPrix();
    if (this.allGrandsPrix.length === 0) return;
    this.loadingMessage = await this.fetchUserTeams();
    if (this.userTeams.length === 0) return;
    this.loadingMessage = await this.fetchRecommendedTeams();
    if (this.recommendedTeams.length === 0) return;

    this.appLoading = false;
  },
  data() {
    return {
      grandPrixStartIndex: -1,
      overlayArray: [],
      overlayIndex: 0,
      overlayType: '',
      overlayObject: {},
      showOverlay: false,
      appLoading: true,
      loadingMessage: {title: 'Loading', message: 'Loading Data...', success: true, stage: 0},
      currentTeam: {},
    }
  },
  watch: {
    allGrandsPrix() {
      this.$nextTick(() => {
        for (let i = 0; i < this.allGrandsPrix.length; i++) {
          if (new Date(this.allGrandsPrix[i].startTime) > new Date()) {
            this.grandPrixStartIndex = i;
            break;
          }
        }
        if (this.grandPrixStartIndex === -1) {
          this.grandPrixStartIndex = this.allGrandsPrix.length - 1;
        }
      });
    },
    userTeams() {
      this.currentTeam = this.userTeams[0];
    }
  },
  methods: {
    ...mapActions(['fetchDrivers', 'fetchConstructors', 'fetchCircuits', 'fetchGrandsPrix', 'fetchUserTeams', 'fetchRecommendedTeams']),
    showGraph(target) {
      this.overlayObject = target;
      this.overlayType = 'ShowGraph';

      this.showOverlay = true;
    },
    editObject(target, array) {
      for (let i = 0; i < array.length; i++) {
        if (array[i] === target) {
          this.overlayIndex = i;
          break;
        }
      }
      this.overlayArray = array;
      this.overlayType = 'EditObject';

      this.showOverlay = true;
    },
    editArray(array) {
      this.overlayArray = array;
      this.overlayType = 'EditArray';

      this.showOverlay = true;
    },
    closeOverlay() {
      this.showOverlay = false;

      this.overlayArray = [];
      this.overlayObject = {};
      this.overlayIndex = 0;
      this.overlayType = '';
    },
    updateComparativeTeam(comparativeTeam) {
      this.currentTeam = comparativeTeam;
    }
  },
  computed: {
    ...mapGetters(['allDrivers', 'allConstructors', 'allCircuits', 'allGrandsPrix', 'userTeams', 'recommendedTeams']),
  },
  components: {
    RecommendedTeamsContainer,
    TeamDisplay,
    OverlayContainer,
    GrandPrixContainer,
    DriverContainer,
    ConstructorContainer,
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
}

.hover-parent:hover .hover-child {
  opacity: 1;
}

.hover-parent:hover .not-hover-child {
  opacity: 0;
}

.hover-child {
  opacity: 0;
}

.flag-frame {
  aspect-ratio: 4 / 3;
}

.flag-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>

<style scoped>

</style>
