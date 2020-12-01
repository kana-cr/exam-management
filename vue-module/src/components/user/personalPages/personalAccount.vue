<template>
  <div>
    <br />
    <el-container>
      <el-header style="text-align: center; height: 100px">
        <div class="block">
          <el-avatar v-image-preview :src="imageUrl" :size="100"></el-avatar
          ><el-button
            @click="uploadAvatarDialog = true"
            v-if="haveAvatar"
            size="small"
            >上传头像</el-button
          >
          <el-button v-else type="danger" @click="deleteAvatar" size="small"
            >删除头像</el-button
          >
        </div>
      </el-header>
      <el-main>
        <el-form v-if="ifUpdate" :model="personAccount" class="demo-ruleForm">
          <fieldset disabled>
            <div class="form-group">
              <el-form-item prop="userName"
                >用户名
                <el-input
                  type="text"
                  autocomplete="off"
                  v-model="personAccount.userName"
              /></el-form-item>
            </div>
            <div class="form-group">
              <el-form-item prop="fullName"
                >别名
                <el-input
                  type="text"
                  autocomplete="off"
                  v-model="personAccount.fullName"
              /></el-form-item>
            </div>
            <div class="form-group">
              <el-form-item prop="email"
                >邮箱
                <el-input
                  type="text"
                  autocomplete="off"
                  v-model="personAccount.email"
              /></el-form-item>
            </div>
            <div class="form-group">
              <el-form-item prop="userId"
                >用户ID
                <el-input
                  type="text"
                  autocomplete="off"
                  v-model="personAccount.userId"
              /></el-form-item>
            </div>
          </fieldset>
          <el-form-item>
            <el-button class="btn btn-primary" @click="changeIfUpdate"
              >更改信息</el-button
            >
          </el-form-item>
        </el-form>

        <el-form
          :model="personAccountUpdate"
          ref="personAccountUpdate"
          :rules="rule"
          v-else
        >
          <fieldset>
            <div class="form-group">
              <el-form-item prop="u_userName"
                >用户名
                <el-input
                  type="text"
                  v-model="personAccountUpdate.u_userName"
                  autocomplete="off"
                  disabled
              /></el-form-item>
            </div>
            <div class="form-group">
              <el-form-item prop="u_fullName"
                >别名
                <el-input
                  type="text"
                  autocomplete="off"
                  v-model="personAccountUpdate.u_fullName"
              /></el-form-item>
            </div>
            <div class="form-group">
              <el-form-item prop="password"
                >密码
                <el-input
                  type="password"
                  autocomplete="off"
                  placeholder="请输入密码"
                  v-model="personAccountUpdate.password"
                  show-password
              /></el-form-item>
            </div>
            <div class="form-group">
              <el-form-item prop="passwordConfirm"
                >再次输入密码
                <el-input
                  type="password"
                  autocomplete="off"
                  placeholder="请再次输入密码"
                  v-model="personAccountUpdate.passwordConfirm"
                  show-password
              /></el-form-item>
            </div>
          </fieldset>
          <el-form-item>
            <el-button class="btn btn-primary" @click="changeIfUpdate"
              >取消更改</el-button
            >
            <el-button
              class="btn btn-primary"
              @click="changeAccount('personAccountUpdate')"
              >更改</el-button
            >
          </el-form-item>
        </el-form>
      </el-main>
      <el-footer></el-footer>
    </el-container>

    <el-dialog :visible.sync="uploadAvatarDialog" width="400px">
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
          @click="uploadAvatar"
          style="margin-left: 10px"
          size="small"
          type="success"
          >上传头像</el-button
        >
        <div slot="tip" class="el-upload__tip">
          只能上传jpg/png文件，且不超过1MB
        </div>
      </el-upload>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";
