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
    round(value, decimals) {
      return Number(Math.round(value + 'e' + decimals) + 'e-' + decimals);
    },
    getRaceLength() {
      return Math.round(this.circuit.length * this.circuit.laps * 10) / 10;
    },
    getOrdinalSuffix(day) {
      if (day > 3 && day < 21) return `${day}th`; // Handle 11th to 19th
      switch (day % 10) {
        case 1:
          return `${day}st`;
        case 2:
          return `${day}nd`;
        case 3:
          return `${day}rd`;
        default:
          return `${day}th`;
      }
    },
    getTitle() {
      // Temp, remove the year from the title in db later.
      return this.grandPrix.fullName.substring(0, this.grandPrix.fullName.length - 5);
    },
    getDate() {
      if (!this.grandPrix.startTime) {
        return "No date";
      }
      const date = parseISO(this.grandPrix.startTime);
      return `${this.getOrdinalSuffix(format(date, 'd'))} ${format(date, 'MMMM yyyy')}`;
    },
    getLocation() {
      let location = '';
      let locations = [this.circuit.fullName, this.circuit.locationName, this.circuit.country];

      for (let i = 0; i < locations.length; i++) {
        if (locations[i]) {
          if (i === 1 && (locations[0].includes(locations[1]) || this.grandPrix.fullName.includes(locations[1]))) {
            continue;
          }
          location += locations[i];
          if (i < locations.length - 1) {
            location += ', ';
          }
        }
      }

      return location;
    },
    getImageUrl(locationName) {
      try {
        // Dynamically require the image if necessary (use the correct path)
        return require(`@/assets/circuits/${locationName.split(' ').join('_')}.jpg`);
      } catch (e) {
        // Return a fallback image if the specific one is not found
        console.log("Fallback image used for", locationName);
        return require('@/assets/logo.png');
      }
    },
  }
}

</script>

<template>
  <div
      class="tw-w-96 tw-h-full tw-transition-colors tw-bg-gradient-to-bl tw-from-primary-dark tw-to-200% tw-flex tw-flex-col tw-outline tw-outline-1 -tw-outline-offset-1 tw-rounded"
      :class="[index === currentIndex ? 'tw-to-f1-red tw-text-f1-white tw-outline-primary-light' : 'tw-to-primary tw-text-primary-light tw-outline-primary']">
    <div class="tw-flex tw-flex-row">
      <div class="tw-mr-auto tw-text-left tw-pl-2 tw-py-1">
        <div class="tw-font-bold tw-text-xl">{{ getTitle() }}</div>
        <div>{{ getDate() }}</div>
      </div>
      <div v-if="grandPrix.sprint" class="tw-mr-2">
        <span class="material-symbols-outlined tw-font-light tw-text-3xl">sprint</span>
      </div>
    </div>
    <div class="tw-w-full tw-h-full tw-bg-primary-dark tw-rounded-b tw-flex tw-flex-col tw-pt-1">
      <div class="tw-flex tw-flex-row tw-h-full tw-px-1 tw-gap-1">
        <div class="tw-relative tw-h-full tw-w-full tw-bg-cover tw-bg-center tw-bg-f1-black tw-border-primary-light tw-border-1 tw-rounded"
             :class="[index === currentIndex ? '' : 'tw-opacity-50']"
             :style="{ backgroundImage: `url(${getImageUrl(circuit.locationName)})` }">
          <div class="tw-absolute tw-bottom-0.5 tw-left-1 tw-text-left tw-text-xs">
            {{ getLocation() }}
          </div>
        </div>
        <div
            class="tw-font-thin tw-border-y-1 tw-border-primary-light tw-ml-auto tw-w-36 tw-text-sm tw-flex tw-flex-col tw-justify-around">
          <div class="tw-flex">
            <div>Length:</div>
            <div class="tw-ml-auto">{{ round(circuit.length, 2) }}<span class="tw-text-xs">km</span></div>
          </div>
          <div class="tw-flex">
            <div>Race:</div>
            <div class="tw-ml-auto">{{ round(getRaceLength(), 0) }}<span class="tw-text-xs">km</span></div>
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

      <div v-if="!inFuture" class="tw-flex tw-h-8 tw-flex-row tw-justify-between tw-px-2 tw-items-center tw-py-1">
        <div class="tw-flex tw-items-center">
          <span class="material-symbols-outlined tw-font-light">heat</span>
          <div class="tw-pt-0.5">{{ round(grandPrix.airTemperature, 0) }}<span class="tw-text-xs">°C</span></div>
        </div>
        <div class="tw-flex tw-items-center">
          <span class="material-symbols-outlined tw-font-light">road</span>
          <div class="tw-pt-0.5">{{ round(grandPrix.trackTemperature, 0) }}<span class="tw-text-xs">°C</span></div>
        </div>
        <div class="tw-flex tw-items-center">
          <span class="material-symbols-outlined tw-font-light">water_do</span>
          <div class="tw-pt-0.5">{{ round(grandPrix.humidity, 0) }}<span class="tw-text-xs">%</span></div>
        </div>
        <div class="tw-flex tw-items-center">
          <span class="material-symbols-outlined tw-font-light">air</span>
          <div class="tw-ml-0.5 tw-pt-0.5">{{ round(grandPrix.windSpeed, 1) }}<span class="tw-text-xs">mph</span></div>
        </div>
        <div v-if="grandPrix.rainfall" class="tw-flex tw-items-center">
          <span class="material-symbols-outlined tw-font-light">rainy_light</span>
        </div>
      </div>
      <div v-if="inFuture" class="tw-h-8 tw-font-bold tw-rounded-b tw-bg-primary tw-pt-0.5 tw-mt-1">
        {{ formattedDate }}
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>