<template>
    <div style="height: 50px;line-height: 50px;border-bottom: 1px solid #ccc;display: flex">
        <div style="width: 200px;padding-left: 30px;font-weight: bold;color: dodgerblue">后台管理</div>
        <div style="flex: 1"></div>
        <div style="width: 100px">
            <el-dropdown>
                <el-button type="primary">
                {{user.nickName}}<i class="el-icon-arrow-down el-icon--right"></i>
                </el-button>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item>个人信息</el-dropdown-item>
                    <el-dropdown-item @click.native="$router.push('/login')">退出系统</el-dropdown-item>

                </el-dropdown-menu>
            </el-dropdown>


        </div>
    </div>
</template>

<script>
    export default {
        name: "Header",

        created() {
            let str=sessionStorage.getItem("user")||"{}"
            this.user=JSON.parse(str);

        }
    }
</script>

<style scoped>

</style>