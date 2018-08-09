<template>
    <div id="header-container">
        <div class="logo-banner">
        </div>
        <div class="nav-bar">
            <ul>
                <li><a>DASHBOARD</a></li>
                <li><a>NEW WORK</a></li>
                <li><a>MY WORK</a></li>
                <li><a>ANALYTICS and VISUALIZATION LIBRARY</a></li>
            </ul>
        </div>
    </div>
</template>
<script>
</script>
<style>
.logo-banner {
    position: absolute;
    top: 10px;
    left: 0px;
    right: 0px;
    height: 82px;
    background-image: url("../../webapp/js/logo.png");
    background-repeat: no-repeat;
    background-position: top center;
    overflow: hidden;
}
.headerListDiv {
    position:relative;
    float:right;
    margin-right:280px;
    margin-top:49px;
}

.headerListDiv ul{
    list-style-type: none;
    overflow:hidden;
}

.headerListDiv li{
    padding-right: 20px;
    float:left;
}

.nav-bar {
    position:absolute;
    top: 92px;
    height: 50px;
    background-color: blue;
    width:100%;
}

.nav-bar ul{
    list-style-type: none;
    margin: 0;
    padding: 0;
    display:flex;
    align-items: center;
    justify-content: center;
    overflow: hidden;
}

.nav-bar li {
    float: left;
}

.nav-bar a {
    display: block;
    color:rgb(206, 200, 200);
    text-align: center;
    padding: 16px;
    text-decoration: none;
}

.nav-bar a:hover {
    color:white;
}

#header-container a:hover {
    cursor: pointer;
}
</style>