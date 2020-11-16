<template>
  <div class="container">
    <el-button @click="openMessageForm">编辑主页内容</el-button>
    <el-button @click="openImgForm">编辑主页图片</el-button>
    <br />
    <br />
    <br />
    <el-table
      v-if="ifMain"
      v-loading="loading"
      :data="
        messageList.slice((currentPage - 1) * pagesize, currentPage * pagesize)
      "
    >
      <el-table-column prop="title" label="标题" align="center">
      </el-table-column>
      <el-table-column prop="label" label="标签" width="120" align="center">
      </el-table-column>
      <el-table-column
        prop="subDate"
        label="发布日期"
        width="100"
        align="center"
      >
      </el-table-column>
      <el-table-column fixed="right" label="操作" align="center">
        <template slot-scope="scope">
          <el-button size="mini" @click="getMessageDetail(scope.row)"
            >详细</el-button
          >
          <el-button type="danger" size="mini" @click="deleteMessage(scope.row)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      background
      align="center"
      layout="total, prev, pager, next, jumper"
      :total="pageTotal"
      v-if="ifMain"
    >
    </el-pagination>

    <!-- 编辑主页内容 -->
    <el-form :model="messageForm" v-if="ifMessage" label-width="120px">
      <el-form-item label="标题">
        <el-input v-model="messageForm.title"></el-input>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-input v-model="messageForm.subDate" readonly></el-input>
      </el-form-item>
      <el-form-item label="标签">
        <el-select v-model="messageForm.label" placeholder="请选择活动区域">
          <el-option label="考试相关" value="考试相关"></el-option>
          <el-option label="网站相关" value="网站相关"></el-option>
          <el-option label="其他" value="其他"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="内容">
        <el-input type="textarea" v-model="messageForm.text"></el-input>
      </el-form-item>
      <el-form-item label="备注">
        <el-input v-model="messageForm.note"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="buildMessage">立即创建</el-button>
        <el-button @click="resetMessageForm">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 显示/更新主页内容 -->
    <el-form :model="messageForm" v-if="ifShowMessage" label-width="120px">
      <el-form-item label="标题">
        <el-input v-model="messageForm.title" :readonly="ifReadonly"></el-input>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-input v-model="messageForm.subDate" readonly></el-input>
      </el-form-item>
      <el-form-item label="标签">
        <el-select v-model="messageForm.label">
          <el-option label="考试相关" value="考试相关"></el-option>
          <el-option label="网站相关" value="网站相关"></el-option>
          <el-option label="其他" value="其他"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="内容">
        <el-input
          type="textarea"
          v-model="messageForm.text"
          :readonly="ifReadonly"
        ></el-input>
      </el-form-item>
      <el-form-item label="备注">
        <el-input v-model="messageForm.note" :readonly="ifReadonly"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="returnMain">返回</el-button>
        <el-button type="primary" @click="beforeUpdate">{{
          updateButtonName
        }}</el-button>
        <el-button type="success" @click="updateMessage" v-if="!ifReadonly"
          >更新</el-button
        >
      </el-form-item>
    </el-form>

    <!-- 编辑主页图片 -->
    <div v-if="ifImg" v-loading="loading">
      <el-upload
        class="upload-demo"
        ref="upload"
        action=""
        accept="image/jpeg, image/png"
        :on-preview="handlePreview"
        :on-remove="handleRemove"
        :auto-upload="false"
        :on-change="onUploadChange"
        list-type="picture"
        :file-list="fileUpLoadList"
        :limit="1"
      >
        <el-button slot="trigger" size="small" type="primary"
          >选取文件</el-button
        >
        <el-button
          style="margin-left: 10px"
          size="small"
          type="success"
          @click="uploadFile"
          >上传到服务器</el-button
        >
        <div slot="tip" class="el-upload__tip">
          只能上传jpg/png文件，且不超过1MB
        </div>
      </el-upload>

      <el-tabs v-model="activeName" type="border-card" @tab-click="handleClick">
        <el-tab-pane label="全部" name="all">
          <el-row>
            <el-col
              :span="4"
              v-for="item in imageList"
              :key="item.imageId"
              :offset="1"
            >
              <div style="margin-top: 15px">
                <el-card :body-style="{ padding: '0px' }" shadow="hover">
                  <img
                    v-image-preview
                    :src="item.url"
                    class="image"
                    height="200px"
                    width="190px"
                  />
                  <div>
                    <span>{{ item.imageName }}</span
                    ><br />
                    <div class="bottom clearfix">
                      <small>类型：{{ item.tagUTF }} </small><br />
                      <small>上传人：{{ item.username }} </small>
                      <br />
                      <el-button
                        type="text"
                        @click="deleteImg(item)"
                        size="mini"
                        >删除</el-button
                      >
                    </div>
                  </div>
                </el-card>
              </div>
            </el-col>
          </el-row>
        </el-tab-pane>

        <el-tab-pane label="主页展示" name="Show">
          <el-row>
            <el-col
              :span="4"
              v-for="item in imageList"
              :key="item.imageId"
              :offset="1"
            >
              <div style="margin-top: 15px">
                <el-card :body-style="{ padding: '0px' }" shadow="hover">
                  <img
                    v-image-preview
                    :src="item.url"
                    class="image"
                    height="200px"
                    width="190px"
                  />
                  <div>
                    <span>{{ item.imageName }}</span
                    ><br />
                    <div class="bottom clearfix">
                      <small>类型：{{ item.tagUTF }} </small><br />
                      <small>上传人：{{ item.username }} </small>
                      <br />
                      <el-button
                        type="text"
                        @click="deleteImg(item)"
                        size="mini"
                        >删除</el-button
                      >
                    </div>
                  </div>
                </el-card>
              </div>
            </el-col>
          </el-row>
        </el-tab-pane>

        <el-tab-pane label="个人头像" name="Avatar">
          <el-row>
            <el-col
              :span="4"
              v-for="item in imageList"
              :key="item.imageId"
              :offset="1"
            >
              <div style="margin-top: 15px">
                <el-card :body-style="{ padding: '0px' }" shadow="hover">
                  <img
                    v-image-preview
                    :src="item.url"
                    class="image"
                    height="200px"
                    width="190px"
                  />
                  <div>
                    <span>{{ item.imageName }}</span
                    ><br />
                    <div class="bottom clearfix">
                      <small>类型：{{ item.tagUTF }} </small><br />
                      <small>上传人：{{ item.username }} </small>
                      <br />
                      <el-button
                        type="text"
                        @click="deleteImg(item)"
                        size="mini"
                        >删除</el-button
                      >
                    </div>
                  </div>
                </el-card>
              </div>
            </el-col>
          </el-row>
        </el-tab-pane>

        <el-tab-pane label="准考证照片" name="Exam">
          <el-row>
            <el-col
              :span="4"
              v-for="item in imageList"
              :key="item.imageId"
              :offset="1"
            >
              <div style="margin-top: 15px">
                <el-card :body-style="{ padding: '0px' }" shadow="hover">
                  <img
                    v-image-preview
                    :src="item.url"
                    class="image"
                    height="200px"
                    width="190px"
                  />
                  <div>
                    <span>{{ item.imageName }}</span
                    ><br />
                    <div class="bottom clearfix">
                      <small>类型：{{ item.tagUTF }} </small><br />
                      <small>上传人：{{ item.username }} </small>
                      <br />
                      <el-button
                        type="text"
                        @click="deleteImg(item)"
                        size="mini"
                        >删除</el-button
                      >
                    </div>
                  </div>
                </el-card>
              </div>
            </el-col>
          </el-row>
        </el-tab-pane>
      </el-tabs>
      <el-backtop> </el-backtop>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { mapState, mapActions } from "vuex";
