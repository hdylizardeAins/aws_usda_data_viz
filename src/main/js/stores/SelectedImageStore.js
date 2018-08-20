import Vue from 'vue';
import axios from 'axios';

var selectedImageStore = {
    state: {
        selectedImage: {
            imgSrc: "",
            show: false
        }
    },
    mutations: {
        updateSelectedImage: function(state, image) {
            state.selectedImage = image;
        },
        hideSelectedImage: function(state){
            state.selectedImage.show = false;
        }
    }
};

export default selectedImageStore;