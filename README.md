# exam-management
考试管理系统

## 项目背景及要求
某学校需要开发完成一个考试管理系统应用对对外的职称考试；
主要包括三大块，考务中心、网站门户、网站系统管理。

在教务中心里要实现，考点管理、场次管理、报名管理、成绩管理等

在网站门户里要实现，考试新闻、网上报名、成绩查询、个人信息维护等

在网站系统管理中主要是实现信息发布、用户及权限管理、日志管理等

该系统同时在线人数为3000人，操作时的反应速度不得低于3秒，并且要考虑到敏感数据的安全性。
> 建议技术方案：B/S结构（即网站）+微信服务号（可选 ，个人端）

![图片.png](https://kana-bucket.oss-cn-beijing.aliyuncs.com/%E5%9B%BE%E7%89%87_1601885201534.png)

注: 部分架构设计参考项目:haetae (beta)

## 该实训项目采用前后端分离开发。

### 技术栈&运行环境  

```
maven
Java 1.8
Spring boot
Mysql
Redis
Vue
```
### 开发人员
> 参与开发人员，排名不分先后  

1. [dgxgefg1.hx](https://github.com/dgxgefg1)
2. [hjy200022.hjy](https://github.com/hjy200022)
3. [j2471571477.jcb](https://github.com/j2471571477)  

### 目录结构
```
|-- exam-service  （业务层）
|-- exam-core  (独立的业务领域核心)
    |-- user        (用户领域核心)
    |-- message    (消息领域核心)
    |-- common   	(通用领域核心)
    |-- exam      (考试领域核心)
|-- exam-security  (服务安全层)    
|-- exam-web   (接口交互层)
|-- util  (通用工具模块 -- 来自Beta的二方库)

```
> 业务码和模块划分暂不列出

### 界面展示

#### 主页
![图片1.png](https://kana-bucket.oss-cn-beijing.aliyuncs.com/图片_1605856514231.png)

#### 登录页面/邮箱找回密码
![图片2.png](https://kana-bucket.oss-cn-beijing.aliyuncs.com/图片_1605856717366.png)

#### 个人信息
![图片3.png](https://kana-bucket.oss-cn-beijing.aliyuncs.com/图片_1605856938638.png)

#### 用户信息
![图片4.png](https://kana-bucket.oss-cn-beijing.aliyuncs.com/图片_1605856947774.png)

#### 考试报名
![图片5.png](https://kana-bucket.oss-cn-beijing.aliyuncs.com/图片_1605856956500.png)

#### 图片管理
![图片6.png](https://kana-bucket.oss-cn-beijing.aliyuncs.com/图片_1605857177957.png)

### 数据库展示
![图片7.png](https://kana-bucket.oss-cn-beijing.aliyuncs.com/图片_1605856276349.png)