import { mapState, mapActions } from "vuex";
export default {
  inject: ["reload"],
  name: "personalAccount",
  data() {
    var checkpwd = /(?!^(\d+|[a-zA-Z]+|[~!@#$%^&*?]+)$)^[\w~!@#$%^&*?]{6,20}$/;
    var validatePwd = (rule, value, callback) => {
      if (!checkpwd.test(value)) {
        callback(new Error("密码应是6-20位数字，字母或字符！"));
      } else {
        callback();
      }
    };
    var validatePwdConfirm = (rule, value, callback) => {
      if (!checkpwd.test(value)) {
        callback(new Error("密码应是6-20位数字，字母或字符！"));
      } else if (this.personAccountUpdate.password != value) {
        callback(new Error("两次密码不一致"));
      } else {
        callback();
      }
    };
    return {
      ifUpdate: true,
      //账户表
      personAccount: {
        userName: "",
        fullName: "",
        email: "",
        userId: "",
      },
      //更新账户表
      personAccountUpdate: {
        u_userName: "",
        u_fullName: "",
        password: "",
        passwordConfirm: "",
      },
      //图片地址
      imageUrl: "",
      //头像dialog
      uploadAvatarDialog: false,
      //更换显示按钮
      haveAvatar: true,
      //获取头像
      imageFile: {},
      //上传头像的file
      fileData: new FormData(),
      fileData: new window.FormData(),
      //头像id
      imageId: "",
      //主页轮播图片表单
      fileUpLoadList: [],
      //获得全部头像，为了小黑
      allImage: [],

      rule: {
        u_fullName: [
          { required: true, message: "昵称不能为空", trigger: "blur" },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { validator: validatePwd, trigger: "blur" },
        ],
        passwordConfirm: [
          { required: true, message: "请输入密码进行验证", trigger: "blur" },
          { validator: validatePwdConfirm, trigger: "blur" },
        ],
      },
    };
  },
  computed: {
    ...mapState({
      print: (state) => state.print.all,
      userId: (state) => state.userId.all,
    }),
  },
  mounted: function () {
    this.getUserAccount();
  },
  methods: {
    getUserAccount: function () {
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
        },
        method: "get",
        url: "/api/users/single?username=" + this.print.username,
      }).then(
        function (reponse) {
          that.personAccount = reponse.data.data;

          that.personAccountUpdate.u_userName = reponse.data.data.userName;
          that.personAccountUpdate.u_fullName = reponse.data.data.fullName;
        },
        function (err) {
          that.$message.error("获取失败");
        }
      );

      //获取头像
      axios({
        headers: { Authorization: this.print.Authorization },
        method: "get",
        url: "/api/image/user?userId=" + this.userId.userId,
      }).then(function (response) {
        that.imageFile = response.data.data;
        that.imageFile = that.imageFile.filter((item) => item.tag == "Avatar");
        if (that.imageFile.length == 0) {
          that.haveAvatar = true;
          //获得小黑头像
          var _that = that;
          axios({
            method: "get",
            url: "/api/image/tag?tag=Show",
          }).then(function (response) {
            _that.allImage = response.data.data;
            _that.allImage.forEach((img) => {
              if (img.imageName == "black") {
                _that.imageUrl = img.url;
              }
            });
          });
        } else {
          that.haveAvatar = false;
          that.imageUrl = that.imageFile[0].url;
        }
      });
    },

    changeIfUpdate: function () {
      this.ifUpdate = !this.ifUpdate;
      this.personAccountUpdate.u_fullName = this.personAccount.fullName;
    },

    changeAccount: function (formName) {
      this.$refs[formName].validate((valid) => {
        var that = this;
        if (this.personAccountUpdate.password != "")
          axios({
            headers: {
              Authorization: this.print.Authorization,
            },
            method: "put",
            data: {
              userName: this.personAccountUpdate.u_userName,
              fullName: this.personAccountUpdate.u_fullName,
              password: this.personAccountUpdate.password,
            },
            url: "/api/users",
          }).then(
            function (reponse) {
              that.$message({
                message: "更改成功",
                type: "success",
              });
              that.reload();
            },
            function (err) {
              that.$message.error("更改失败，请重新尝试");
            }
          );
        else
          this.$message({
            message: "密码没有输入",
            type: "warning",
          });
      });
    },

    handleRemove(file, fileList) {
      console.log(file, fileList);
      this.fileUpLoadList = [];
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

    uploadAvatar: function () {
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
          axios.post("/api/common/aliyun", param, config),
          axios({
            headers: { Authorization: this.print.Authorization },
            method: "post",
            url: "/api/image",
            params: {
              imageName: this.fileData.name.slice(
                0,
                this.fileData.name.length - 4
              ),
              userId: this.userId.userId,
              tag: "Avatar",
            },
          }),
        ])
        .then(
          axios.spread(function (aliyunResponse, infoResponse) {
            that.$message({
              message: "上传头像成功",
              type: "success",
            });
            that.reload();
          })
        );
    },

    deleteAvatar: function () {
      var that = this;
      this.imageId = this.imageFile[0].imageId;
      axios
        .all([
          axios({
            headers: { Authorization: this.print.Authorization },
            method: "delete",
            url: "/api/image?imageId=" + this.imageId,
          }),
          axios({
            headers: { Authorization: this.print.Authorization },
            method: "delete",
            url: "/api/common/aliyun?fileurl=" + this.imageUrl,
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