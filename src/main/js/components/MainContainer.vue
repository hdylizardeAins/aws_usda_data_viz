<template>
    <div>
        <logo-banner />
        <div class="main-container">
            <datasets-panel></datasets-panel>
        </div>
    </div>
</template>

<script>
import LogoBanner from './LogoBanner.vue';
import DatasetsPanel from './DatasetsPanel.vue';
export default {
    components: {
        LogoBanner,
        DatasetsPanel
    },
    mounted() {

    },
    methods: {
    }
}
</script>
<style>
/* Main container fills the screen */
div.main-container {
    position: absolute;
    top: 150px;
    bottom: 0px;
    left: 0px;
    right: 0px;
    display:flex;
}
</style>
