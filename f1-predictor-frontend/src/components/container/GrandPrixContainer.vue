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
        this.currentIndex = this.startIndex;
        this.scrollToGrandPrix(this.currentIndex, false);
      }
    },
  },
  computed: {
    ...mapGetters(['allGrandsPrix']),
  },
  methods: {
    scrollToGrandPrix(newIndex, smooth = true) {
      const id = newIndex === this.allGrandsPrix.length ? 'grand-prix-NEW' : `grand-prix-${newIndex}`;
      const element = document.getElementById(id);
      if (element) {
        element.scrollIntoView({
          behavior: smooth ? 'smooth' : 'auto',
          block: 'nearest',
          inline: 'center',
        });
        if (newIndex !== this.allGrandsPrix.length) {
          this.$emit('updateGrandPrix', this.allGrandsPrix[newIndex]);
        }
      }
    },
    switchGrandPrix(newIndex) {
      this.currentIndex = newIndex >= 0 ? newIndex % (this.allGrandsPrix.length + 1) : this.allGrandsPrix.length;
      this.scrollToGrandPrix(this.currentIndex);
    },
    addGrandPrix() {
      this.$emit('addObject', this.allGrandsPrix);
    },
    graph(gp) {
      this.$emit('showGraph', gp);
    },
  },
  components: {
    ContinueButton,
    GrandPrixDisplay,
  }
}
</script>

<template>
  <div v-if="allGrandsPrix"
       class="tw-relative tw-w-2/3 tw-h-64 tw-border-1 tw-border-primary-light tw-bg-f1-black tw-rounded-lg tw-overflow-hidden">
    <div class="tw-w-full tw-h-full tw-py-2 tw-overflow-hidden tw-relative">
      <div class="tw-w-fit tw-px-full tw-h-full tw-flex tw-flex-row tw-overflow-hidden tw-scroll-smooth">
        <GrandPrixDisplay class="tw-snap-center tw-snap-always tw-mr-2"
                          @graph="graph"
                          v-for="(grandPrix, index) in allGrandsPrix"
                          :key="grandPrix.code" :grand-prix="grandPrix"
                          :index="index" :currentIndex="currentIndex"/>
        <ContinueButton id="grand-prix-NEW" @continue="addGrandPrix" class="tw-w-96 tw-h-full"
                        :class="[currentIndex === allGrandsPrix.length ? '' : 'tw-opacity-50 tw-pointer-events-none']">
          <span class="material-symbols-outlined tw-text-6xl tw-font-light">add</span>
        </ContinueButton>
      </div>
    </div>
    <div class="tw-absolute tw-flex tw-justify-center tw-flex-col tw-left-2 tw-top-0 tw-h-full tw-text-f1-white">
      <ContinueButton @continue="switchGrandPrix(this.currentIndex - 1)" class="tw-rounded-full tw-h-32 tw-transition-all tw-w-8 tw-backdrop-blur-xl">
        <span class="material-symbols-outlined tw-text-3xl tw-font-medium">keyboard_arrow_left</span>
      </ContinueButton>
    </div>
    <div class="tw-absolute tw-flex tw-justify-center tw-flex-col tw-right-2 tw-top-0 tw-h-full tw-gap-0 tw-text-f1-white">
      <ContinueButton @continue="$emit('editArray', this.allGrandsPrix)" class="tw-rounded-t-full tw-border-b-0 tw-w-8 tw-h-8 tw-backdrop-blur-xl">
        <span class="tw-transition-opacity material-symbols-outlined tw-text-xl tw-mt-1 tw-leading-none">edit_note</span>
      </ContinueButton>
      <ContinueButton @continue="switchGrandPrix(this.currentIndex + 1)" class="tw-rounded-b-full tw-h-24 tw-w-8 tw-backdrop-blur-xl">
        <span class="material-symbols-outlined tw-text-3xl tw-font-medium tw-mb-2">keyboard_arrow_right</span>
      </ContinueButton>
    </div>
  </div>
</template>

<style scoped>
</style>