import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import usercenter from '../components/user/usercenter'
/* usercenter子路由 */
/* 普通用户 */
import personalAccount from '../components/user/personalPages/personalAccount'
import personalImformation from '../components/user/personalPages/personalImformation'
import personalProgram from '../components/user/personalPages/personalProgram'
import personalNotice from '../components/user/personalPages/personalNotice'
/* 消息子路由*/
import examDetailNotice from '../components/user/personalPages/notice/examDetailNotice'
import examRegistration from '../components/user/personalPages/notice/examRegistration'
import examResultNotice from '../components/user/personalPages/notice/examResultNotice'
/* 教师 */
import managerTestType from '../components/user/managerPages/managerTestType'
import managerChannel from '../components/user/managerPages/managerChannel'
import managerGetUserInfo from '../components/user/managerPages/managerGetUserInfo'
/* 考试管理子路由 */
import managerExam from '../components/user/managerPages/managerTest/managerExam'
import registrationRelease from '../components/user/managerPages/managerTest/registrationRelease'
import managerScore from '../components/user/managerPages/managerTest/managerScore'
/* 报名子路由 */
import getRegistration from '../components/user/managerPages/managerTest/registration/getRegistration'
import setRegistration from '../components/user/managerPages/managerTest/registration/setRegistration'
import fileRegistration from '../components/user/managerPages/managerTest/registration/fileRegistration'
/* 用户管理子路由 */
import managerGetUserInfoList from '../components/user/managerPages/managerUser/managerGetUserInfoList'
import managerGetUserClassList from '../components/user/managerPages/managerUser/managerGetUserClassList'
/* 管理员 */
import adminHomepage from '../components/user/adminPages/adminHomepage'
import adminChangeRole from '../components/user/adminPages/adminChangeRole'
import adminGetLog from '../components/user/adminPages/adminGetLog'
/* 主页菜单子路由 */
import homepage from '../components/public/homepage'
import publicGetChannel from '../components/public/publicGetChannel'
import publicGetExam from '../components/public/publicGetExam'
import takeinExam from '../components/public/takeinExam'
/* 主页消息页面 */
import homepagemessage from '../components/public/message/homepagemessage'
/* 登陆注册 */
import login from '../components/user/login'
import register from '../components/user/register'
/* 登陆跳转中间页
  url不一致无法做，暂时放弃
*/
import authorize from '../components/user/authorize.vue'

// 解决ElementUI导航栏中的vue-router在3.0版本以上重复点菜单报错问题
const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}
Vue.use(Router)

const router = new Router({
  routes: [
    {
      path: '/',
      name: 'HelloWorld',
      component: HelloWorld,
      children: [
        {
          path: '/usercenter',
          name: 'usercenter',
          component: usercenter,
          children: [
            {
              path: 'personalAccount',
              name: 'personalAccount',
              component: personalAccount
            },
            {
              path: 'personalImformation',
              name: 'personalImformation',
              component: personalImformation
            },
            {
              path: 'personalProgram',
              name: 'personalProgram',
              component: personalProgram
            },
            {
              path: 'personalNotice',
              name: 'personalNotice',
              component: personalNotice,
              children: [
                {
                  path: 'examDetailNotice',
                  name: 'examDetailNotice',
                  component: examDetailNotice,
                },
                {
                  path: 'examRegistration',
                  name: 'examRegistration',
                  component: examRegistration,
                },
                {
                  path: 'examResultNotice',
                  name: 'examResultNotice',
                  component: examResultNotice,
                },
              ]
            },
            {
              path: 'adminHomepage',
              name: 'adminHomepage',
              component: adminHomepage
            },
            {
              path: 'adminChangeRole',
              name: 'adminChangeRole',
              component: adminChangeRole
            },
            {
              path: 'managerGetUserInfo',
              name: 'managerGetUserInfo',
              component: managerGetUserInfo,
              children: [
                {
                  path: 'managerGetUserInfoList',
                  name: 'managerGetUserInfoList',
                  component: managerGetUserInfoList
                },
                {
                  path: 'managerGetUserClassList',
                  name: 'managerGetUserClassList',
                  component: managerGetUserClassList
                },
              ]
            },
            {
              path: 'managerTestType',
              name: 'managerTestType',
              component: managerTestType,
              children: [
                {
                  path: 'managerExam',
                  name: 'managerExam',
                  component: managerExam,
                },
                {
                  path: 'registrationRelease',
                  name: 'registrationRelease',
                  component: registrationRelease,
                  children: [
                    {
                      path: 'getRegistration',
                      name: 'getRegistration',
                      component: getRegistration,
                    },
                    {
                      path: 'setRegistration',
                      name: 'setRegistration',
                      component: setRegistration,
                    },
                    {
                      path: 'fileRegistration',
                      name: 'fileRegistration',
                      component: fileRegistration,
                    },
                  ]
                },
                {
                  path: 'managerScore',
                  name: 'managerScore',
                  component: managerScore,
                },
              ],
            },
            {
              path: 'managerChannel',
              name: 'managerChannel',
              component: managerChannel
            },
            {
              path: 'adminGetLog',
              name: 'adminGetLog',
              component: adminGetLog
            },
          ]
        },
        {
          path: '/homepage',
          name: 'homepage',
          component: homepage
        },
        {
          path: '/homepagemessage',
          name: 'homepagemessage',
          component: homepagemessage
        },
        {
          path: '/publicGetChannel',
          name: 'publicGetChannel',
          component: publicGetChannel
        },
        {
          path: '/publicGetExam',
          name: 'publicGetExam',
          component: publicGetExam
        },
        {
          path: '/takeinExam',
          name: 'takeinExam',
          component: takeinExam
        },
      ]
    },
    {
      path: '/login',
      name: 'login',
      component: login
    },
    {
      path: '/register',
      name: 'register',
      component: register
    },
    {
      path: '/authorize',
      name: 'authorize',
      component: authorize
    },
  ]
})

//路由守卫
router.beforeEach((to, from, next) => {
  if (to == "/" || from == "/")
    next({
      path: "homepage"
    })
  // console.log(to)
  // console.log(from)
  next();
})

export default router;