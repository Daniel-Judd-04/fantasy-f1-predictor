<script>
import {differenceInDays, differenceInHours, differenceInMinutes, differenceInMonths, differenceInWeeks, differenceInYears, format, isFuture, parseISO} from 'date-fns';
import {formatDate, getCircuitById} from "@/utils/common";
import {mapGetters} from "vuex";

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
    ...mapGetters(['allGrandsPrix']),
    isNewSeason() {
      return this.allGrandsPrix[this.index - 1] ? (this.grandPrix.season !== this.allGrandsPrix[this.index - 1].season) : true;
    },
    formattedDate() {
      if (!this.grandPrix.startTime) {
        return "No date set";
      }
      const targetDate = parseISO(this.grandPrix.startTime);
      const now = this.currentTime;

      const years = Math.abs(differenceInYears(targetDate, now));
      const months = Math.abs(differenceInMonths(targetDate, now));
      const weeks = Math.abs(differenceInWeeks(targetDate, now));
      const days = Math.abs(differenceInDays(targetDate, now));
      const hours = Math.abs(differenceInHours(targetDate, now));
      const minutes = Math.abs(differenceInMinutes(targetDate, now));

      if (isFuture(targetDate)) {
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
          return "Happening Soon";
        }
      } else if (this.isHappeningNow()) {
        return "Happening Now";
      }

      return format(targetDate, 'dd/MM/yyyy');
    }
  },
  mounted() {
    // Set an interval to update the currentTime every minute for accurate time differences
    this.timer = setInterval(() => {
          this.currentTime = new Date();
          this.inFuture = isFuture(parseISO(this.grandPrix.startTime));
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
    formatDate,
    isFuture() {
      if (!this.grandPrix.startTime) {
        return false;
      }
      return isFuture(parseISO(this.grandPrix.startTime));
    },
    isHappeningNow() {
      if (!this.grandPrix.startTime) {
        return false;
      }
      return !isFuture(parseISO(this.grandPrix.startTime)) && Math.abs(differenceInHours(parseISO(this.grandPrix.startTime), this.currentTime)) < 3;
    },
    hasWeatherData() {
      return this.grandPrix.airTemperature && this.grandPrix.trackTemperature && this.grandPrix.humidity && this.grandPrix.windSpeed;
    },
    round(value, decimals) {
      return Number(Math.round(value + 'e' + decimals) + 'e-' + decimals);
    },
    getRaceLength() {
      return Math.round(this.circuit.length * this.circuit.laps * 10) / 10;
    },
    getLocation() {
      let location = '';
      let locations = [this.circuit.fullName, this.circuit.locationName];

      for (let i = 0; i < locations.length; i++) {
        if (locations[i]) {
          if (i === 1 && (locations[0].includes(locations[1]) || this.grandPrix.fullName.includes(locations[1]))) {
            continue;
          }
          location += locations[i] + ', ';
        }
      }

      return location.substring(0, location.length - 2);
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
    graph() {
      this.$emit('graph', this.grandPrix);
    },
  }
}

</script>

<template>
  <div :id="`grand-prix-${index}`" class="hover-parent tw-h-full tw-flex tw-transition-all"
       :class="[index === currentIndex ? 'tw-text-f1-white' : 'tw-text-primary-light tw-opacity-25']">
    <div v-if="isNewSeason"
         class="tw-bg-primary-dark tw-h-full tw-flex tw-flex-col tw-justify-center tw-px-3 tw-rounded-l tw-border-y-1 tw-border-l-1 tw-border-primary-light">
      <div v-for="(digit, i) in grandPrix.season.toString().split('')" :key="digit.code"
           class="tw-text-3xl tw-leading-none"
           :class="[i > 1 && index === currentIndex ? 'tw-text-f1-white' : 'tw-text-primary-light', index === currentIndex ? 'tw-font-extrabold' : 'tw-font-medium']">
        {{ digit }}
      </div>
    </div>
    <div class="tw-w-96 tw-h-full tw-bg-gradient-to-bl tw-from-primary-dark tw-to-200% tw-flex tw-flex-col tw-outline tw-outline-1 tw-outline-primary-light -tw-outline-offset-1"
         :class="[index === currentIndex ? 'tw-to-f1-red' : 'tw-to-primary', isNewSeason ? 'tw-rounded-r' : 'tw-rounded']">
      <div class="tw-flex tw-flex-row tw-px-2 tw-gap-2">
        <div class="tw-mr-auto tw-text-left tw-pb-1">
          <div class="tw-font-bold tw-text-xl tw-mt-0.5">{{ this.grandPrix.fullName }}</div>
          <div class="tw-leading-none">{{ formatDate(this.grandPrix.startTime) }}</div>
        </div>
        <div :id="`${index === currentIndex ? 'selected-grand-prix' : ''}`" class="tw-hidden">{{ grandPrix.grandPrixId }}</div>
        <div v-if="grandPrix.sprint">
          <span class="material-symbols-outlined tw-font-light tw-text-3xl tw-mt-0.5">sprint</span>
        </div>
        <div v-if="isHappeningNow()" class="tw-flex tw-flex-col tw-py-2">
          <div class="tw-flex tw-gap-1 tw-items-center tw-justify-center tw-border-1 tw-border-f1-red tw-rounded tw-p-1 tw-bg-f1-red tw-bg-opacity-50 tw-font-bold">
            <div class="live-border tw-w-4 tw-h-4 tw-rounded-full tw-flex tw-items-center tw-justify-center tw-border-2 tw-border-f1-red">
              <div class="live-bg tw-w-1 tw-h-1 tw-rounded-full tw-bg-f1-red"></div>
            </div>
            <div class="tw-leading-none tw-mt-[1px]">LIVE</div>
          </div>
        </div>
      </div>
      <div class="tw-w-full tw-h-full tw-bg-primary-dark tw-rounded-b tw-flex tw-flex-col tw-pt-1">
        <div class="tw-flex tw-flex-row tw-h-full tw-px-1 tw-gap-1">
          <div class="tw-relative tw-h-full tw-w-full tw-bg-cover tw-bg-center tw-bg-f1-black tw-border-primary-light tw-border-1 tw-rounded"
               :style="{ backgroundImage: `url(${getImageUrl(circuit.locationName)})` }">
            <div v-if="grandPrix.results.length > 0" class="tw-absolute tw-right-0.5 tw-top-0.5">
              <span @click="graph()"
                    class="hover-child tw-transition-opacity material-symbols-outlined tw-flex tw-items-center tw-justify-center tw-cursor-pointer tw-text-xl tw-leading-none">bar_chart</span>
            </div>
            <div class="tw-absolute tw-flex tw-gap-0.5 tw-items-end tw-left-0.5 tw-bottom-0.5 tw-text-left tw-text-xs tw-leading-none">
              <span class="material-symbols-outlined tw-text-sm tw-font-light tw-leading-none">location_on</span>
              <div class="tw-mb-0">{{ getLocation() }}</div>
            </div>
          </div>
          <div
              class="tw-px-0.5 tw-border-y-1 tw-border-primary-light tw-ml-auto tw-w-36 tw-text-sm tw-font-light tw-leading-1 tw-flex tw-flex-col tw-justify-around">
            <div class="tw-flex tw-justify-between">
              <div>Length:</div>
              <div>{{ circuit.length.toFixed(1) }}<span class="tw-text-xs">km</span></div>
            </div>
            <div class="tw-flex tw-justify-between">
              <div>Race:</div>
              <div>{{ getRaceLength().toFixed(0) }}<span class="tw-text-xs">km</span></div>
            </div>
            <div class="tw-flex tw-justify-between">
              <div>Laps:</div>
              <div>{{ circuit.laps }}</div>
            </div>
            <div class="tw-flex tw-justify-between">
              <div>Turns:</div>
              <div>{{ circuit.corners }}</div>
            </div>
            <div class="tw-flex tw-justify-between">
              <div>DRS:</div>
              <div>{{ circuit.drsZones }}</div>
            </div>
          </div>
        </div>


        <div v-if="isFuture() || isHappeningNow()" class="tw-h-8 tw-font-bold tw-rounded-b tw-bg-primary tw-pt-0.5 tw-mt-1">
          {{ formattedDate }}
        </div>
        <div v-else-if="!isFuture() && !isHappeningNow() && hasWeatherData()" class="tw-flex tw-h-8 tw-flex-row tw-justify-between tw-px-2 tw-items-center tw-py-1">
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
        <div v-else class="tw-h-8 tw-text-sm tw-italic tw-rounded-b tw-bg-primary tw-pt-1 tw-mt-1">
          Weather Data Unavailable
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.live-border {
  animation: pulse 1.5s infinite ease-in-out;
}

.live-bg {
}

@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 50%;
  }
  50% {
    transform: scale(0.9);
    opacity: 100%;
  }
  100% {
    transform: scale(1);
    opacity: 50%;
  }
}
</style>