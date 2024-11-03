<script>
import {
  differenceInDays,
  differenceInHours,
  differenceInMinutes,
  differenceInMonths,
  differenceInWeeks,
  differenceInYears,
  format,
  parseISO
} from 'date-fns';

export default {
  name: 'CircuitDisplay',
  props: {
    circuit: {
      default: () => ({}),
      type: Object,
      required: true
    },
    grandPrix: {
      default: () => ({}),
      type: Object,
      required: true
    }
  },
  data() {
    return {
      currentTime: new Date()
    };
  },
  computed: {
    formattedDate() {
      if (!this.grandPrix.startTime) {
        return "No date set";
      }
      const targetDate = parseISO(this.grandPrix.startTime);
      const now = this.currentTime;
      const isFuture = targetDate > now;

      const years = Math.abs(differenceInYears(targetDate, now));
      const months = Math.abs(differenceInMonths(targetDate, now));
      const weeks = Math.abs(differenceInWeeks(targetDate, now));
      const days = Math.abs(differenceInDays(targetDate, now));
      const hours = Math.abs(differenceInHours(targetDate, now));
      const minutes = Math.abs(differenceInMinutes(targetDate, now));

      if (years > 1) {
        return format(targetDate, 'dd/MM/yyyy');
      } else if (months >= 1) {
        return isFuture ? `In ${months} month${months > 1 ? 's' : ''}` : `${months} month${months > 1 ? 's' : ''} ago`;
      } else if (weeks >= 1) {
        return isFuture ? `In ${weeks} week${weeks > 1 ? 's' : ''}` : `${weeks} week${weeks > 1 ? 's' : ''} ago`;
      } else if (days >= 1) {
        return isFuture ? `In ${days} day${days > 1 ? 's' : ''}` : `${days} day${days > 1 ? 's' : ''} ago`;
      } else if (hours >= 1) {
        return isFuture ? `In ${hours} hour${hours > 1 ? 's' : ''}` : `${hours} hour${hours > 1 ? 's' : ''} ago`;
      } else if (minutes >= 1) {
        return isFuture ? `In ${minutes} minute${minutes > 1 ? 's' : ''}` : `${minutes} minute${minutes > 1 ? 's' : ''} ago`;
      } else {
        return "Now";
      }
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
  }
}

</script>

<template>
  <div
      class="tw-border-2 tw-w-96 tw-h-64 tw-bg-gradient-to-bl tw-from-primary-dark tw-to-f1-red tw-to-150% tw-text-f1-white tw-flex tw-flex-col tw-rounded">
    <div class="tw-ml-auto tw-text-right tw-px-2">
      <div class="tw-font-bold tw-text-xl">{{ grandPrix.fullName }}</div>
      <div>{{ circuit.fullName }}, {{ circuit.locationName }}, {{ circuit.country }}</div>
    </div>
    <div class="tw-w-full tw-h-full tw-bg-primary-dark tw-flex tw-flex-col">
      <div class="tw-flex tw-flex-row tw-h-full tw-px-2 tw-gap-2 tw-mt-2">
        <div class="tw-h-full tw-w-full tw-bg-white tw-rounded">
          <!--IMAGE-->
        </div>
        <div class="tw-ml-auto tw-w-28 tw-flex tw-flex-col tw-justify-around">
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
      <div class="tw-w-full tw-flex tw-flex-col">
        <div class="tw-flex tw-flex-row tw-justify-around">
          <div>Circuit Length: {{ circuit.length }} km</div>
          <div>Race Distance: {{ getRaceLength() }} km</div>
        </div>
        <div class="tw-flex tw-flex-row tw-justify-around">
          <div>{{ circuit.laps }} Laps</div>
          <div>{{ circuit.corners }} Corners</div>
          <div>{{ circuit.drsZones }} DRS Zones</div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>