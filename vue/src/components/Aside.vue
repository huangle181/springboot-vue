<template>
    <div>
        <el-menu
                style="width: 200px;min-height:calc(100vh - 50px)"
                default-active="2"
                :default-openeds="['1']"
                router
                class="el-menu-vertical-demo"
                >

                <el-submenu index="1">
                    <template slot="title">管理系统</template>
                    <el-menu-item index="user">用户管理</el-menu-item>
                </el-submenu>
            <el-menu-item index="book" >书籍管理</el-menu-item>
            <el-menu-item index="news" >新闻管理</el-menu-item>
            <el-menu-item index="order" >我的订单</el-menu-item>

        </el-menu>
    </div>
</template>

<script>
    export default {
        name: "Aside"
    }
</script>

<style scoped>

</style>