<template>
  <div class="container">
    <el-header style="text-align: left; font-size: 30px">
      <span>{{ examDescription }}报名页面</span>
    </el-header>
    <el-main>
      <el-steps :active="active" finish-status="success" align-center>
        <el-step title="步骤 1" description="考试须知"></el-step>
        <el-step title="步骤 2" description="确定个人信息"></el-step>
        <el-step title="步骤 3" description="准考证资料"></el-step>
      </el-steps>
      <template v-if="step == 0">
        <br />
        <el-container style="border: 1px solid #eee">
          <el-header style="text-align: center; font-size: 12px">
            <h1>
              <span>考试须知</span>
            </h1>
          </el-header>

          <el-main>
            <p>
              1.考前三十分钟，考生需持带有照片的有效证件(身份证、临时身份证、护照和港澳台通行证)和自行打印的准考证进入规定的考场。
            </p>
            <p>
              2.考试期间考生可以在考桌上方的可携带物品：签字笔、铅笔、橡皮、二十四色彩笔(包含黑色、蓝色、棕色、绿色、灰色、橙色、粉色、紫色、红色、黄色)铅笔盒、塑料瓶装水、药品、纸巾和准考证。
            </p>
            <p>
              3.考试期间需放在指定区域的禁止携带物品：处于关闭状态下的手机、相机或任何其他电子产品、字典、笔记本、修正液/修正带等、纸张、书包、手提包、行李箱。
            </p>
            <p>4.考场内不得相互借用文具。严禁在考场内饮食。</p>
            <p>
              5.考生入场后，按号入座，将本人《准考证》放在课桌上，以便核验。
            </p>
            <p>6.统一开考信号发出后才能开始答题。</p>
            <p>
              7.笔试开考10分钟内，允许迟到考生进入考场参加考试，考试结束时间按照统一规定结束。开考10分钟后，禁止迟到考生入场考试。考生开考三十分钟后，方准交卷出场的，出场后不得进场续考。
            </p>
            <p>
              8.考生领到试卷后，必须先在指定位置准确，清楚地填写姓名，准考证号，座位号等栏目。
            </p>
            <p>
              9.考生遇试卷分发错误及试题字迹不清等问题可举手询问，监考人员应当众答复；涉及试题内容的疑问，不得向监考人员询问。
            </p>
            <p>
              10.考生在考场内必须保持安静，严格遵守考场纪律，不准交头接耳，左顾右盼；不准偷看，抄袭他人答案；不准夹带，冒名或换卷，对于违反考场规则，不服从监考人员管理和舞弊者按违反考场规则处理，情节严重的将取消本次考试资格或限期停考。
            </p>
            <p>
              11.考生离开考场时必须交卷，不准携带试卷离开考场。离开考场后不准在考场附近逗留和交谈。
            </p>
            <p>
              12.考试结束铃声响后考生立即停止答卷，并将试卷按顺序整理好放在桌上，待监考人员允许后方可离场。
            </p>
            <p>13.笔试结束后，考生应服从监考人员的管理，在口试候考室</p>
            <p>
              14.等待口试考试。口试完毕后，考生应迅速离开口试室，不得与候考室考生接触。
            </p>
            <p>
              15.考生应自觉服从监考人员管理，任何人不得以任何理由妨碍监考人员进行正常工作。监考人员有权对考场内发生的问题，按规定做出处理。
            </p>
          </el-main>
          <el-footer style="text-align: center; font-size: 12px">
            <el-checkbox v-model="ifRead">已阅读</el-checkbox>
          </el-footer>
        </el-container>
      </template>
      <template v-if="step == 1">
        <el-form :model="personInfo" class="demo-ruleForm">
          <fieldset disabled>
            <div class="form-group">
              <el-form-item prop="realName"
                >姓名
                <el-input
                  type="text"
                  autocomplete="off"
                  v-model="personInfo.realName"
              /></el-form-item>
            </div>
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
        </el-form>
      </template>
      <template v-if="step == 2">
        <el-container>
          <table
          
            border="3"
            width="95%"
            cellspacing="0"
            style="margin: 20px auto"
          >
            <tr id="juzhong" height="100">
              <td colspan="2">
                <h1>准考证</h1>
              </td>
            </tr>
            <tr height="200">
              <td width="841">
                考生姓名： {{ personInfo.realName }}<br />
                准考证号： 待确认<br />
                学号： {{ personInfo.stuNo }}<br />
                身份证号： {{ personInfo.identificationNumber }}<br />
                考试科目： {{ examDescription }}<br />
                考生学校： 杭电信工<br />
                考试地点: 杭州电子科技大学信息工程学院<br />
                专 业： {{ personInfo.major }}<br />
                专业班级：{{ personInfo.className }}
              </td>
              <td style="border-style: none; border-wigth: 0px">
                <img
                  :src="imageUrl"
                  width="170"
                  height="200"
                />
              </td>
            </tr>
            <tr id="juzhong" height="400">
              <td colspan="2">
                <p>考试科目及时间</p>
                <table border="1" width="100%" cellspacing="0">
                  <tr id="juzhong">
                    <td>考试科目</td>
                    <td>考试开始时间</td>
                    <td>考试结束时间</td>
                    <td>考场号</td>
                  </tr>
                  <tr id="juzhong">
                    <td>{{ examDescription }}</td>
                    <td>略</td>
                    <td>略</td>
                    <td>等待教师选择</td>
                  </tr>
                </table>
                <br />
                <p>考生须知</p>
                <br />
                <div id="juzuo">
                  1、考生应考时，上午下午均应携带黑或黑色墨水笔。严禁将书籍、笔记、移动电话、电子记事本等电子设备带至考场，一经发现，按违纪处理。<br />
                  2、考试前30分钟进入考场，监考人员核对准考证（包括电子准考证）、身份证后，学生对号入座将身份证放在桌角上备查（不携带身份证不容许参加考试）。<br />
                  3、考试开始后15分钟，迟到的考生不得进入考场参加考试。考生不得提前交卷。<br />
                  4、严禁将试卷、答题卡（纸）带出考场。<br /><br />
                  <p>提示：请考生在考试前一天熟悉考点地址和交通路线。</p>
                </div>
                <br />

                <br />
              </td>
            </tr>
          </table>
        </el-container>
      </template>
      <template v-if="step == 3"> 已完成所有步骤，请点击完成报名 </template>
    </el-main>
    <el-footer style="text-align: center">
      <template v-if="active < 3">
        <el-button style="margin-top: 12px" @click="prev">上一步</el-button>
        <el-button style="margin-top: 12px" @click="next">下一步</el-button>
      </template>
      <template v-else>
        <el-button style="margin-top: 12px" @click="finishRegistration"
          >完成报名</el-button
        >
      </template>
    </el-footer>
  </div>
