<template>
    <figure>
        <img class="graph-image" :src="imageUrl" @click="handleImageClick" />
        <figcaption>
            <p>{{ caption }}</p>
            <p>{{ graphData }}</p>
        </figcaption>
    </figure>
    
</template>
<script>
import Constants from './Constants.js';

export default{
    props: ["imageUrl", "graphData", "caption"],
    methods:{
        handleImageClick: function(){
            this.$store.commit('updateSelectedImage', {
                imgSrc: this.imageUrl,
                graphData: this.graphData,
                caption: this.caption,
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

.graph-image:hover {
    cursor: zoom-in;
}
</style>