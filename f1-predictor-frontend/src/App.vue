<template>
  <div id="app" class="tw-h-screen tw-w-screen tw-bg-f1-black tw-flex tw-flex-row tw-gap-2 tw-p-2">
    <ConstructorContainer/>
    <!--    <div v-if="allGrandsPrix" @click="scrollToGrandPrix(currentIndex)"-->
    <!--         class="tw-overflow-hidden tw-relative tw-w-full tw-h-64 tw-border-2 tw-cursor-e-resize tw-border-primary-light tw-rounded tw-py-2 ">-->
    <!--      <div class="tw-w-full tw-h-full tw-overflow-hidden tw-scroll-smooth tw-snap-x tw-snap-mandatory">-->
    <!--        <div class="tw-w-fit tw-h-full tw-flex tw-flex-row tw-px-2 tw-gap-2">-->
    <!--          <GrandPrixDisplay class="tw-snap-center tw-snap-always" v-for="(grandPrix, index) in allGrandsPrix"-->
    <!--                            :id="'grand-prix-' + index"-->
    <!--                            :key="grandPrix.code"-->
    <!--                            :grand-prix="grandPrix"/>-->
    <!--        </div>-->
    <!--      </div>-->
    <!--      <div class="tw-sticky -tw-left-4 -tw-top-64 tw-bg-emerald-600 tw-h-full tw-w-5">-->
    <!--      </div>-->
    <!--    </div>-->
    <div v-if="allGrandsPrix" @click="scrollToGrandPrix(currentIndex)"
         class="tw-relative tw-w-full tw-h-64 tw-py-2 tw-border-2 tw-border-primary-light tw-rounded tw-overflow-hidden main-container-with-shadow">
      <!-- Gradient shadow effect on the left and right -->
      <div class="tw-w-full tw-h-full tw-overflow-hidden tw-relative">
        <!-- Scrollable content -->
        <div
            class="tw-w-fit tw-h-full tw-flex tw-flex-row tw-px-2 tw-gap-2 tw-overflow-scroll tw-scroll-smooth inner-scrollable-container">
          <GrandPrixDisplay class="tw-snap-center tw-snap-always" v-for="(grandPrix, index) in allGrandsPrix"
                            :id="'grand-prix-' + index"
                            :key="grandPrix.code"
                            :grand-prix="grandPrix"/>
        </div>
      </div>
      <!-- Left and right shadow elements outside the scrollable area -->
      <div
          class="tw-absolute tw-top-0 -tw-left-8 tw-w-16 tw-h-screen tw-blur-xl tw-bg-f1-black tw-pointer-events-none tw-z-10"></div>
      <div
          class="tw-absolute tw-top-0 -tw-right-8 tw-w-16 tw-h-screen tw-blur-xl tw-bg-f1-black tw-pointer-events-none tw-z-10"></div>

      <!--      .right-blur {
        position: absolute;
        top: 0;
        right: -50px;
        width: 100px; /* Adjust width as needed */
        height: 200%;
        background: aqua;
        filter: blur(50px);
        pointer-events: none;
        z-index: 10;
      }-->
    </div>

    <DriverContainer/>
  </div>
</template>

<script>
import DriverContainer from "@/components/DriverContainer.vue";
import {mapActions, mapGetters} from "vuex";
import ConstructorContainer from "@/components/ConstructorContainer.vue";
import GrandPrixDisplay from "@/components/GrandPrixDisplay.vue";

export default {
  name: 'App',
  data() {
    return {
      currentIndex: 3,
    }
  },
  created() {
    this.fetchDrivers();
    this.fetchConstructors();
    this.fetchCircuits();
    this.fetchGrandsPrix();
  },
  methods: {
    ...mapActions(['fetchDrivers', 'fetchConstructors', 'fetchCircuits', 'fetchGrandsPrix']),
    scrollToGrandPrix(newIndex) {
      const element = document.getElementById(`grand-prix-${newIndex}`);
      if (element && newIndex < element.parentElement.childElementCount - 1) {
        console.log(element, newIndex, element.parentElement.childElementCount);
        element.scrollIntoView({
          behavior: 'smooth', // Smooth scrolling effect
          block: 'nearest',   // Aligns the element in the nearest way
          inline: 'center',    // Aligns horizontally with center of container
        });
        this.currentIndex++;
      }
    },
  },
  computed: {
    ...mapGetters(['allCircuits', 'allGrandsPrix']),
  },
  components: {
    GrandPrixDisplay,
    DriverContainer,
    ConstructorContainer,
  },
  mounted() {
    this.scrollToGrandPrix(this.currentIndex);
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
.main-container-with-shadow {
  position: relative;
  overflow: hidden;
}

.inner-scrollable-container {
  overflow-x: scroll;
  /* Hide vertical scrollbar if not needed */
  overflow-y: hidden;
  white-space: nowrap; /* Ensure content stays in one row */
}

/* Fixed left blur */
.left-blur {
  position: absolute;
  top: 0;
  left: 0;
  width: 30px; /* Adjust width as needed */
  height: 100%;
  background: linear-gradient(to right, rgba(255, 255, 255, 1), rgba(255, 255, 255, 0));
  pointer-events: none;
  z-index: 10;
}

/* Fixed right blur */
.right-blur {
  position: absolute;
  top: 0;
  right: -50px;
  width: 100px; /* Adjust width as needed */
  height: 200%;
  background: aqua;
  filter: blur(50px);
  pointer-events: none;
  z-index: 10;
}

</style>
