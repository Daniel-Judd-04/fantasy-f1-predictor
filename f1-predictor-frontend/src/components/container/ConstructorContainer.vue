<script>

import ConstructorDisplay from "@/components/display/ConstructorDisplay.vue";
import {getFlagURL} from "@/utils/common";
import {mapGetters} from "vuex";
import ContinueButton from "@/components/common/ContinueButton.vue";

export default {
  name: 'ConstructorContainer',
  components: {ContinueButton, ConstructorDisplay},
  data() {
    return {
      collapsed: false,
    }
  },
  computed: {
    ...mapGetters(['allConstructors']),
  },
  methods: {
    getFlagURL,
    edit(constructor) {
      this.$emit('editObject', constructor, this.allConstructors);
    },
    graph(constructor) {
      this.$emit('showGraph', constructor);
    },
    editConstructors() {
      this.$emit('editArray', this.allConstructors);
    }
  },
};

</script>

<template>
  <div :class="`${collapsed ? 'tw-w-20 tw-transition-all' : 'tw-w-56'}`" class="tw-flex tw-flex-col tw-h-full tw-gap-2">
    <div class="hover-parent tw-border-primary-light tw-text-f1-white tw-relative tw-flex tw-flex-row tw-justify-between tw-items-center">
      <ContinueButton @continue="editConstructors" class="hover-child tw-transition-opacity tw-px-1">
        <span class="material-symbols-outlined tw-text-xl">edit_note</span>
      </ContinueButton>
      <div :class="`${collapsed ? 'not-hover-child tw-absolute tw-left-0 tw-right-0 tw-text-xs tw-pointer-events-none' : 'tw-text-xl'}`" class="tw-font-bold tw-mt-0.5">
        Constructors
      </div>
      <ContinueButton @continue="collapsed = !collapsed" class="hover-child tw-transition-opacity tw-px-1">
        <span class="material-symbols-outlined tw-text-xl">{{ !collapsed ? 'chevron_left' : 'chevron_right' }}</span>
      </ContinueButton>
    </div>
    <div :class="`${collapsed ? 'tw-w-20 tw-transition-all' : 'tw-w-56'}`" class="tw-flex tw-flex-col tw-h-full tw-justify-between" :key="allConstructors.length">
      <ConstructorDisplay @edit="edit" @graph="graph" v-for="constructor in allConstructors" :key="constructor.code"
                          :constructor="constructor" :flagURL="getFlagURL(constructor.country)" :collapsed="collapsed"/>
    </div>
  </div>
</template>

<style scoped>

</style>