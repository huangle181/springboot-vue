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
              prop="title"
              label="标题"
              >
      </el-table-column>

      <el-table-column
              prop="author"
              label="作者">
      </el-table-column>
      <el-table-column
              prop="time"
              label="发表日期">
      </el-table-column>


      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
                  size="mini"
                  @click="handleEdit(scope.row)">编辑</el-button>
          <el-button
                  size="mini"
                  @click="det(scope.row)">详情</el-button>
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
              width="60%">
        <el-form :model="form" label-width="120px">
          <el-form-item label="标题">
            <el-input v-model="form.title" style="width: 70%"></el-input>
          </el-form-item>

          <el-form-item label="作者">
            <el-input v-model="form.author" style="width: 70%"></el-input>
          </el-form-item>

           <div id="div1"
            ref="editors"
           >

           </div>
        </el-form>

        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="save ">确 定</el-button>
        </span>
      </el-dialog>
      <el-dialog title="详情"
                 :visible.sync="vis"
                  width="50%">
        <el-card>
          <div v-html="detail.content" style="min-height: 100px"></div>
        </el-card>
      </el-dialog>
    </div>
  </div>

</template>

<script>



import request from "../../utils/request";

import E from  'wangeditor'

let editor
export default {
  name: 'News',
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
      detail:{},
      vis:false,
      tableData:[

      ]
    }
  },
  created() {
    this.load()
  },
  methods: {
    det(row){
      this.detail=row
      this.vis=true
    },
    filesUploadSuccess(res){
      console.log(res)
      this.form.cover=res.data
    },
    load(){
      request.get("api/news",{
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

      this.$nextTick(() => {
      // 关联弹窗里面的div，new一个 editor对象
      if (!editor) {
        editor = new E('#div1')

        // 配置 server 接口地址
        editor.config.uploadImgServer = 'http://localhost:9090/files/editor/upload'
        editor.config.uploadFileName = "file"  // 设置上传参数名称
        editor.create()
      }

      editor.txt.html("")//若已经生成过富文本编辑器对象，则清空文本内容，

    })
    },
    save() {

      this.form.content=editor.txt.html()

      if(this.form.id){  //更新操作
        request.put("/api/news", this.form).then(res => {
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
        request.post("/api/news", this.form).then(res => {
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
        // 关联弹窗里面的div，new一个 editor对象
        // 关联弹窗里面的div，new一个 editor对象
        if (!editor) {
          editor = new E('#div1')

          // 配置 server 接口地址
          editor.config.uploadImgServer = 'http://localhost:9090/files/editor/upload'
          editor.config.uploadFileName = "file"  // 设置上传参数名称
          editor.create()
        }

        editor.txt.html(row.content)
      })

    },
    handleDelete(id) {
      request.delete("/api/news/"+id).then(res=>{

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
