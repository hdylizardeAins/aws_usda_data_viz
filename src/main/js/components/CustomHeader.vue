<template>
    <div class="logo-banner" />
</template>
<script>
</script>
<style>
.logo-banner {
    height: 82px; 
    background-image: url("../../webapp/js/images/banner.jpg");
    background-repeat: no-repeat;
    background-position: top center;
    background-size: 100% 100%;
    overflow: hidden;
}

@media screen and (max-width: 1050px) {
    .logo-banner {
        background-image: url("../../webapp/js/images/banner-small.png");
    }
}
</style>