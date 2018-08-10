import Vue from 'vue';
import Vuex from 'vuex';
import DatasetsStore from './DatasetsStore.js';
import AnalyticsStore from './AnalyticsStore.js';

Vue.use(Vuex);

var store = new Vuex.Store({
   modules: {
       datasetsStore: DatasetsStore,
       analyticsStore: AnalyticsStore
   }
});

export default store;