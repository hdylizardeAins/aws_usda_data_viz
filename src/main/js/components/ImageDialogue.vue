<template>
    <el-dialog :visible="selectedImage.show" @close="handleClose">
        <figure>
            <img class="resized-image" :src="selectedImage.imgSrc"/>
            <figcaption>
                <p>{{ selectedImage.graphData }}</p>
                <p>{{ selectedImage.caption }}</p>
            </figcaption>
        </figure>
    </el-dialog>
</template>
<script>
export default {
    computed: {
        selectedImage: function(){
            return this.$store.state.selectedImageStore.selectedImage;
        }
    },
    methods: {
        handleClose: function(){
            this.$store.commit('hideSelectedImage');
        }
    }
}
</script>
<style>
.resized-image {
    display: inline-block;
    max-width: 90vw;
    padding: 5px;
    overflow-y: auto;
}
</style>