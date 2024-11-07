<script>
import GrandPrixDisplay from "@/components/display/GrandPrixDisplay.vue";
import {mapGetters} from "vuex";
import ContinueButton from "@/components/common/ContinueButton.vue";

export default {
  name: 'GrandPrixContainer',
  props: {
    startIndex: {
      default: 0,
      type: Number,
      required: true
    }
  },
  data() {
    return {
      currentIndex: this.startIndex,
    }
  },
  watch: {
    startIndex: {
      immediate: true,
      handler() {
        console.log('Start index changed to', this.startIndex);
        this.currentIndex = this.startIndex;
        // This is not working as element is not yet rendered
        this.scrollToGrandPrix(this.currentIndex);
      }
    },
  },
  computed: {
    ...mapGetters(['allGrandsPrix']),
  },
  methods: {
    scrollToGrandPrix(newIndex) {
      const element = document.getElementById(`grand-prix-${newIndex}`);
      console.log('Scrolling to', element, 'at index', newIndex);
      if (element) {
        element.scrollIntoView({
          behavior: 'smooth',
          block: 'nearest',
          inline: 'center',
        });
      }
    },
    switchGrandPrix(newIndex) {
      this.currentIndex = newIndex >= 0 ? newIndex % this.allGrandsPrix.length : this.allGrandsPrix.length - 1;
      this.scrollToGrandPrix(this.currentIndex);
    }
  },
  components: {
    ContinueButton,
    GrandPrixDisplay,
  }
}
</script>

<template>
  <div v-if="allGrandsPrix"
       class="tw-relative tw-w-full tw-h-64 tw-py-2 tw-border-2 tw-border-primary-light tw-bg-f1-black tw-rounded-xl tw-overflow-hidden">
    <div class="tw-w-full tw-h-full tw-py-2 tw-overflow-hidden tw-relative">
      <div
          class="tw-w-fit tw-px-full tw-h-full tw-flex tw-flex-row tw-gap-2 tw-overflow-hidden tw-scroll-smooth">
        <GrandPrixDisplay class="tw-snap-center tw-snap-always" v-for="(grandPrix, index) in allGrandsPrix"
                          :id="'grand-prix-' + index" :key="grandPrix.code" :grand-prix="grandPrix"
                          :index="index" :currentIndex="currentIndex"/>
      </div>
    </div>
    <div
        class="tw-absolute tw-flex tw-justify-center tw-flex-col tw-left-0 tw-top-0 tw-h-full tw-ml-2 tw-text-f1-white">
      <ContinueButton @continue="switchGrandPrix(this.currentIndex - 1)" class="tw-h-full tw-my-2 tw-backdrop-blur-xl">
        <span class="material-icons">keyboard_arrow_left</span>
      </ContinueButton>
    </div>
    <div
        class="tw-absolute tw-flex tw-justify-center tw-flex-col tw-right-0 tw-top-0 tw-h-full tw-mr-2 tw-text-f1-white">
      <ContinueButton @continue="switchGrandPrix(this.currentIndex + 1)" class="tw-h-full tw-my-2 tw-backdrop-blur-xl">
        <span class="material-icons">keyboard_arrow_right</span>
      </ContinueButton>
    </div>
  </div>
</template>

<style scoped>

</style>