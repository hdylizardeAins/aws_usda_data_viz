import Vue from 'vue'
import ElementUI from 'element-ui'
import locale from 'element-ui/lib/locale/lang/en'
import App from './App.vue'
import store from './stores/Stores.js'
import 'font-awesome/css/font-awesome.min.css'

// //Our Element.js theme (auto-generated with element-theme tool)
// import './css/element/index.css'

// //Our manual Element.js theme overrides
// import './css/elementui-theme.css'

//Our CSS
import './css/global.css'

Vue.use(ElementUI, { locale })  //Use English localization of ElementJS

//Manually create the Vue.js container to use (simplifies dev server setup)
var mainContainer = document.createElement("div");
document.body.appendChild(mainContainer);

//Start Vue.js
new Vue({
  el: mainContainer,
  render: h => h(App),
  store    //Has to be named "store" for vuex to inject it properly
});