<template>
  <div>
    <br />
    <el-container>
      <el-header style="text-align: center; height: 100px">
        <div class="block">
          <el-avatar v-image-preview :src="imageUrl" :size="100"></el-avatar
          ><el-button
            @click="uploadExamDialog = true"
            v-if="haveExam"
            size="small"
            >上传照片</el-button
          >
          <el-button v-else type="danger" @click="deleteExam" size="small"
            >删除照片</el-button
          >
        </div>
      </el-header>
      <el-main>
        <el-form :model="personInfo" class="demo-ruleForm" v-if="ifUpdate">
          <div class="form-group">
            <el-form-item prop="realName"
              >姓名
              <el-input
                type="text"
                autocomplete="off"
                v-model="personInfo.realName"
            /></el-form-item>
          </div>
          <fieldset disabled>
            <div class="form-group">
              <el-form-item prop="stuNo"
                >学号
                <el-input
                  type="text"
                  autocomplete="off"
                  v-model="personInfo.stuNo"
              /></el-form-item>
            </div>
            <div class="form-group">
              <el-form-item prop="major"
                >专业
                <el-input
                  type="text"
                  autocomplete="off"
                  v-model="personInfo.major"
              /></el-form-item>
            </div>
            <div class="form-group">
              <el-form-item prop="className"
                >专业班级
                <el-input
                  type="text"
                  autocomplete="off"
                  v-model="personInfo.className"
              /></el-form-item>
            </div>
            <div class="form-group">
              <el-form-item prop="identificationNumber"
                >身份证号码
                <el-input
                  type="text"
                  autocomplete="off"
                  v-model="personInfo.identificationNumber"
              /></el-form-item>
            </div>
          </fieldset>
          <el-button @click="changeIfUpdate">更改信息</el-button>
        </el-form>

        <el-form
          :model="personInfoUpdate"
          ref="personInfoUpdate"
          :rules="rule"
          class="demo-ruleForm"
          v-if="!ifUpdate"
        >
          <fieldset>
            <div class="form-group">
              <el-form-item prop="u_realName"
                >姓名
                <el-input
                  type="text"
                  autocomplete="off"
                  v-model="personInfoUpdate.u_realName"
              /></el-form-item>
            </div>
            <div class="form-group">
              <el-form-item prop="u_stuNo"
                >学号
                <el-input
                  type="text"
                  autocomplete="off"
                  v-model="personInfoUpdate.u_stuNo"
              /></el-form-item>
            </div>
            <div class="form-group">
              <el-form-item prop="u_major"
                >专业<br />
                <el-select
                  v-model="personInfoUpdate.u_major"
                  placeholder="请选择"
                  @change="getClassList(personInfoUpdate.u_major)"
                >
                  <el-option
                    v-for="item in majorList"
                    :key="item.discipline"
                    :label="item.major + '系' + item.discipline"
                    :value="item.discipline"
                  >
                  </el-option> </el-select
              ></el-form-item>
            </div>
            <div class="form-group" v-if="chooseMajor">
              <el-form-item prop="u_className"
                >专业班级<br />
                <el-select
                  v-model="personInfoUpdate.u_className"
                  placeholder="请选择"
                >
                  <el-option
                    v-for="item in classList"
                    :key="item"
                    :label="item"
                    :value="item"
                  >
                  </el-option> </el-select
              ></el-form-item>
            </div>
            <div class="form-group">
              <el-form-item prop="u_identificationNumber"
                >身份证号码
                <el-input
                  type="text"
                  autocomplete="off"
                  v-model="personInfoUpdate.u_identificationNumber"
              /></el-form-item>
            </div>
          </fieldset>
          <el-button type="button" @click="changeIfUpdate">取消更改</el-button>
          <el-button type="button" @click="dialogVisible = true"
            >更改信息</el-button
          >

          <el-dialog title="提示" :visible.sync="dialogVisible" width="30%">
            <span>确认修改个人信息吗？</span>
            <span slot="footer" class="dialog-footer">
              <el-button @click="dialogVisible = false">取 消</el-button>
              <el-button
                type="primary"
                @click="updateInformation('personInfoUpdate')"
                >确 定</el-button
              >
            </span>
          </el-dialog>
        </el-form>
      </el-main>
      <el-footer></el-footer>
    </el-container>

    <el-dialog :visible.sync="uploadExamDialog" width="400px">
      <el-upload
        class="upload-demo"
        ref="upload"
        action=""
        accept="image/jpeg, image/png"
        :on-remove="handleRemove"
        :auto-upload="false"
        :on-change="uploadImg"
        list-type="picture"
        :file-list="fileUpLoadList"
        :limit="1"
      >
        <el-button slot="trigger" size="small" type="primary"
          >选取文件</el-button
        >
        <el-button
          @click="uploadExam"
          style="margin-left: 10px"
          size="small"
          type="success"
          >上传照片</el-button
        >
        <div slot="tip" class="el-upload__tip">
          只能上传jpg/png文件，且不超过1MB
        </div>
      </el-upload>
    </el-dialog>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
