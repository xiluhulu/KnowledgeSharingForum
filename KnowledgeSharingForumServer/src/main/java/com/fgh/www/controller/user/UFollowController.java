package com.fgh.www.controller.user;


import com.fgh.www.common.util.Jwt_Utils;
import com.fgh.www.common.util.R;
import com.fgh.www.pojo.Follow;
import com.fgh.www.service.IFollowService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 关注表 前端控制器
 * </p>
 *
 * @author fgh
 * @since 2025-02-19
 */
@Tag(name = "前端Follow接口", description = "包含用户相关的 API 接口")
@RestController
@RequestMapping("/user-api/follow")
public class UFollowController {
    @Autowired
    private IFollowService followService;

    @PostMapping("/addFollow")
    public R addFollow(@RequestBody Follow follow, @CookieValue("token") String token) {
        Integer id = Jwt_Utils.getId(token);

        //设置关注人id
        follow.setFollowerId(id);
        boolean b = followService.selectOne(follow);
        if (b) {
            return R.error("不能重复关注");
        }
        followService.save(follow);
        return R.ok("关注成功").put("data",true);
    }
    @DeleteMapping("/deleteFollow/{followingId}")
    public R deleteFollow(@PathVariable("followingId") Integer followingId, @CookieValue("token") String token) {
        Integer id = Jwt_Utils.getId(token);
        boolean b = followService.deleteFollow(id, followingId);
        if (!b) {
            return R.error("取消关注失败");
        }
        return R.ok("成功取消关注").put("data",false);
    }
    @GetMapping("/isFollow/{followingId}")
    public R isFollow(@PathVariable("followingId") Integer followingId, @CookieValue("token") String token) {
        Integer id = Jwt_Utils.getId(token);
        Follow follow = new Follow();
        follow.setFollowerId(id);
        follow.setFollowingId(followingId);
        boolean b = followService.selectOne(follow);
        if (b) {
             return R.ok("已关注").put("data",true);
        }
        return R.ok("未关注").put("data",false);
    }

    public R getFollowerList() {
        return R.ok();
    }
}
