<script>

import ConstructorDisplay from "@/components/display/ConstructorDisplay.vue";
import {getFlagURL} from "@/utils/common";
import {mapGetters} from "vuex";
import ContinueButton from "@/components/common/ContinueButton.vue";

export default {
  name: 'ConstructorContainer',
  components: {ContinueButton, ConstructorDisplay},
  computed: {
    ...mapGetters(['allConstructors']),
  },
  methods: {
    getFlagURL,
    editConstructor(constructor) {
      this.$emit('editObject', constructor, this.allConstructors);
    },
    editConstructors() {
      this.$emit('editArray', this.allConstructors);
    }
  },
};

</script>

<template>
  <div class="tw-flex tw-flex-col tw-h-full tw-gap-2">
    <div class="hover-parent tw-border-primary-light tw-text-f1-white tw-relative tw-flex tw-flex-row tw-justify-center tw-items-center">
      <div class="tw-font-medium tw-text-xl">Constructors</div>
      <ContinueButton @continue="editConstructors" class="hover-child tw-absolute tw-right-0 tw-h-8">
        Edit
      </ContinueButton>
    </div>
    <div class="tw-flex tw-flex-col tw-h-full tw-justify-between" :key="allConstructors.length">
      <ConstructorDisplay @edit="editConstructor" v-for="constructor in allConstructors" :key="constructor.code"
                          :constructor="constructor" :flagURL="getFlagURL(constructor.country)"/>
    </div>
  </div>
</template>

<style scoped>

</style>