<template>
  <div style="padding: 10px">

    <el-button type="primary" @click="add">新增</el-button>
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="请输入内容" style="width: 20%" clearable></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="load">搜索</el-button>
    </div>
    <el-table
            ref="filterTable"
            :data="tableData"
            stripe
            style="width: 100%">
      <el-table-column
              prop="id"
              label="ID"
              sortable
      >
      </el-table-column>
      <el-table-column
              prop="name"
              label="书名"
              >
      </el-table-column>
      <el-table-column
              label="封面"
      >
        <template #default="scope">

            <el-image
                    style="width: 100px; height: 100px"
                    :src="scope.row.cover"
                    :preview-src-list="[scope.row.cover]">
            </el-image>

        </template>
      </el-table-column>
      <el-table-column
              prop="price"
              label="价格">
      </el-table-column>
      <el-table-column
              prop="press"
              label="出版社">
      </el-table-column>
      <el-table-column
              prop="createTime"
              label="出版日期">
      </el-table-column>

      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="buy(scope.row.id)">购买</el-button>
          <el-button
                  size="mini"
                  @click="handleEdit(scope.row)">编辑</el-button>
          <el-popconfirm
                  title="这是一段内容确定删除吗？" @confirm="handleDelete(scope.row.id)"
          >
            <el-button slot="reference"
                       size="mini"
                       type="danger"
                       style="margin-left: 5px">删除</el-button>
          </el-popconfirm>

        </template>
      </el-table-column>
    </el-table>
<!--    标签-->
    <div style="margin: 10px 0">
      <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="currentPage4"
              :page-sizes="[10, 20, 30]"
              :page-size="pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="total">
      </el-pagination>


      <el-dialog
              title="提示"
              :visible.sync="dialogVisible"
              width="30%">
        <el-form :model="form" label-width="120px">
          <el-form-item label="书名">
            <el-input v-model="form.name" style="width: 70%"></el-input>
          </el-form-item>
          <el-form-item label="封面">
            <el-upload
                    ref="upload"
                    action="http://localhost:9090/files/upload"
                    :on-success="filesUploadSuccess">
              <el-button  type="primary">点击上传</el-button>
            </el-upload>
          </el-form-item>
          <el-form-item label="价格">
            <el-input v-model="form.price" style="width: 70%"></el-input>
          </el-form-item>
          <el-form-item label="出版社">
            <el-input v-model="form.press" style="width: 70%"></el-input>
          </el-form-item>

          <el-form-item label="出版日期">
            <el-date-picker
                    v-model="form.createTime"
                    type="date"

                    style="width: 70%"
                    placeholder="选择日期">
            </el-date-picker>
          </el-form-item>
        </el-form>

        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="save ">确 定</el-button>
        </span>
      </el-dialog>
    </div>
  </div>

</template>

<script>



import request from "../../utils/request";

export default {
  name: 'Book',
  components: {

  },
  data(){
    return{
      form:{},
      search:'',
      currentPage4:1,
      pageSize:10,
      total:10,
      dialogVisible:false,
      tableData:[

      ]
    }
  },
  created() {
    let userStr = sessionStorage.getItem("user") || "{}"
    this.user = JSON.parse(userStr)
    // 请求服务端，确认当前登录用户的 合法信息
    request.get("/user/" + this.user.id).then(res => {
      if (res.code === '0') {
        this.user = res.data
      }
    })

    this.load()
  },
  methods: {
    buy(bookId) {
      request.get("/api/order/buy/" + bookId).then(res => {
        // 请求成功跳转沙箱支付的页面
        window.open(res.data)
      })
    },
    filesUploadSuccess(res){
      console.log(res)
      this.form.cover=res.data
    },
    load(){
      request.get("api/book",{
        params:{
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res=>{
        this.tableData=res.data.records
        this.total=res.data.total
      })
    },
    add() {
      this.dialogVisible = true
      this.form = {}
      this.$refs['upload'].clearFiles()
    },
    save() {
      if(this.form.id){  //更新操作
        request.put("/api/book", this.form).then(res => {
          console.log(res)
          if(res.code === '0'){
            this.$message({
              type:"success",
              message:"更新成功"
            })
          }
          else {
            this.$message({
              type:"error",
              message:"更新失败"
            })
          }
          this.load()  //更新操作
          this.dialogVisible=false //关闭弹窗
        })
      }
      else { //添加操作
        request.post("/api/book", this.form).then(res => {
          console.log(res)
          if(res.code === '0'){
            this.$message({
              type:"success",
              message:"添加成功"
            })
          }
          else {
            this.$message({
              type:"error",
              message:"添加失败"
            })
          }
          this.load()  //更新操作
          this.dialogVisible=false //关闭弹窗
        })
      }

    },
    handleEdit(row) {
      this.form=JSON.parse(JSON.stringify(row))//深拷贝，减少影响
      this.dialogVisible=true
      this.$nextTick(() => {
        if (this.$refs['upload']) {
          this.$refs['upload'].clearFiles()  // 清除历史文件列表
        }
      })
    },
    handleDelete(id) {
      request.delete("/api/book/"+id).then(res=>{

        if(res.code === '0'){
          this.$message({
            type:"success",
            message:"删除成功"
          })
        }
        else {
          this.$message({
            type:"error",
            message:"删除失败"
          })
        }
        this.load()  //更新操作

      })
    },
    handleSizeChange(pageSize) {
      this.pageSize=pageSize
      this.load()
      },
    handleCurrentChange(pageNum) {
      this.currentPage=pageNum
      this.load()
    }


  }
}
</script>
