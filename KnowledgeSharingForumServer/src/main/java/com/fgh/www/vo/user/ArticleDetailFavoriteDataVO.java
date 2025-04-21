package com.fgh.www.vo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDetailFavoriteDataVO {
    //是否收藏
    private Boolean isFavorite;
    //收藏数量
    private Long favoriteCount;
}
