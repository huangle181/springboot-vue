<template>
  <div style="padding: 10px">

    <el-button type="primary" @click="add">新增}</el-button>
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

              column-key="date"

      >
      </el-table-column>
      <el-table-column
              prop="username"
              label="用户名"
              >
      </el-table-column>
      <el-table-column
              prop="nickName"
              label="昵称">
      </el-table-column>
      <el-table-column
              prop="age"
              label="年龄">
      </el-table-column>
      <el-table-column
              prop="address"
              label="地址">
      </el-table-column>
      <el-table-column
              prop="tag"
              label="标签"
              :filters="[{ text: '家', value: '家' }, { text: '公司', value: '公司' }]"
              :filter-method="filterTag"
              filter-placement="bottom-end">
        <template slot-scope="scope">
          <el-tag
                  :type="scope.row.tag === '家' ? 'primary' : 'success'"
                  disable-transitions>{{scope.row.tag}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
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
          <el-form-item label="用户名">
            <el-input v-model="form.username" style="width: 70%"></el-input>
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="form.password" style="width: 70%"></el-input>
          </el-form-item>
          <el-form-item label="昵称">
            <el-input v-model="form.nickName" style="width: 70%"></el-input>
          </el-form-item>
          <el-form-item label="性别">
            <el-radio v-model="form.sex" label="男">男</el-radio>
            <el-radio v-model="form.sex" label="女">女</el-radio>
            <el-radio v-model="form.sex" label="曾小贤">曾小贤</el-radio>

          </el-form-item>
          <el-form-item label="年龄">
            <el-input v-model="form.age" style="width: 70%"></el-input>
          </el-form-item>
          <el-form-item label="地址">
            <el-input type="textarea" v-model="form.address" style="width: 70%"></el-input>
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
  name: 'User',
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
    this.load()
  },
  methods: {
    load(){
      request.get("api/user",{
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
    },
    save() {
      if(this.form.id){  //更新操作
        request.put("/api/user", this.form).then(res => {
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
        request.post("/api/user", this.form).then(res => {
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
      console.log(row);
    },
    handleDelete(id) {
      request.delete("/api/user/"+id).then(res=>{

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
