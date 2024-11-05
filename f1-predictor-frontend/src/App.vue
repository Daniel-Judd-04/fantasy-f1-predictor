<template>
  <div id="app" class="tw-h-screen tw-w-screen tw-bg-f1-black tw-flex tw-flex-row tw-justify-between tw-gap-2 tw-p-2">
    <ConstructorContainer @edit="editProperty"/>
    <div class="tw-w-full tw-h-full tw-flex tw-flex-col tw-gap-2 tw-overflow-hidden">
      <div class="tw-text-3xl tw-font-extrabold tw-text-f1-white">
        F1 Fantasy Predictor v2
      </div>
      <div class="tw-h-64 tw-flex tw-flex-row tw-gap-2">
        <GrandPrixContainer :start-index="nextRaceIndex"/>
        <div class="tw-min-w-1/3 tw-w-1/3 tw-bg-emerald-700">
          <div class="tw-w-96">

          </div>
        </div>
      </div>
      <div class="tw-w-full tw-h-full tw-bg-fuchsia-800">

      </div>
    </div>

    <DriverContainer @edit="editProperty"/>
  </div>
  <OverlayContainer @exit="showOverlay = false" v-if="showOverlay" :object-array="overlayArray"
                    :start-index="overlayIndex">
    <template v-slot:title>
      Edit Property
    </template>
  </OverlayContainer>
</template>

<script>
import DriverContainer from "@/components/container/DriverContainer.vue";
import {mapActions, mapGetters} from "vuex";
import ConstructorContainer from "@/components/container/ConstructorContainer.vue";
import GrandPrixContainer from "@/components/container/GrandPrixContainer.vue";
import OverlayContainer from "@/components/container/OverlayContainer.vue";

export default {
  name: 'App',
  created() {
    this.fetchDrivers();
    this.fetchConstructors();
    this.fetchCircuits();
    this.fetchGrandsPrix();
  },
  data() {
    return {
      nextRaceIndex: 0,
      overlayArray: [],
      overlayIndex: 0,
      showOverlay: false,
    }
  },
  watch: {
    allGrandsPrix() {
      this.$nextTick(() => {
        for (let i = 0; i < this.allGrandsPrix.length; i++) {
          if (new Date(this.allGrandsPrix[i].startTime) > new Date()) {
            this.nextRaceIndex = i;
            break;
          }
        }
      });
    }
  },
  methods: {
    ...mapActions(['fetchDrivers', 'fetchConstructors', 'fetchCircuits', 'fetchGrandsPrix']),
    editProperty(target, array) {
      for (let i = 0; i < array.length; i++) {
        if (array[i] === target) {
          this.overlayIndex = i;
          break;
        }
      }
      this.overlayArray = array;
      this.showOverlay = true;
    },
  },
  computed: {
    ...mapGetters(['allGrandsPrix']),
  },
  components: {
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
</style>

<style scoped>

</style>
