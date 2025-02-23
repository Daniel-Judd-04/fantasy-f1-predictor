<template>
  <div id="app"
       class="tw-h-screen tw-w-screen tw-bg-f1-black tw-flex tw-flex-row tw-justify-between tw-gap-2 tw-p-1 tw-pt-2 tw-select-none tw-text-f1-white tw-border-primary-light tw-outline-primary-light">
    <ConstructorContainer v-if="true" @showGraph="showGraph" @editObject="editObject" @editArray="editArray"/>
    <div class="tw-w-full tw-h-full tw-flex tw-flex-col tw-gap-2 tw-overflow-hidden">
      <div class="tw-h-64 tw-flex tw-flex-row tw-gap-2">
        <GrandPrixContainer @showGraph="showGraph" @editArray="editArray" @addObject="addObject" @updateGrandPrix="updateGrandPrix" :start-index="grandPrixStartIndex"/>
        <TeamDisplay @editArray="editArray" @updateComparativeTeam="updateComparativeTeam" :user-teams="userTeams"/>
      </div>
      <RecommendedTeamsContainer :comparative-team="currentTeam" :grand-prix="grandPrix"/>
    </div>
    <DriverContainer v-if="true" @showGraph="showGraph" @editObject="editObject" @editArray="editArray"/>
  </div>
  <OverlayContainer @exit="closeOverlay()" v-if="showOverlay" :overlay-data="overlayData"/>
  <div :style="{ height: `${loadingMessage.stage !== loadingMessage.maxStage ? '100' : '0'}vh` }"
       class="tw-w-screen tw-overflow-hidden tw-transition-all tw-absolute tw-flex tw-items-center tw-justify-center tw-bg-f1-black tw-top-0 tw-text-f1-white">
    <div>
      <div class="tw-font-light tw-text-2xl tw-flex tw-flex-col tw-gap-2 tw-items-center">
        <span v-if="loadingMessage.success" class="material-symbols-outlined tw-text-5xl">verified_user</span>
        <span v-else class="material-symbols-outlined tw-text-red-700 tw-text-5xl">gpp_maybe</span>
        <div class="tw-mt-0.5">{{ loadingMessage.title }}</div>
      </div>
      <div class="tw-font-extralight tw-text-lg tw-text-primary-light">{{ loadingMessage.message }}</div>
    </div>
    <LoadingBar :success="loadingMessage.success" :max-stage="loadingMessage.maxStage" :stage="loadingMessage.stage"/>
  </div>


</template>

<script>
import DriverContainer from "@/components/container/DriverContainer.vue";
import {mapGetters} from "vuex";
import ConstructorContainer from "@/components/container/ConstructorContainer.vue";
import GrandPrixContainer from "@/components/container/GrandPrixContainer.vue";
import OverlayContainer from "@/components/overlay/OverlayContainer.vue";
import TeamDisplay from "@/components/display/TeamDisplay.vue";
import RecommendedTeamsContainer from "@/components/container/RecommendedTeamsContainer.vue";
import LoadingBar from "@/components/common/LoadingBar.vue";
import {LoadingMessage, OverlayData} from "@/utils/classes";
import {isGrandPrix} from "@/utils/common";

export default {
  name: 'App',
  async created() {
    this.loadingMessage = await this.$store.dispatch('fetchDrivers');
    if (this.allDrivers.length === 0) return;
    this.loadingMessage = await this.$store.dispatch('fetchConstructors');
    if (this.allConstructors.length === 0) return;
    this.loadingMessage = await this.$store.dispatch('fetchCircuits');
    if (this.allCircuits.length === 0) return;
    this.loadingMessage = await this.$store.dispatch('fetchGrandsPrix');
    if (this.allGrandsPrix.length === 0) return;
    this.loadingMessage = await this.$store.dispatch('fetchUserTeams');
    if (this.userTeams.length === 0) return;
    console.log('All Data Loaded Successfully!');
  },
  data() {
    return {
      grandPrixStartIndex: -1,
      overlayData: {
        overlayArray: [],
        overlayObject: {},
        overlayType: ''
      },
      showOverlay: false,
      loadingMessage: new LoadingMessage(0, 5, 'Loading', 'Loading Data..', true),
      currentTeam: {},
      grandPrix: {}
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
    showGraph(target) {
      this.overlayData = new OverlayData(target, [], isGrandPrix(target) ? 'ShowGrandPrixResults' : 'ShowGraph');

      this.showOverlay = true;
    },
    addObject(array) {
      this.overlayData = new OverlayData(null, array, 'AddObject');

      this.showOverlay = true;
    },
    editObject(target, array) {
      this.overlayData = new OverlayData(target, array, 'EditObject');

      this.showOverlay = true;
    },
    editArray(array) {
      this.overlayData = new OverlayData(null, array, 'EditArray');

      this.showOverlay = true;
    },
    closeOverlay() {
      this.showOverlay = false;

      this.overlayData = new OverlayData(null, [], '');
    },
    updateComparativeTeam(comparativeTeam) {
      this.currentTeam = comparativeTeam;
    },
    updateGrandPrix(grandPrix) {
      this.grandPrix = grandPrix;
    }
  },
  computed: {
    ...mapGetters(['allDrivers', 'allConstructors', 'allCircuits', 'allGrandsPrix', 'userTeams']),
  },
  components: {
    LoadingBar,
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
  font-family: Montserrat, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
}

input:focus, textarea:focus, select:focus {
  outline: none;
}

input[type="number"]::-webkit-inner-spin-button,
input[type="number"]::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

input[type="datetime-local"] {
  text-align: right;
  -webkit-appearance: none; /* For WebKit-based browsers (Chrome, Safari, Edge) */
  appearance: none; /* For modern browsers */
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
