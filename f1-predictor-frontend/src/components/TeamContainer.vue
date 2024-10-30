<script>

import TeamDisplay from "@/components/TeamDisplay.vue";
import {getFlagURL} from "@/utils/common";
import {mapGetters} from "vuex";

export default {
  name: 'TeamContainer',
  components: {TeamDisplay},
  computed: {
    ...mapGetters(['allTeams']),
  },
  methods: {
    getFlagURL,
  },
};

</script>

<template>
  <div>
    <div class="tw-flex tw-flex-row">
      <div class="tw-p-4 tw-bg-primary">
        <div>Teams:</div>
        <TeamDisplay v-for="team in allTeams" :key="team.code" :team="team"
                     :flagURL="getFlagURL(team.country)"/>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>