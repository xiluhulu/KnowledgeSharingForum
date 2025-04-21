package com.fgh.www.controller.user;


import com.fgh.www.common.util.Jwt_Utils;
import com.fgh.www.common.util.R;
import com.fgh.www.pojo.Advertisement;
import com.fgh.www.pojo.User;
import com.fgh.www.service.IAdvertisementService;
import com.fgh.www.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 广告表 前端控制器
 * </p>
 *
 * @author fgh
 * @since 2025-02-25
 */
@RestController
@RequestMapping("/user-api/advertisement")
public class UAdvertisementController {
    @Autowired
    private IAdvertisementService advertisementService;
    @Autowired
    private IUserService userService;
    @GetMapping("/selectIndex")
    public R selectIndex(@CookieValue(name = "token",required = false,defaultValue = "") String token){
        if(StringUtils.isBlank(token)){
            List<Advertisement> list = advertisementService.selectIndex();
            return R.ok().put("data",list);
        }
        Integer id = Jwt_Utils.getId(token);
        User user = userService.getById(id);
        if(user.getVipStatus()==0){
            List<Advertisement> list = advertisementService.selectIndex();
            return R.ok().put("data",list);
        }

        return R.error();
    }
    @GetMapping("/selectPrson")
    public R selectPrson(@CookieValue(name = "token",required = false,defaultValue = "") String token){
        if(StringUtils.isBlank(token)){
            List<Advertisement> list = advertisementService.selectPrson();
            return R.ok().put("data",list);
        }
        Integer id = Jwt_Utils.getId(token);
        User user = userService.getById(id);
        if(user.getVipStatus()==0){
            List<Advertisement> list = advertisementService.selectPrson();
            return R.ok().put("data",list);
        }

        return R.error();
    }
}
