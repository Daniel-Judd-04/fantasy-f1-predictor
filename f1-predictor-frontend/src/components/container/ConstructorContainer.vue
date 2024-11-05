<script>

import ConstructorDisplay from "@/components/display/ConstructorDisplay.vue";
import {getFlagURL} from "@/utils/common";
import {mapGetters} from "vuex";

export default {
  name: 'ConstructorContainer',
  components: {ConstructorDisplay},
  computed: {
    ...mapGetters(['allConstructors']),
  },
  methods: {
    getFlagURL,
    edit(constructor) {
      this.$emit('edit', constructor, this.allConstructors);
    },
  },
};

</script>

<template>
  <div class="tw-flex tw-flex-col tw-h-full tw-justify-between">
    <ConstructorDisplay @edit="edit" v-for="constructor in allConstructors" :key="constructor.code"
                        :constructor="constructor"
                        :flagURL="getFlagURL(constructor.country)"/>
  </div>
</template>

<style scoped>

</style>