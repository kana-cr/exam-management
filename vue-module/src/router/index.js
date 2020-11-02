import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import login from '../components/user/login'
import register from '../components/user/register'
import usercenter from '../components/user/usercenter'
/* usercenter子路由 */
/* 普通用户 */
import personalAccount from '../components/user/personalPages/personalAccount'
import personalImformation from '../components/user/personalPages/personalImformation'
import personalProgram from '../components/user/personalPages/personalProgram'
import personalNotice from '../components/user/personalPages/personalNotice'
/* 消息子路由
  区分消息来源
*/
import examDetailNotice from '../components/user/personalPages/notice/examDetailNotice'
import examRegistration from '../components/user/personalPages/notice/examRegistration'
import examResultNotice from '../components/user/personalPages/notice/examResultNotice'
/* 管理员 */
import managerChangeRole from '../components/user/managerPages/managerChangeRole'
import managerGetUserInfo from '../components/user/managerPages/managerGetUserInfo'
import managerTestType from '../components/user/managerPages/managerTestType'
import managerChannel from '../components/user/managerPages/managerChannel'
import managerGetLog from '../components/user/managerPages/managerGetLog'
/* 用户管理子路由 */
import managerGetUserInfoList from '../components/user/managerPages/managerUser/managerGetUserInfoList'
import managerGetUserClassList from '../components/user/managerPages/managerUser/managerGetUserClassList'
/* 考试管理子路由 */
import managerExam from '../components/user/managerPages/managerTest/managerExam'
import registrationRelease from '../components/user/managerPages/managerTest/registrationRelease'
import managerScore from '../components/user/managerPages/managerTest/managerScore'

/* 报名子路由 */
import getRegistration from '../components/user/managerPages/managerTest/registration/getRegistration'
import setRegistration from '../components/user/managerPages/managerTest/registration/setRegistration'

/* 主页菜单子路由 */
import homepage from '../components/public/homepage'
import publicGetChannel from '../components/public/publicGetChannel'
import publicGetExam from '../components/public/publicGetExam'
import takeinExam from '../components/public/takeinExam'
/* 测试页面 */
import test from '../components/test'

Vue.use(Router)

export default new Router({
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
              path: 'managerChangeRole',
              name: 'managerChangeRole',
              component: managerChangeRole
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
              path: 'managerGetLog',
              name: 'managerGetLog',
              component: managerGetLog
            },
          ]
        },
        {
          path: '/homepage',
          name: 'homepage',
          component: homepage
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
      path: '/test',
      name: 'test',
      component: test,
    },
  ]
})
