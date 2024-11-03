<script>
import {
  differenceInDays,
  differenceInHours,
  differenceInMinutes,
  differenceInMonths,
  differenceInWeeks,
  differenceInYears,
  format,
  isFuture,
  parseISO
} from 'date-fns';
import {getCircuitById} from "@/utils/common";

export default {
  name: 'CircuitDisplay',
  props: {
    grandPrix: {
      default: () => ({}),
      type: Object,
      required: true
    }
  },
  data() {
    return {
      currentTime: new Date(),
      circuit: {},
    };
  },
  watch: {
    grandPrix: {
      immediate: true,
      handler() {
        this.circuit = getCircuitById(this.grandPrix.circuit);
      }
    }
  },
  computed: {
    formattedDate() {
      if (!this.grandPrix.startTime) {
        return "No date set";
      }
      const targetDate = parseISO(this.grandPrix.startTime);
      const now = this.currentTime;
      if (isFuture(targetDate)) {
        const years = Math.abs(differenceInYears(targetDate, now));
        const months = Math.abs(differenceInMonths(targetDate, now));
        const weeks = Math.abs(differenceInWeeks(targetDate, now));
        const days = Math.abs(differenceInDays(targetDate, now));
        const hours = Math.abs(differenceInHours(targetDate, now));
        const minutes = Math.abs(differenceInMinutes(targetDate, now));

        if (years >= 1) {
          return `In ${years} year${years > 1 ? 's' : ''}`;
        } else if (months >= 1) {
          return `In ${months} month${months > 1 ? 's' : ''}`;
        } else if (weeks >= 1) {
          return `In ${weeks} week${weeks > 1 ? 's' : ''}`;
        } else if (days >= 1) {
          return `In ${days} day${days > 1 ? 's' : ''}`;
        } else if (hours >= 1) {
          return `In ${hours} hour${hours > 1 ? 's' : ''}`;
        } else if (minutes >= 1) {
          return `In ${minutes} minute${minutes > 1 ? 's' : ''}`;
        } else {
          return "Now";
        }
      }

      return format(targetDate, 'dd/MM/yyyy');
    },
    inFuture() {
      return isFuture(parseISO(this.grandPrix.startTime));
    }
  },
  mounted() {
    // Set an interval to update the currentTime every minute for accurate time differences
    this.timer = setInterval(() => {
      this.currentTime = new Date();
    }, 60000);
  },
  beforeUnmount() {
    // Clear the timer to prevent memory leaks
    clearInterval(this.timer);
  },
  methods: {
    getRaceLength() {
      return Math.round(this.circuit.length * this.circuit.laps * 10) / 10;
    },
    getLocation() {
      let location = '';
      let locations = [this.circuit.country, this.circuit.locationName, this.circuit.fullName];

      for (let i = 0; i < locations.length; i++) {
        if (locations[i]) {
          location += locations[i];
          if (i < locations.length - 1) {
            location += ', ';
          }
        }
      }

      return location;
    },
  }
}

</script>

<template>
  <div
      class="tw-w-96 tw-h-64 tw-bg-gradient-to-bl tw-from-primary-dark tw-to-f1-red tw-to-150% tw-text-f1-white tw-flex tw-flex-col tw-border-2 tw-border-primary-light tw-rounded">
    <div class="tw-flex tw-flex-row tw-py-1">
      <div v-if="grandPrix.sprint" class="tw-h-full tw-pl-2">
        <span class="material-symbols-outlined">sprint</span>
      </div>
      <div class="tw-ml-auto tw-text-right tw-pr-2">
        <div class="tw-font-bold tw-text-xl">{{ grandPrix.fullName }}</div>
        <div>{{ getLocation() }}</div>
      </div>
    </div>
    <div class="tw-w-full tw-h-full tw-bg-primary-dark tw-flex tw-flex-col tw-gap-2 tw-py-2">
      <div class="tw-flex tw-text-xs tw-flex-row tw-justify-around">
        <div>Laps: {{ circuit.laps }}</div>
        <div>Turns: {{ circuit.corners }}</div>
        <div>DRS: {{ circuit.drsZones }}</div>
        <div>Length: {{ circuit.length }} km</div>
        <div>Race: {{ getRaceLength() }} km</div>
      </div>
      <div class="tw-flex tw-flex-row tw-h-full tw-px-2 tw-gap-2">
        <div class="tw-h-full tw-w-full tw-bg-white tw-rounded">
          <!--IMAGE-->
        </div>
        <div v-if="!inFuture"
             class="tw-font-thin tw-border-y-2 tw-border-primary-light tw-ml-auto tw-w-28 tw-flex tw-flex-col tw-justify-around">
          <div class="tw-flex">
            <span class="material-symbols-outlined tw-mr-auto">heat</span>
            <div>{{ grandPrix.airTemperature }}°C</div>
          </div>
          <div class="tw-flex">
            <span class="material-symbols-outlined tw-mr-auto">road</span>
            <div>{{ grandPrix.trackTemperature }}°C</div>
          </div>
          <div class="tw-flex">
            <span class="material-symbols-outlined tw-mr-auto">water_do</span>
            <div>{{ grandPrix.humidity }}%</div>
          </div>
          <div class="tw-flex">
            <span class="material-symbols-outlined tw-mr-auto">air</span>
            <div>{{ grandPrix.windSpeed }} mph</div>
          </div>
          <div class="tw-flex">
            <span class="material-symbols-outlined tw-mr-auto">rainy_light</span>
            <div>{{ grandPrix.rainfall ? 'Yes' : 'No' }}</div>
          </div>
        </div>
      </div>
    </div>
    <div v-if="inFuture" class="tw-font-bold tw-bg-primary">
      {{ formattedDate }}
    </div>
  </div>
</template>

<style scoped>

</style>