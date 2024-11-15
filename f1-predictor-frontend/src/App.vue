<template>
  <div id="app" class="tw-h-screen tw-w-screen tw-bg-f1-black tw-flex tw-flex-row tw-justify-between tw-gap-2 tw-p-1 tw-select-none">
    <ConstructorContainer v-if="true" @showGraph="showGraph" @editObject="editObject" @editArray="editArray"/>
    <div class="tw-w-full tw-h-full tw-flex tw-flex-col tw-gap-2 tw-overflow-hidden">
      <div class="tw-h-8 tw-text-xl tw-font-extrabold tw-text-f1-white">
        Fantasy F1 Predictor v2
      </div>
      <div class="tw-h-64 tw-flex tw-flex-row tw-gap-2">
        <GrandPrixContainer :start-index="grandPrixStartIndex"/>
        <TeamDisplay :team="currentTeam"/>
      </div>
      <RecommendedTeamsContainer @updateComparativeTeam="updateComparativeTeam" :recommended-teams="userTeams" :user-teams="userTeams" :comparative-team="currentTeam"/>
    </div>
    <DriverContainer v-if="true" @showGraph="showGraph" @editObject="editObject" @editArray="editArray"/>
  </div>
  <OverlayContainer @exit="closeOverlay()" v-if="showOverlay" :overlay-object="overlayObject"
                    :overlay-array="overlayArray" :start-index="overlayIndex" :overlay-type="overlayType"/>
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
    await this.fetchDrivers();
    await this.fetchConstructors();
    await this.fetchCircuits();
    await this.fetchGrandsPrix();
    await this.fetchUserTeams();
  },
  data() {
    return {
      grandPrixStartIndex: -1,
      overlayArray: [],
      overlayIndex: 0,
      overlayType: '',
      overlayObject: {},
      showOverlay: false,
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
    ...mapActions(['fetchDrivers', 'fetchConstructors', 'fetchCircuits', 'fetchGrandsPrix', 'fetchUserTeams']),
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
    ...mapGetters(['allGrandsPrix', 'userTeams']),
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
