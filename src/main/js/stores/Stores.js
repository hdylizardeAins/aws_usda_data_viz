import Vue from 'vue';
import Vuex from 'vuex';
import DatasetsStore from './DatasetsStore.js';

Vue.use(Vuex);

var store = new Vuex.Store({
   modules: {
       datasetsStore: DatasetsStore
   }
});

export default store;