export default {
  inject: ["reload"],
  name: "adminHomepage",
  data() {
    return {
      //时间
      nowDate: "",
      loading: false,
      //显示主页
      ifMain: true,
      //显示表单
      ifMessage: false,
      ifImg: false,
      //查看消息
      ifShowMessage: false,
      //是否可编辑，更新
      ifReadonly: true,
      //更新按钮更改显示
      updateButtonName: "更新",
      //初始页
      currentPage: 1,
      //每页的数据
      pagesize: 10,
      //数组总数
      pageTotal: 100000,
      //存放消息
      messageList: [],
      //主页消息表单
      messageForm: {
        title: "",
        text: "",
        subDate: "",
        label: "",
        note: "",
      },

      carouselId: "",
      //主页轮播图片表单
      fileUpLoadList: [],
      file: new FormData(),
      file: new window.FormData(),
      fileName: "",
      imageName: "",
      //图片列表
      imageList: [],
      //卡片选择all展示所有图
      activeName: "all",
      //用户信息表
      userList: [],
      //不同类别图片不同用用户表
      otherUser: [],
    };
  },
  computed: {
    ...mapState({
      print: (state) => state.print.all,
      userId: (state) => state.userId.all,
    }),
  },
  created: function () {
    var aData = new Date();
    //console.log(aData);
    //Wed Aug 21 2019 10:00:58 GMT+0800 (中国标准时间)
    this.newData =
      aData.getFullYear() +
      "-" +
      (aData.getMonth() + 1) +
      "-" +
      aData.getDate();
  },
  mounted: function () {
    this.getMessage();
  },
  methods: {
    getMessage: function () {
      var that = this;
      this.loading = true;
      axios
        .all([
          //默认到消息页面，取得全部消息
          axios({
            method: "get",
            url: "http://kana.chat:70/carousel?pageNum=&pageSize=100000",
          }),
          //获取账号信息
          axios({
            headers: { Authorization: this.print.Authorization },
            method: "get",
            url: "http://kana.chat:70/users?pageNum=0&pageSize=100000",
          }),
        ])
        .then(
          axios.spread(function (messageResponse, userResponse) {
            that.messageList = messageResponse.data.data;
            that.pageTotal = messageResponse.data.data.length;
            that.loading = false;

            that.userList = userResponse.data.data;
          })
        );
    },

    handleCurrentChange: function (currentPage) {
      this.currentPage = currentPage;
    },

    openMessageForm: function () {
      this.ifMessage = !this.ifMessage;
      this.ifImg = false;
      this.ifShowMessage = false;
      if (this.ifMessage == false) this.ifMain = true;
      else this.ifMain = false;
      this.messageForm = {};
      this.messageForm.subDate = this.newData;

      this.messageForm.subDate = this.newData;
    },

    openImgForm: function () {
      this.ifImg = !this.ifImg;
      this.ifMain = !this.ifMain;
      this.ifShowMessage = false;
      if (this.ifImg == false) this.ifMain = true;
      else this.ifMain = false;

      this.ifMessage = false;
      var that = this;
      this.loading = true;

      //获取全部图片
      axios({
        method: "get",
        url: "http://kana.chat:70/image/all?pageNum=&pageSize=1000000",
      }).then(
        function (response) {
          that.imageList = response.data.data;
          that.imageList.forEach((item) => {
            if (item.tag == "Avatar") that.$set(item, "tagUTF", "头像");
            else if (item.tag == "Show") that.$set(item, "tagUTF", "展示");
            else if (item.tag == "Exam") that.$set(item, "tagUTF", "考试");

            that.otherUser = that.userList.filter(
              (user) => user.userId == item.userId
            );
            that.$set(item, "username", that.otherUser[0].userName);
          });
          that.loading = false;
        },
        function (err) {
          that.$message.error("获取图片失败");
          that.loading = false;
        }
      );
    },

    //图片上传
    handleRemove(file, fileList) {
      console.log(file, fileList);
      this.fileUpLoadList = [];
    },
    handlePreview(file) {
      console.log(file);
    },

    buildMessage: function () {
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
          "Content-Type": "application/x-www-form-urlencoded",
        },
        method: "post",
        url: "http://kana.chat:70/carousel",
        params: this.messageForm,
      }).then(
        function (response) {
          that.$message({
            message: "创建消息成功，请前往主页查看",
            type: "success",
          });
          that.messageList.push(that.messageForm);
          that.pageTotal++;
          that.messageForm = {};
          that.messageForm.subDate = that.newData;
        },
        function (err) {
          that.$message.error("创建消息失败，请重新尝试");
        }
      );
    },

    resetMessageForm: function () {
      this.messageForm = {};
      this.messageForm.subDate = this.newData;
    },

    getMessageDetail: function (row) {
      var that = this;
      axios({
        method: "get",
        url: "http://kana.chat:70/carousel/single?carouselId=" + row.carouselId,
      }).then(
        function (response) {
          that.messageForm = {
            title: response.data.data.title,
            subDate: response.data.data.subDate,
            label: response.data.data.label,
            text: response.data.data.text,
            note: response.data.data.note,
          };
          that.ifShowMessage = true;
          that.ifMain = false;
          that.carouselId = row.carouselId;
        },
        function (err) {
          that.$message.error("获取详细消息失败");
        }
      );
    },

    deleteMessage: function (row) {
      var that = this;
      axios({
        headers: { Authorization: this.print.Authorization },
        method: "delete",
        url: "http://kana.chat:70/carousel?carouselId=" + row.carouselId,
      }).then(
        function (response) {
          that.$message({
            message: "删除成功",
            type: "success",
          });
          that.reload();
        },
        function (err) {
          that.$message.error("获取详细消息失败");
        }
      );
    },

    returnMain: function () {
      this.ifShowMessage = false;
      this.ifMain = true;
    },

    beforeUpdate: function () {
      this.ifReadonly = !this.ifReadonly;
      if (this.ifReadonly == true) {
        this.updateButtonName = "更新";
      } else this.updateButtonName = "取消编辑";
    },

    updateMessage: function () {
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
          "Content-Type": "application/x-www-form-urlencoded",
        },
        method: "put",
        url: "http://kana.chat:70/carousel",
        params: {
          carouselId: this.carouselId,
          title: this.messageForm.title,
          text: this.messageForm.text,
          subDate: this.messageForm.subDate,
          label: this.messageForm.label,
          note: this.messageForm.note,
        },
      }).then(
        function (response) {
          that.$message({
            message: "更改信息成功",
            type: "success",
          });
          that.reload();
        },
        function (err) {
          that.$message.error("更改信息失败");
        }
      );
    },

    onUploadChange: function (file) {
      const isIMAGE =
        file.raw.type === "image/jpeg" || file.raw.type === "image/png";
      const isLt = file.size / 1024 / 1024 < 1;

      if (!isIMAGE) {
        this.$message.error("只能上传jpg/png图片");
        return false;
      }
      if (!isLt) {
        this.$message.error("上传文件大小不能超过1MB");
        return false;
      }
      var reader = new FileReader();
      reader.readAsDataURL(file.raw);
      reader.onload = function (e) {
        //图片base64数据
        //console.log(this.result);
      };
      this.imageName = file.raw.name;
      this.file = file.raw;
      this.fileName =
        file.raw.name.slice(0, file.raw.name.length - 4) + this.userId.userId;
    },

    uploadFile: function () {
      let param = new FormData();
      param.append("file", this.file);
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
              imageName: this.imageName.slice(0, this.imageName.length - 4),
              userId: this.userId.userId,
              tag: "Show",
            },
          }),
        ])
        .then(
          axios.spread(function (aliyunResponse, infoResponse) {
            that.$message({
              message: "上传图像成功",
              type: "success",
            });
            that.reload();
          })
        );
    },

    deleteImg: function (item) {
      //删除图片信息和图片地址
      var that = this;
      axios
        .all([
          axios({
            headers: {
              Authorization: this.print.Authorization,
              "Content-Type": "multipart/form-data",
            },
            method: "delete",
            url: "http://kana.chat:70/common/aliyun",
            params: {
              fileurl: item.url,
            },
          }),
          axios({
            headers: {
              Authorization: this.print.Authorization,
              "Content-Type": "application/x-www-form-urlencoded",
            },
            method: "delete",
            url: "http://kana.chat:70/image",
            params: {
              imageId: item.imageId,
            },
          }),
        ])
        .then(
          axios.spread(function (delResponse1, delResponse2) {
            that.$message({
              message: "删除图片成功",
              type: "success",
            });

            that.imageList = that.imageList.filter(
              (image) => image.imageId != item.imageId
            );
          })
        );
    },

    handleClick(tab) {
      this.imageList = [];
      var that = this;
      if (tab.name == "all") {
        //获取全部图片
        axios({
          method: "get",
          url: "http://kana.chat:70/image/all?pageNum=&pageSize=1000000",
        }).then(function (response) {
          that.imageList = response.data.data;
          that.imageList.forEach((item) => {
            if (item.tag == "Avatar") that.$set(item, "tagUTF", "头像");
            else if (item.tag == "Show") that.$set(item, "tagUTF", "展示");
            else if (item.tag == "Exam") that.$set(item, "tagUTF", "考试");

            that.otherUser = that.userList.filter(
              (user) => user.userId == item.userId
            );
            that.$set(item, "username", that.otherUser[0].userName);
          });
        });
      } else {
        //获得分类图片
        axios({
          method: "get",
          url: "http://kana.chat:70/image/tag?tag=" + tab.name,
        }).then(function (response) {
          that.imageList = response.data.data;
          that.imageList.forEach((item) => {
            if (item.tag == "Avatar") that.$set(item, "tagUTF", "头像");
            else if (item.tag == "Show") that.$set(item, "tagUTF", "展示");
            else if (item.tag == "Exam") that.$set(item, "tagUTF", "考试");

            that.otherUser = that.userList.filter(
              (user) => user.userId == item.userId
            );
            that.$set(item, "username", that.otherUser[0].userName);
          });
        });
      }
    },
  },
};
</script>

