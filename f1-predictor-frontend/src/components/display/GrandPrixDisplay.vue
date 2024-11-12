<script>
import {differenceInDays, differenceInHours, differenceInMinutes, differenceInMonths, differenceInWeeks, differenceInYears, format, isFuture, parseISO} from 'date-fns';
import {getCircuitById} from "@/utils/common";

export default {
  name: 'CircuitDisplay',
  props: {
    grandPrix: {
      default: () => ({}),
      type: Object,
      required: true
    },
    index: {
      type: Number,
      required: true
    },
    currentIndex: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      currentTime: new Date(),
      circuit: {},
    };
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
      if (!this.grandPrix.startTime) {
        return false;
      }
      return isFuture(parseISO(this.grandPrix.startTime));
    }
  },
  mounted() {
    // Set an interval to update the currentTime every minute for accurate time differences
    this.timer = setInterval(() => {
          this.currentTime = new Date();
        },
        60000);
  },
  watch: {
    grandPrix: {
      immediate: true,
      handler(newGrandPrix) {
        if (newGrandPrix && newGrandPrix.circuit) {
          this.circuit = getCircuitById(newGrandPrix.circuit);
        }
      }
    }
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
      let locations = [this.circuit.fullName, this.circuit.locationName, this.circuit.country];

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
      class="tw-w-96 tw-h-full tw-transition-colors tw-bg-gradient-to-bl tw-from-primary-dark tw-to-200% tw-flex tw-flex-col tw-border-1 tw-rounded"
      :class="[index === currentIndex ? 'tw-to-f1-red tw-text-f1-white tw-border-primary-light' : 'tw-to-primary tw-text-primary-light tw-border-primary']">
    <div class="tw-flex tw-flex-row tw-py-1">
      <div class="tw-mr-auto tw-text-left tw-pl-2">
        <div class="tw-font-bold tw-text-xl">{{ grandPrix.fullName }}</div>
        <div>{{ getLocation() }}</div>
      </div>
      <div v-if="grandPrix.sprint" class="tw-h-full tw-pr-2">
        <span class="material-symbols-outlined">sprint</span>
      </div>
    </div>
    <div class="tw-w-full tw-h-full tw-bg-primary-dark tw-flex tw-flex-col tw-gap-2 tw-pt-2">
      <div class="tw-flex tw-flex-row tw-h-full tw-px-2 tw-gap-2">
        <div class="tw-h-full tw-w-full tw-bg-f1-black tw-border-primary-light tw-border-1 tw-rounded"
             :class="[index === currentIndex ? '' : 'tw-opacity-50']">
          <!--IMAGE-->
        </div>
        <div
            class="tw-font-thin tw-border-y-1 tw-border-primary-light tw-ml-auto tw-w-36 tw-text-xs tw-flex tw-flex-col tw-justify-around">
          <div class="tw-flex">
            <div>Length:</div>
            <div class="tw-ml-auto">{{ circuit.length }}km</div>
          </div>
          <div class="tw-flex">
            <div>Race:</div>
            <div class="tw-ml-auto">{{ getRaceLength() }}km</div>
          </div>
          <div class="tw-flex">
            <div>Laps:</div>
            <div class="tw-ml-auto">{{ circuit.laps }}</div>
          </div>
          <div class="tw-flex">
            <div>Turns:</div>
            <div class="tw-ml-auto">{{ circuit.corners }}</div>
          </div>
          <div class="tw-flex">
            <div>DRS:</div>
            <div class="tw-ml-auto">{{ circuit.drsZones }}</div>
          </div>
        </div>
      </div>

      <div v-if="!inFuture" class="tw-flex tw-h-8 tw-flex-row tw-justify-around tw-items-center tw-pb-1">
        <div class="tw-flex tw-items-center tw-gap-1">
          <span class="material-symbols-outlined">heat</span>
          <div>{{ grandPrix.airTemperature }}°C</div>
        </div>
        <div class="tw-flex tw-items-center tw-gap-1">
          <span class="material-symbols-outlined">road</span>
          <div>{{ grandPrix.trackTemperature }}°C</div>
        </div>
        <div class="tw-flex tw-items-center tw-gap-1">
          <span class="material-symbols-outlined">water_do</span>
          <div>{{ grandPrix.humidity }}%</div>
        </div>
        <div class="tw-flex tw-items-center tw-gap-1">
          <span class="material-symbols-outlined">air</span>
          <div>{{ grandPrix.windSpeed }} mph</div>
        </div>
        <div class="tw-flex tw-items-center tw-gap-1">
          <span class="material-symbols-outlined">rainy_light</span>
          <div>{{ grandPrix.rainfall ? 'Yes' : 'No' }}</div>
        </div>
      </div>
      <div v-if="inFuture" class="tw-h-8 tw-font-bold tw-rounded-b tw-bg-primary">
        {{ formattedDate }}
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>