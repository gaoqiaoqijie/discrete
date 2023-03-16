package com.lc.ucenter.service.impl;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lc.common.exception.TrainingException;
import com.lc.common.utils.JwtUtils;
import com.lc.ucenter.entity.User;
import com.lc.ucenter.entity.vo.PasswordVo;
import com.lc.ucenter.entity.vo.RegisterVo;
import com.lc.ucenter.entity.vo.UserQuery;
import com.lc.ucenter.mapper.UserMapper;
import com.lc.ucenter.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lc
 * @since 2021-03-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Value("${default-setting.avatar}")
    private String avatar;

    @Value("${default-setting.mail.count-left}")
    private String leftCount;

    @Value("${default-setting.mail.count-right}")
    private String rightCount;

    @Value("${default-setting.mail.from}")
    private String from;

    @Value("${default-setting.mail.username}")
    private String username;

    @Value("${default-setting.mail.subject}")
    private String subject;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public String sendCode(String mail) {
        System.out.println("目标邮件地址：" + mail);
        // 生成验证码
        String code = generateCode();
        // 设置正文
        String content = leftCount + code + rightCount;
        try {
            //创建邮件  SimpleMailMessage是创建文件 包括内容，邮件消息，发件人，收件人……
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(new InternetAddress(MimeUtility.encodeText(username)+from).toString());
            message.setTo(mail);
            message.setSubject(subject);
            //把验证码存入resdis中，设置过期时间为三分钟，不需要可忽略
            redisTemplate.opsForValue().set(mail,code,5, TimeUnit.MINUTES);
            //setText 就是设置邮件发送的内容，根据自己的需要自行编写即可。
            message.setText(content);

            //发送邮件
            mailSender.send(message);
//            MailUtil.send(mailAccount,mail,subject,content,false);
            /*System.out.println("mail参数" + mail + subject + content);
            MailUtil.send(mail, subject, content, false);
            redisTemplate.opsForValue().set(mail, code, 5, TimeUnit.MINUTES);
            System.out.println("发送完成！" + "目标邮件地址：" + mail);*/
        } catch (Exception e) {
            e.printStackTrace();
            throw new TrainingException(500, "请检查邮箱格式是否正确！");
        }
        return code;
    }

    @Override
    public boolean register(RegisterVo registerVo) {
        String mail = registerVo.getMail();
        String password = registerVo.getPassword();
        String username = registerVo.getUsername();
        String code = registerVo.getCode();

        if (StringUtils.isEmpty(mail) || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(username) || StringUtils.isEmpty(code)) {
            throw new TrainingException(500, "注册失败，请填写完整注册信息！");
        }

        // 检查邮箱是否重复
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("mail", mail);
        Integer count = baseMapper.selectCount(wrapper);
        if (count >= 1) {
            throw new TrainingException(500, "注册失败，邮箱重复！");
        }

        // 检查昵称是否重复
        QueryWrapper<User> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("username", username);
        Integer count1 = baseMapper.selectCount(wrapper1);
        if (count1 >= 1) {
            throw new TrainingException(500, "注册失败，用户名重复！");
        }
        // 检查验证码
        String redisCode = redisTemplate.opsForValue().get(mail);
        if (!code.equals(redisCode)) {
            throw new TrainingException(500, "注册失败，验证码错误！");
        }

        // 其他情况正常注册
        User user = new User();
        user.setMail(mail);
        user.setUsername(username);
        user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        user.setAvatar(avatar);

        int line = baseMapper.insert(user);
        return line == 1;
    }

    @Override
    public Map<String, Object> login(String username, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        User user = baseMapper.selectOne(wrapper);
        // 是否可以成功查询到数据
        if (null == user) {
            throw new TrainingException(500, "用户名或密码错误！");
        }
        // 校验密码
        if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
            throw new TrainingException(500, "用户名或密码错误！");
        }
        // 通过重重检测，可以给token了
        Map<String, Object> map = new HashMap<>();
//        if (user.getPermission() == 1) {
            map.put("token", JwtUtils.getJwtToken(user.getId(), user.getUsername()));
//        } else {
//            map.put("userToken", JwtUtils.getJwtToken(user.getId(), user.getUsername()));
//            map.put("userToken", "tokentest");
//        }
        return map;
    }

    @Override
    public User getInfo(String userId) {
        return baseMapper.selectById(userId);
    }

    @Override
    public boolean updatePassword(PasswordVo passwordVo) {
        // 查出原来的密码
        User user = baseMapper.selectById(passwordVo.getId());
        // 旧密码输入错误的话就返回错误
        if (!user.getPassword().equals(DigestUtils.md5DigestAsHex(passwordVo.getOldPassword().getBytes()))) {
            throw new TrainingException(500, "原密码输入错误");
        }
        // 通过执行更新
        user.setPassword(DigestUtils.md5DigestAsHex(passwordVo.getNewPassword().getBytes()));
        int line = baseMapper.updateById(user);
        return line > 0;
    }

    /**
     * 获取用户分页信息
     *
     * @param userPage  page
     * @param userQuery 查询条件
     */
    @Override
    public void pageQuery(Page<User> userPage, UserQuery userQuery) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (userQuery != null) {
            String name = userQuery.getName();
            String begin = userQuery.getBegin();
            String end = userQuery.getEnd();
            if (org.apache.commons.lang.StringUtils.isNotEmpty(name)) {
                wrapper.like("username", name);
            }
            if (org.apache.commons.lang.StringUtils.isNotEmpty(begin)) {
                wrapper.ge("create_time", begin);
            }
            if (StringUtils.isNotEmpty(end)) {
                wrapper.le("create_time", end);
            }
        }
        baseMapper.selectPage(userPage, wrapper);
    }

    @Override
    public boolean resetPwd(String id) {
        User user = baseMapper.selectById(id);
        if (user == null) {
            return false;
        }
        user.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        int i = baseMapper.updateById(user);
        return i == 1;
    }

    @Override
    public boolean ebableById(String id) {
        User user = baseMapper.selectById(id);
        if (user == null) {
            return false;
        }
        user.setDelFlag(0);
        int i = baseMapper.updateById(user);
        return i == 1;
    }

    @Override
    public List<User> getAllTeacher() {
        QueryWrapper<User> wapper = new QueryWrapper<>();
        wapper.eq("permission", 1);
        List<User> teacher = baseMapper.selectList(wapper);
        return teacher;
    }

    /**
     * 生成四位数验证码
     *
     * @return 四位数验证码
     */
    private String generateCode() {
        String source = "1234567890";
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            int index = random.nextInt(source.length());
            // 直接charAt方法也是O(1)，因为String底层是维护着一个final的value数组
            result.append(source.charAt(index));
        }
        return result.toString();
    }
}