import axios from "axios";
export default {
  inject: ["reload"],
  name: "personalImformation",
  data() {
    return {
      personInfo: {
        realName: "",
        stuNo: "",
        major: "",
        className: "",
        identificationNumber: "",
      },
      ifUpdate: true,

      //获得专业列表
      majorList: [],
      //获得班级列表
      classList: [],
      chooseMajor: false,

      //更新专用，判断是录入还是更新
      personInfoUpdate: {
        u_realName: "",
        u_stuNo: "",
        u_major: "",
        u_className: "",
        u_identificationNumber: "",
      },
      dialogVisible: false,
      //头像dialog
      uploadExamDialog: false,
      //获取头像
      imageFile: {},
      //上传头像的file
      fileData: new FormData(),
      fileData: new window.FormData(),
      //图片名字
      fileName: "",
      //是否有头像
      haveExam: true,
      //图片地址
      imageUrl: "",
      //图片id
      imageId: "",
      //主页轮播图片表单
      fileUpLoadList: [],

      rule: {
        u_realName: [
          { required: true, message: "请输入姓名", trigger: "blur" },
        ],
        u_stuNo: [
          { required: true, message: "请输入学号", trigger: "blur" },
          { min: 9, max: 10, message: "请输入正确的学号", trigger: "blur" },
        ],
        u_major: [{ required: true, message: "请输入专业", trigger: "blur" }],
        u_className: [
          { required: true, message: "请输入班级号", trigger: "blur" },
        ],
        u_identificationNumber: [
          { required: true, message: "请输入身份证", trigger: "blur" },
          {
            min: 15,
            max: 18,
            message: "请如实填写18位身份证号码",
            trigger: "blur",
          },
          {
            required: true,
            pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/,
            message: "身份证格式错误，请输入正确的身份证号码",
            trigger: "blur",
          },
        ],
      },
    };
  },
  mounted: function () {
    this.getImformation();
  },
  computed: {
    ...mapState({
      print: (state) => state.print.all,
      userId: (state) => state.userId.all,
    }),
  },
  methods: {
    getImformation: function () {
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
        },
        method: "get",
        url: "http://kana.chat:70/userInfo?username=" + this.print.username,
      }).then(
        function (reponse) {
          that.personInfo = reponse.data.data;

          that.personInfoUpdate.u_realName = reponse.data.data.realName;
          that.personInfoUpdate.u_stuNo = reponse.data.data.stuNo;
          that.personInfoUpdate.u_major = reponse.data.data.major;
          that.personInfoUpdate.u_className = reponse.data.data.className;
          that.personInfoUpdate.u_identificationNumber =
            reponse.data.data.identificationNumber;
        },
        function (err) {
          that.$message.error("你没有信息，请尽快填写");
        }
      );

      //获取考试准考证照片
      axios({
        headers: { Authorization: this.print.Authorization },
        method: "get",
        url: "http://kana.chat:70/image/user?userId=" + this.userId.userId,
      }).then(function (response) {
        that.imageFile = response.data.data;
        that.imageFile = that.imageFile.filter((item) => item.tag == "Exam");
        if (that.imageFile.length == 0) {
          that.haveExam = true;
          that.imageUrl = "";
        } else {
          that.haveExam = false;
          that.imageUrl = that.imageFile[0].url;
        }
      });
    },

    changeIfUpdate: function () {
      this.ifUpdate = !this.ifUpdate;
      this.personInfoUpdate.u_realName = this.personInfo.realName;
      this.personInfoUpdate.u_stuNo = this.personInfo.stuNo;
      this.personInfoUpdate.u_major = this.personInfo.major;
      this.personInfoUpdate.u_className = this.personInfo.className;
      this.personInfoUpdate.u_identificationNumber = this.personInfo.identificationNumber;

      //获得专业列表
      var that = this;
      axios({
        headers: { Authorization: this.print.Authorization },
        method: "get",
        url: "http://kana.chat:70/major/all?pageNum&pageSize",
      }).then(function (reponse) {
        for (var i = 0; i < reponse.data.data.length; i++) {
          that.majorList[i] = {
            major: reponse.data.data[i].major,
            discipline: reponse.data.data[i].discipline,
          };
        }
        that.majorList = that.unique(that.majorList);
        //that.majorList = reponse.data.data;
        console.log(that.majorList);
      });
    },

    //去掉相同项
    unique(arr) {
      const res = new Map();
      return arr.filter(
        (arr) => !res.has(arr.discipline) && res.set(arr.discipline, 1)
      );
    },

    getClassList: function (major) {
      if (major != "") {
        this.classList = [];
        this.chooseMajor = true;
        let value = "";
        this.majorList.forEach((item) => {
          if (item.discipline == major) {
            this.value = item.major;
          }
        });
        var that = this;
        axios({
          headers: { Authorization: this.print.Authorization },
          method: "get",
          url: "http://kana.chat:70/major?major=" + this.value,
        }).then(function (reponse) {
          reponse.data.data.forEach((item) => {
            if (item.discipline == major) {
              that.classList.push(item.className);
            }
          });
        });
      }
    },

    updateInformation: function (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          var that = this;
          //判断是录入还是更新
          //只要有一个空值就是录入
          if (this.personInfo.realName == "") {
            console.log("录入 post");
            //录入 post请求
            axios({
              method: "post",
              url: "http://kana.chat:70/userInfo",
              headers: {
                Authorization: this.print.Authorization,
                "Content-Type":
                  "application/x-www-form-urlencoded; charset=UTF-8",
              },
              params: {
                username: this.print.username,
                realName: this.personInfoUpdate.u_realName,
                className: this.personInfoUpdate.u_className,
                stuNo: this.personInfoUpdate.u_stuNo,
                major: this.personInfoUpdate.u_major,
                identificationNumber: this.personInfoUpdate
                  .u_identificationNumber,
              },
            }).then(
              function (reponse) {
                that.$message({
                  message: "更改成功",
                  type: "success",
                });
                that.$router.go(0);
              },
              function (err) {
                that.$message.error("更改失败");
              }
            );
          } else {
            console.log("更新 put");
            //更新 put请求
            axios({
              method: "put",
              url: "http://kana.chat:70/userInfo",
              params: {
                username: this.print.username,
                realName: this.personInfoUpdate.u_realName,
                className: this.personInfoUpdate.u_className,
                stuNo: this.personInfoUpdate.u_stuNo,
                major: this.personInfoUpdate.u_major,
                identificationNumber: this.personInfoUpdate
                  .u_identificationNumber,
              },
              headers: {
                Authorization: this.print.Authorization,
                "Content-Type":
                  "application/x-www-form-urlencoded; charset=UTF-8",
              },
            }).then(
              function (reponse) {
                that.$message({
                  message: "更改成功",
                  type: "success",
                });
                that.$router.go(0);
              },
              function (err) {
                that.$message.error("更改失败");
              }
            );
          }
        } else {
          this.dialogVisible = false;
          return false;
        }
      });
    },

    uploadImg: function (file) {
      const isLt2M = file.size / 1024 / 1024 < 1;
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 1MB!");
      }
      this.fileData = file.raw;
      this.fileName =
        file.raw.name.slice(0, file.raw.name.length - 4) + this.userId.userId;
    },

    handleRemove(file, fileList) {
      console.log(file, fileList);
      this.fileUpLoadList = [];
    },

    uploadExam: function () {
      let param = new FormData();
      param.append("file", this.fileData);
      param.append("fileName", this.fileName);
      let config = {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: this.print.Authorization,
        }, //这里是重点，需要和后台沟通好请求头，Content-Type不一定是这个值
      }; //添加请求头
      var that = this;
      axios
        .all([
          axios.post("http://kana.chat:70/common/aliyun", param, config),
          axios({
            headers: { Authorization: this.print.Authorization },
            method: "post",
            url: "http://kana.chat:70/image",
            params: {
              imageName: this.fileData.name.slice(
                0,
                this.fileData.name.length - 4
              ),
              userId: this.userId.userId,
              tag: "Exam",
            },
          }),
        ])
        .then(
          axios.spread(function (aliyunResponse, infoResponse) {
            that.$message({
              message: "上传照片成功",
              type: "success",
            });
            that.reload();
          })
        );
    },

    deleteExam: function () {
      var that = this;
      this.imageId = this.imageFile[0].imageId;
      axios
        .all([
          axios({
            headers: { Authorization: this.print.Authorization },
            method: "delete",
            url: "http://kana.chat:70/image?imageId=" + this.imageId,
          }),
          axios({
            headers: { Authorization: this.print.Authorization },
            method: "delete",
            url: "http://kana.chat:70/common/aliyun?fileurl=" + this.imageUrl,
          }),
        ])
        .then(
          axios.spread(function (del1, del2) {
            that.$message({
              message: "删除照片成功",
              type: "success",
            });
            that.reload();
          })
        );
    },
  },
};
</script>