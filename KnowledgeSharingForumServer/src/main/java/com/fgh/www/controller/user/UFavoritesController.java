package com.fgh.www.controller.user;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgh.www.common.util.Jwt_Utils;
import com.fgh.www.common.util.R;
import com.fgh.www.pojo.Favorites;
import com.fgh.www.service.IFavoritesService;
import com.fgh.www.vo.user.ArticleDetailFavoriteDataVO;
import com.fgh.www.vo.user.ArticleVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 收藏表 前端控制器
 * </p>
 *
 * @author fgh
 * @since 2025-02-20
 */
@Tag(name = "前端Favorites接口", description = "包含用户相关的 API 接口")
@RestController
@RequestMapping("/user-api/favorites")
public class UFavoritesController {
    @Autowired
    private IFavoritesService favoritesService;
    //分页查询收藏列表
    @GetMapping("/selectPage")
    public R selectPage(@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,@CookieValue("token")String token){
        Integer id = Jwt_Utils.getId(token);
        Page<ArticleVO> page = favoritesService.selectPage(pageNum, pageSize, id);
        return R.ok().put("data",page);
    }


    //添加收藏
    @PostMapping("/addFavorites")
    public R addFavorites(@RequestBody Favorites favorites, @CookieValue("token") String token) {

        Integer id = Jwt_Utils.getId(token);
        favorites.setUserId(id);
        boolean b = favoritesService.selectOne(favorites);
        if (b){
            return R.error("已经收藏过了");
        }
        favoritesService.save(favorites);
        return R.ok("收藏成功").put("data",true);
    }
    //删除收藏
    @DeleteMapping("/deleteFavorites/{articleId}")
    public R deleteFavorites(@PathVariable("articleId") Integer articleId, @CookieValue("token") String token) {
        Integer id = Jwt_Utils.getId(token);

        boolean b = favoritesService.deleteFavorite(articleId, id);
        if (!b){
            return R.error("取消收藏失败");
        }
        return R.ok("取消收藏成功").put("data",false);
    }
    //获取详情页面的收藏数据
    @GetMapping("/getFavoriteData/{articleId}")
    public R getFavoriteData(@PathVariable("articleId") Integer articleId, @CookieValue(name = "token",required = false,defaultValue = "") String token) {
        //如果登录则判断是否收藏
        boolean isFavorite=false;
        if (StringUtils.isNoneBlank(token)){
            Integer id = Jwt_Utils.getId(token);
            boolean b = favoritesService.selectOne(new Favorites().setArticleId(articleId).setUserId(id));
            if(b){
                isFavorite=true;
            }
        }
        Long articleFavoriteCount = favoritesService.getArticleFavoriteCount(articleId);
        return R.ok().put("data",new ArticleDetailFavoriteDataVO(isFavorite,articleFavoriteCount));

    }
}
