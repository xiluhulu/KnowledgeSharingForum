package com.fgh.www.controller.user;


import com.fgh.www.common.util.Jwt_Utils;
import com.fgh.www.common.util.R;
import com.fgh.www.pojo.Likes;
import com.fgh.www.service.ILikesService;
import com.fgh.www.vo.user.ArticleDetailLikeDataVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 点赞 前端控制器
 * </p>
 *
 * @author fgh
 * @since 2024-12-19
 */
@Tag(name = "前端Likes接口", description = "包含用户相关的 API 接口")
@RestController
@RequestMapping("/user-api/likes")
public class ULikesController {
    @Autowired
    private ILikesService likesService;
    // 添加点赞
    @PostMapping("/addLike")
    public R addLike(@RequestBody Likes likes,@CookieValue("token") String token) {
        Integer id = Jwt_Utils.getId(token);
        //设置点赞的用户id
        likes.setUserId(id);
        boolean b = likesService.selectOne(likes);
        if (b){
            return R.error("已经点赞过了");
        }
        likesService.save(likes);
        return R.ok("点赞成功").put("data",true);
    }
    //获取详情页面的点赞数据
    @GetMapping("/getLikeData/{fid}")
    public R getLikeData(@PathVariable("fid")Integer fid,@CookieValue(name = "token",required = false,defaultValue = "") String token){
        //如果登录则判断是否点赞
        boolean isLike=false;
        if (StringUtils.isNoneBlank(token)){
            Integer id = Jwt_Utils.getId(token);
            boolean b = likesService.selectOne(new Likes().setFid(fid).setUserId(id));
            if(b){
                isLike=true;
            }
        }
        Long articleLikeCount = likesService.getArticleLikeCount(fid);
        return R.ok().put("data",new ArticleDetailLikeDataVO(isLike,articleLikeCount));
    }
    //删除点赞
    @DeleteMapping("/deleteLike/{fid}")
    public R deleteLike(@PathVariable("fid")Integer fid,@CookieValue("token") String token){
        Integer id = Jwt_Utils.getId(token);
        boolean b = likesService.deleteLike(fid, id);
        if (!b){
            return R.error("取消点赞失败");
        }
        return R.ok("取消点赞成功").put("data",false);
    }

}
