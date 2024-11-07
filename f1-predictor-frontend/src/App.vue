<template>
  <div id="app"
       class="tw-h-screen tw-w-screen tw-bg-f1-black tw-flex tw-flex-row tw-justify-between tw-gap-2 tw-p-1 tw-select-none">
    <ConstructorContainer @editObject="editObject" @editArray="editArray"/>
    <div class="tw-w-full tw-h-full tw-flex tw-flex-col tw-gap-2 tw-overflow-hidden">
      <div class="tw-h-10 tw-text-3xl tw-font-extrabold tw-text-f1-white">
        Fantasy F1 Predictor v2
      </div>
      <div class="tw-h-64 tw-flex tw-flex-row tw-gap-2">
        <GrandPrixContainer :start-index="nextRaceIndex"/>
        <div class="tw-min-w-1/3 tw-w-1/3 tw-bg-primary-dark tw-border-primary-light tw-border-2 tw-rounded-xl">
          <div class="tw-w-96">

          </div>
        </div>
      </div>
      <div class="tw-w-full tw-h-full tw-bg-primary-dark tw-border-primary-light tw-border-2 tw-rounded-xl">
      </div>
    </div>

    <DriverContainer @edit="editObject"/>
  </div>
  <OverlayContainer @exit="showOverlay = false" v-if="showOverlay"
                    :object-array="overlayArray" :start-index="overlayIndex"/>
</template>

<script>
import DriverContainer from "@/components/container/DriverContainer.vue";
import {mapActions, mapGetters} from "vuex";
import ConstructorContainer from "@/components/container/ConstructorContainer.vue";
import GrandPrixContainer from "@/components/container/GrandPrixContainer.vue";
import OverlayContainer from "@/components/overlay/OverlayContainer.vue";

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
    editObject(target, array) {
      for (let i = 0; i < array.length; i++) {
        if (array[i] === target) {
          this.overlayIndex = i;
          break;
        }
      }
      this.overlayArray = array;
      this.showOverlay = true;
    },
    editArray(array) {
      this.overlayIndex = -1;
      this.overlayArray = array;
      this.showOverlay = true;
    }
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
