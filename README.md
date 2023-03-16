### 项目介绍

#### 背景介绍
毕业设计

毕设原型：根据b站视频教程所写，链接：https://www.bilibili.com/video/BV1dQ4y1A75e

基于https://github.com/hczs/training 进行二次开发



项目前台前端代码地址：https://github.com/gaoqiaoqijie/discrete_front

项目后台管理前端代码地址：https://github.com/gaoqiaoqijie/discrete_admin


#### 开发环境

jdk1.8+MySQL5.7+maven3.6.3

#### 技术栈

SpringCloud、Nacos、SpringBoot、Redis、MyBatis-Plus、Vue、element-ui、Nuxt

#### 主要功能介绍

1. 在线视频观看：播放进度记录、学习时长记录、学习课程记录
2. 培训计划：后台规划计划课程和学习人员，相关学习人员学习计划课程并总结，后台评分
3. 问答：简单的提问回答模块，回答实现楼中楼回复，问题按照最新提问和回答来排序
4. 热门课程：按照学习人数和点击次数来排序
5. 个人中心：我学习的课程、个人资料修改、我的提问、我的回答

#### 各个服务模块介绍

1. gateway：网关服务，统一入口，目前就做了统一跨域处理和swagger整合
2. service-home：首页微服务，提供首页轮播图查询
3. service-learning：学习中心，提供课程、章节相关功能
4. service-oss：与阿里云oss对接，提供文件上传功能
6. service-qa：问答相关功能
7. service-ucenter：用户中心，登录、注册相关功能
8. service-video：对接阿里云视频点播服务，提供视频上传、播放凭证获取，视频信息获取相关功能

### 安装教程

#### 需要准备什么

1. 电脑需要安装jdk1.8、mysql、maven基本环境

2. nacos注册中心，本地启动即可，nacos安装启动教程自行百度

3. 开通阿里云oss服务，并在service-oss的application.yml中修改为自己的相关配置

4. redis，可以准备云服务器或虚拟机来装一个redis，redis在本项目中有两个作用，首页优化和邮箱验证码存储

5. 邮箱，本项目使用发验证码使用了发送邮件的形式，所以需要准备一个邮箱并且开通POP3/IMAP/SMTP服务，配置单独的授权密码，然后将邮箱和授权密码填进service-ucenter服务中的resources/config/mail.setting文件中
6. 开通阿里云视频点播服务，根据官方或网上的教程创建key配置，并且配置到service-video服务的application.yml中

#### 怎么使用

1. 打开项目，下载相关依赖包到本地，并且将lib文件夹中的aliyun-java-vod-upload-1.4.14.jar单独引入到service-video中，因为maven中没有这个jar包，所以需要手动引入，然后项目基本不会报包找不到这些错误了
2. 然后创建数据库，字符集：utf8mb4 -- UTF-8 Unicode，排序规则：utf8mb4_general_ci，运行项目中的sql文件，导入相关表，表设计有不合理的地方（但是能用），请后续自行修改
3. 依次检查每个服务的配置文件，修改为你的配置


