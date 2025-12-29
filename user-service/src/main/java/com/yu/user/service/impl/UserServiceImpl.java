package com.yu.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yu.api.client.MemberClient;
import com.yu.api.client.OrderClient;
import com.yu.api.client.PayClient;
import com.yu.api.client.UserClient;
import com.yu.api.enums.OrderStatus;
import com.yu.api.po.Order;
import com.yu.api.po.PayOrder;
import com.yu.api.vo.MemberVO;
import com.yu.common.domain.AjaxResult;
import com.yu.common.exception.BusinessException;
import com.yu.common.utils.BeanUtils;
import com.yu.common.utils.CollUtils;
import com.yu.common.utils.UserContext;
import com.yu.user.config.JwtProperties;
import com.yu.user.domain.dto.*;
import com.yu.user.domain.po.User;
import com.yu.user.domain.vo.UserLoginVO;
import com.yu.user.domain.vo.UserRegisterVO;
import com.yu.user.domain.vo.UserVO;
import com.yu.user.enums.MemberLevel;
import com.yu.user.enums.UserStatus;
import com.yu.user.mapper.UserMapper;
import com.yu.user.service.IUserService;
import com.yu.user.utils.JwtTool;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.cli.Digest;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final JwtTool jwtTool;

    private final JwtProperties jwtProperties;

    private final FileStorageService fileStorageService;

    private final MemberClient memberClient;

    private final PayClient payClient;

    private final OrderClient orderClient;



    @Override
    public UserLoginVO login(LoginFormDTO loginFormDTO) {
        String username = loginFormDTO.getUsername();
        String pw = loginFormDTO.getPassword();
        String password = DigestUtils.md5DigestAsHex(pw.getBytes());
        User user = lambdaQuery().eq(User::getUsername, username).one();
        Assert.notNull(user, "用户不存在");

        Assert.isTrue(BCrypt.checkpw(password, user.getPassword()), "密码错误");
        if(user.getStatus() != UserStatus.NORMAL){
            throw new RuntimeException("用户被禁用");
        }
        String token = jwtTool.createToken(user.getId(), jwtProperties.getTokenTTL());
        log.debug("生成token:{}", token);
        AjaxResult<List<MemberVO>> memberVOList = memberClient.listByMember();

        List<MemberVO> data = memberVOList.getData();
        if(CollUtils.isEmpty(data)){
            log.error("会员服务数据异常,无法查询到所有会员");
            throw new RuntimeException("会员服务数据异常,无法查询到所有会员");
        }
        for (MemberVO datum : data) {
            if(datum.getMinPoints() <= user.getCurrentPoints() && datum.getMaxPoints() >= user.getCurrentPoints()){
               lambdaUpdate().eq(User::getId, user.getId())
                       .set(StrUtil.isNotBlank(datum.getLevelName()),User::getLevelName, datum.getLevelName())
                             .set(datum.getId() != null,User::getMemberLevelId, datum.getId())
                             .update();
            }
        }


        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO
                .setToken(token)
                .setAvatar(user.getAvatar())
                .setNickName(user.getNickName())
                .setUserId(user.getId())
                .setBalance(user.getBalance())
                .setCurrentPoints(user.getCurrentPoints())
                .setLevelName(user.getLevelName());

        return userLoginVO;
    }

    @Override
    @Transactional
    public UserRegisterVO register(RegisterFormDTO registerFormDTO) {
        String username = registerFormDTO.getUsername();
        String pw = registerFormDTO.getPassword();
        String phone = registerFormDTO.getPhone();
        String password = DigestUtils.md5DigestAsHex(pw.getBytes());
        String nickName = registerFormDTO.getNickName();
        String hashPassword = BCrypt.hashpw( password, BCrypt.gensalt());
        User user  = new User()
                .setUsername(username)
                .setPassword(hashPassword)
                .setPhone(phone)
                .setStatus(UserStatus.NORMAL)
                .setLevelName("普通会员")
                .setAvatar(registerFormDTO.getAvatar())
                .setNickName(nickName)
                .setBalance(0L)
                .setMemberLevelId(MemberLevel.GENERAL.getId())
                .setCurrentPoints(0)
                .setPayPassword(hashPassword);

        save(user);
        log.info("用户注册成功：{}", user);
        User u = lambdaQuery().eq(User::getUsername, username).one();
        UserRegisterVO userRegisterVO = new UserRegisterVO();
        userRegisterVO.setUserId(u.getId());
        userRegisterVO.setBalance(u.getBalance());
         userRegisterVO.setUsername(u.getUsername());
         userRegisterVO.setAvatar(u.getAvatar());
         userRegisterVO.setNickName(u.getNickName());
         userRegisterVO.setLevelName(u.getLevelName());
        userRegisterVO.setToken(jwtTool.createToken(u.getId(), jwtProperties.getTokenTTL()));

        return userRegisterVO;
    }

        @Override
        @GlobalTransactional(rollbackFor = Exception.class, name = "yu-mall-user-service")
        public void deductMoney(DeductLocalMoneyDTO deductLocalMoneyDTO) {
            Long userId = deductLocalMoneyDTO.getUserId();
            User user = getById(userId);
            if(user == null){
                log.error("用户不存在");
                throw new RuntimeException("用户不存在");
            }
            if(user.getStatus() == UserStatus.FROZEN){
                log.error("用户被冻结");
                throw new RuntimeException("用户被冻结");
            }
            String pw = DigestUtils.md5DigestAsHex(deductLocalMoneyDTO.getPayPassword().getBytes());
            if(!BCrypt.checkpw(pw, user.getPassword())){
                log.error("支付密码错误");
                throw new RuntimeException("支付密码错误");
            }
            AjaxResult<MemberVO> memberVOData = memberClient.getMemberById(user.getMemberLevelId());
            if(!memberVOData.isSuccess() || memberVOData.getData() == null){
                log.error("会员服务异常");
                throw new RuntimeException("会员服务异常");
            }
            if(deductLocalMoneyDTO.getOrderBizId() == null){
                log.error("订单用户扣减余额服务id为null");
                throw new RuntimeException("订单用户扣减余额服务id为null");
            }
            AjaxResult<Order> byId = orderClient.getById(deductLocalMoneyDTO.getOrderBizId());
            Order data = byId.getData();
            if(data == null || !byId.isSuccess() || OrderStatus.of(data.getStatus()) != OrderStatus.UNPAID){
                log.warn("提前校验拦截：订单 {}的状态为{}, 跳过处理", deductLocalMoneyDTO.getOrderBizId(), OrderStatus.of(data.getStatus()) == null ? "未知" : OrderStatus.of(data.getStatus()));
                return;
            }
            AjaxResult<PayOrder> payOrderByOrderId = payClient.getPayOrderByOrderId(deductLocalMoneyDTO.getOrderBizId());
            if(payOrderByOrderId.getData() != null && payOrderByOrderId.isSuccess() && OrderStatus.UNPAID == OrderStatus.of(payOrderByOrderId.getData().getStatus())){
               log.warn("该订单流水已经存在");
               throw new BusinessException("该订单流水已经存在,请勿重复提交");
            }
            MemberVO memberVO = memberVOData.getData();
            BigDecimal discountRate = memberVO.getDiscountRate();
            Long amount = deductLocalMoneyDTO.getAmount();
            BigDecimal amountBd = new BigDecimal(amount);
            BigDecimal discountedBd = amountBd.multiply(discountRate);
            amount = discountedBd.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
            if(user.getBalance() < amount){
                log.error("余额不足");
                throw new BusinessException("余额不足");
            }
            boolean update = lambdaUpdate().eq(User::getId, userId)
                    .setSql(amount != null,"balance = balance -"+ amount)
                    .setSql(amount != null, "current_points = current_points +" + amount).update();
           if(!update){
               log.error("更新用户余额失败");
               throw new RuntimeException("更新用户余额失败");
           }
        }


    @Override
    public boolean updateUserInfo(Long userId, UserBasicInfoDTO userBasicInfoDTO) {
        Long user = UserContext.getUser();
        if(user == null){
            log.error("用户未登录");
            throw new RuntimeException("用户未登录,请检查登录状态");
        }
        boolean update = lambdaUpdate()
                .eq(User::getId, user)
                .set(StrUtil.isNotBlank(userBasicInfoDTO.getNickName()), User::getNickName, userBasicInfoDTO.getNickName())
                .set(StrUtil.isNotBlank(userBasicInfoDTO.getAvatar()), User::getAvatar, userBasicInfoDTO.getAvatar())
                .set(StrUtil.isNotBlank(userBasicInfoDTO.getPhone()), User::getPhone, userBasicInfoDTO.getPhone())
                .set(StrUtil.isNotBlank(userBasicInfoDTO.getEmail()), User::getEmail, userBasicInfoDTO.getEmail())
                .set(userBasicInfoDTO.getGender() != null, User::getGender, userBasicInfoDTO.getGender())
                .set(userBasicInfoDTO.getBirthday() != null, User::getBirthday, userBasicInfoDTO.getBirthday())
                .update();
        return update;
    }


    @Override
    public boolean updatePassword(PasswordDTO passwordDTO) {
        Long userId = UserContext.getUser();
        if(userId == null){
            log.error("用户未登录");
            throw new RuntimeException("用户未登录,请检查登录状态");
        }
        User one = lambdaQuery().eq(User::getId, userId).one();
        if(one != null && one.getPhone() != null){
            if(!one.getPhone().equals(passwordDTO.getPhone())){
                log.warn("手机号不一致");
                throw new RuntimeException("手机号不一致");
            }
        }
        String pw = DigestUtils.md5DigestAsHex(passwordDTO.getOldPassword().getBytes());
        if(!pw.equals(one.getPassword())){
            log.warn("旧密码错误");
            throw new RuntimeException("旧密码错误");
        }
        if (!passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())) {
            log.warn("密码不一致");
            throw new RuntimeException("密码不一致");
        }
        String newPw = DigestUtils.md5DigestAsHex(passwordDTO.getConfirmPassword().getBytes());
        boolean update = lambdaUpdate().eq(User::getId, userId)
                .set(User::getPassword, newPw)
                .update();
        return update;
    }


    @Override
    public UserVO getUserInfo() {
        Long user = UserContext.getUser();
        if(user == null){
            log.error("用户未登录");
            throw new RuntimeException("用户未登录,请检查登录状态");
        }
        User one = lambdaQuery().eq(User::getId, user).one();
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(one, userVO, CopyOptions.create().setIgnoreCase( true).setIgnoreProperties("id"));
        userVO.setUserId(one.getId());
        userVO.setPhone(one.getPhone().substring(0,3) + "****");
        return userVO;

    }
}
