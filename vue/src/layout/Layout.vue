<template>
<div>
    <!--    头部-->
    <Header />
    <!--    主体-->
    <div style="display: flex">
        <!--    侧边-->
        <Aside/>
        <!--    内容-->
        <router-view style="flex: 1"/>
    </div>
</div>
</template>

<script>
    import Aside from "../components/Aside";
    import Header from "../components/Header";

    export default {
        name: "Layout",
        components:{
            Aside,
            Header
        }
    }
</script>

<style scoped>

</style>