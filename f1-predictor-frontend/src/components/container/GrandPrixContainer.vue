<script>
import GrandPrixDisplay from "@/components/display/GrandPrixDisplay.vue";
import {mapGetters} from "vuex";

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
    nextGrandPrix() {
      const newIndex = this.currentIndex + 1;
      this.currentIndex = newIndex > 0 ? newIndex % this.allGrandsPrix.length : this.allGrandsPrix.length - 1;
      this.scrollToGrandPrix(this.currentIndex);
    }
  },
  components: {
    GrandPrixDisplay,
  }
}
</script>

<template>
  <div v-if="allGrandsPrix" @click="nextGrandPrix()"
       class="tw-cursor-pointer tw-relative tw-w-full tw-h-64 tw-py-2 tw-border-2 tw-border-primary-light tw-bg-primary-dark tw-rounded tw-overflow-hidden">
    <div class="tw-w-full tw-h-full tw-overflow-hidden tw-relative">
      <div
          class="tw-w-fit tw-px-full tw-h-full tw-flex tw-flex-row tw-gap-2 tw-overflow-hidden tw-scroll-smooth">
        <GrandPrixDisplay class="tw-snap-center tw-snap-always" v-for="(grandPrix, index) in allGrandsPrix"
                          :id="'grand-prix-' + index" :key="grandPrix.code" :grand-prix="grandPrix"
                          :index="index" :currentIndex="currentIndex"/>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>