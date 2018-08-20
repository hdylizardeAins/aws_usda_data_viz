<template>
    <el-dialog :visible="selectedImage.show" @close="handleClose">
        <img class="resized-image" :src="selectedImage.imgSrc"/>
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