</template>

<script>
import axios from "axios";
import { mapState, mapActions } from "vuex";
export default {
  name: "takeinExam",
  data() {
    return {
      examEntryId: this.$route.params.examEntryId,
      //显示标题用
      examDescription: this.$route.params.examDescription,
      //判断最后报名时人数是否空缺
      number: this.$route.params.number,
      //选择考场用
      examDetailId: this.$route.params.examDetailId,
      //个人信息表
      personInfo: {
        realName: "",
        stuNo: "",
        major: "",
        className: "",
        identificationNumber: "",
      },
      active: 0,
      step: 0,
      //获取头像
      imageFile: {},
      //图片地址
      imageUrl: "",
      //阅读须知
      ifRead: false,
      //考场表单
      locationForm: {
        examDetailId: "",
        location: "",
        userId: "",
        teacher: "",
        userEntryId: "",
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
    var that = this;
    this.checkIfReg();
    setTimeout(function () {}, 200);
  },
  methods: {
    prev() {
      if (this.active == 0) {
        this.$router.go(-1);
      } else {
        this.active--;
        this.checkStep(this.active);
      }
    },
    next() {
      if (this.ifRead == false) {
        this.$message({
          message: "请阅读考试须知",
          type: "warning",
        });
      } else {
        this.active++;
      }
      this.checkStep(this.active);
    },

    checkIfReg: function () {
      var that = this;
      if (this.number > 1000) {
        alert("人数大于1000的考试无法获得信息，且确认自己是否已经报名！");
      }
      axios({
        headers: { Authorization: this.print.Authorization },
        method: "get",
        url:
          "/api/userExamEntry/user?userId=" + this.userId.userId,
      }).then(
        function (reponse) {
          for (var i = 0; i < reponse.data.data.length; i++) {
            if (reponse.data.data[i].examEntryId == that.examEntryId) {
              that.$message({
                message: "已经报名该考试，无法再次报名",
                type: "warning",
              });
              that.$router.push({ name: "publicGetExam" });
            }
          }
        },
        function (err) {
          that.$message.error("获取用户报名信息失败");
        }
      );
    },

    checkStep: function (active) {
      var that = this;
      if (active == 0) {
        this.step = 0;
        //显示个人须知
        //判断是否报名过
      } else if (active == 1) {
        this.step = 1;
        //显示个人信息
        axios({
          headers: {
            Authorization: this.print.Authorization,
          },
          method: "get",
          url: "/api/userInfo?username=" + this.print.username,
        }).then(
          function (reponse) {
            that.personInfo = reponse.data.data;
            that.$message({
              message: "请核对个人信息",
              type: "info",
            });
            var _that = that;
            //获取考试头像
            axios({
              method: "get",
              url:
                "/api/image/user?userId=" + that.userId.userId,
            }).then(function (response) {
              _that.imageFile = response.data.data;
              if (_that.imageFile.length == 0) {
                _that.imageUrl = "";
              } else {
                _that.imageFile.forEach((item) => {
                  if (item.tag == "Exam") _that.imageUrl = item.url;
                  // _that.personInfo
                });
              }
            });
          },
          function (err) {
            that.$message.error("你没有信息，无法进行报名，跳转个人中心填写");
            that.$router.push({
              name: "personalImformation",
            });
          }
        );
      } else if (active == 2) {
        //报名
        this.takeinExam();
        this.step = 2;
      } else if (active == 3) {
        //打印准考证
        //选择考场，点击下一步后调
        this.step = 3;
      }
    },

    takeinExam: function () {
      //userid
      //examEntryId
      //number
      var that = this;
      if (this.number <= 1000) {
        //再检查人数是否空缺
        axios({
          headers: { Authorization: this.print.Authorization },
          method: "get",
          url:
            "/api/userExamEntry/remain?examEntryId=" +
            this.examEntryId,
        }).then(function (reponse) {
          if (reponse.data.data == 0) {
            that.$message.error("报名人数已满");
            that.$router.push({ name: "publicGetExam" });
          }
        });
        axios({
          headers: { Authorization: this.print.Authorization },
          method: "post",
          url: "/api/userExamEntry",
          params: {
            examEntryId: this.examEntryId,
            userId: this.userId.userId,
          },
        }).then(
          function (reponse) {
            that.$message({
              message: "报名成功，请选择最后考场位置",
              type: "success",
            });
          },
          function (err) {
            that.$message.error("报名失败，请重新尝试");
          }
        );
      } else if (this.number > 1000) {
        axios({
          headers: { Authorization: this.print.Authorization },
          method: "get",
          url:
            "/api/userExamEntry/cache/remain?examEntryId=" +
            this.examEntryId,
        }).then(function (reponse) {
          if (reponse.data.data == 0) {
            that.$message.error("报名人数已满");
            that.$router.push({ name: "publicGetExam" });
          }
        });
        axios({
          headers: { Authorization: this.print.Authorization },
          method: "post",
          url: "/api/userExamEntry/cache",
          params: {
            examEntryId: this.examEntryId,
            userId: this.userId.userId,
          },
        }).then(
          function (reponse) {
            that.$message({
              message: "报名成功，请选择最后考场位置",
              type: "success",
            });
          },
          function (err) {
            that.$message.error("报名失败，请重新尝试");
          }
        );
      }
    },

    finishRegistration: function () {
      this.$notify({
        title: "报名成功",
        message: "请记住考试信息，忘记可前往个人中心个人通知的考试报名中查询",
        duration: 0,
      });
      this.$router.push({ name: "homepage" });
    },
  },
};
</script>