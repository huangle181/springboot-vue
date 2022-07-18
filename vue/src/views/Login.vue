<template>
    <div style="width: 100%; height: 100vh; background-color: dodgerblue; overflow:hidden">
        <div style="width: 400px; margin: 150px auto">
            <div style="color: #cccccc; font-size: 30px; text-align: center; padding:30px 0 ">个人网站</div>
            <el-form ref="form" :model="form" size="normal" :rules="rules">
                <el-icon><user /></el-icon>
                <el-form-item prop="username">
                    <el-input prefix-icon="el-icon-user-solid" v-model="form.username" placeholder="用户名"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input prefix-icon="el-icon-lock" v-model="form.password" placeholder="密码" show-password></el-input>
                </el-form-item>

                <el-form-item>
                    <div style="display: flex">
                        <el-input prefix-icon="el-icon-key" v-model="form.validCode" style="width: 50%;" placeholder="请输入验证码"></el-input>
                        <ValidCode @input="createValidCode" />
                    </div>
                </el-form-item>
                <el-form-item>
                    <el-button style = "width:100%" type="primary" @click="login">登陆</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    import request from "../../utils/request";
    import ValidCode from "../components/ValidCode";
    export default {
        name: "Login",
        components: {
            ValidCode,
        },
        data(){
            return {
                form:{},
                validCode: '',
                rules: {
                    username: [
                        { required: true, message: '请输入用户名', trigger: 'blur'},
                    ],
                    password: [
                        { required: true, message: '请输入密码', trigger: 'blur'},
                    ],
                }
            }
        },
        created() {
            sessionStorage.removeItem("user")
        },
        methods:{
            //接收验证码组件提交的 4位验证码
            createValidCode(data) {
                this.validCode = data
            },
            login(){
                this.$refs['form'].validate((valid) => {
                    if (valid) {
                        if (!this.form.validCode) {
                            this.$message.error("请填写验证码")
                            return
                        }
                        if(this.form.validCode.toLowerCase() !== this.validCode.toLowerCase()) {
                            this.$message.error("验证码错误")
                            return
                        }
                        request.post("/api/user/login",this.form).then(res => {
                            console.log(res)
                            if (res.code === '0') {
                                this.$message({
                                    type: "success",
                                    message: "登陆成功"
                                })
                                sessionStorage.setItem("user",JSON.stringify(res.data)) //缓存用户信息
                                this.$router.push("/user")  //登陆成功后进行页面跳转，跳转到主页
                            } else {
                                this.$message({
                                    type: "error",
                                    message: res.msg
                                })
                            }
                        })
                    }
                })
            }
        }
    }
</script>

<style scoped>
</style>