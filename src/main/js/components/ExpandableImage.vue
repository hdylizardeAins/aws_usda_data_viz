<template>
    <img class="graph-image" :src="imageUrl" @click="handleImageClick">
</template>
<script>
import Constants from './Constants.js';

export default{
    props: ["imageUrl"],
    methods:{
        handleImageClick: function(){
            this.$store.commit('updateSelectedImage', {
                imgSrc: this.imageUrl,
                show: true
            })
        }
    }
}
</script>
<style>
.graph-image {
    max-width: 100%;
    display: block;
}
</style>