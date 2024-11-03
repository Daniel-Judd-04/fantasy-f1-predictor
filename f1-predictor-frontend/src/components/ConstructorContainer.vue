<script>

import ConstructorDisplay from "@/components/ConstructorDisplay.vue";
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
  },
};

</script>

<template>
  <div>
    <div class="tw-bg-primary">
      <div>Teams:</div>
      <div class="tw-flex tw-flex-col tw-gap-2">
        <ConstructorDisplay v-for="constructor in allConstructors" :key="constructor.code" :constructor="constructor"
                            :flagURL="getFlagURL(constructor.country)"/>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>