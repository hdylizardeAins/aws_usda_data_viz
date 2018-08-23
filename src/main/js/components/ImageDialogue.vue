<template>
    <el-dialog :visible="selectedImage.show" @close="handleClose" class="image-dialog">
        <div class="scrollable-div">
            <figure>
                <img class="resized-image" :src="selectedImage.imgSrc"/>
                <figcaption>
                    <p>{{ selectedImage.graphData }}</p>
                    <p>{{ selectedImage.caption }}</p>
                </figcaption>
            </figure>
        </div>
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
div.scrollable-div {
    overflow:auto;
}

img.resized-image {
    display: inline-block;
    padding: 5px;
}

div.image-dialog {
    max-width: 95vw;
    margin:auto;
    height: auto;
    overflow-x:auto;
    overflow-y:auto;
}

.image-dialog figure{
    text-align:center;
}

.image-dialog .el-dialog{
    max-width: 95vw;
}

</style>