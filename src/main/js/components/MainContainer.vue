<template>
    <div>
        <p>Main Container</p>
    </div>
</template>

<script>

export default {
    components: {
    },
    mounted() {
        //Read application settings
        var me = this;
        this.$store.dispatch('readSettings', {
            failure(err) {
                me.$notify.error({
                    message: 'Unable to read application settings from server: ' + err.message,
                    customClass: 'error',
                    duration: 0,
                    title: 'Error'
                });
            }
        });
    },
    methods: {
        handleHSplitterResized(event){
            this.broadcastEvent('h-splitter-drag-end', event);
        },
        broadcastEvent(event, args){
            EventBus.$emit(event, args);
        }
    }
}
</script>

<style>
</style>
