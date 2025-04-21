package com.fgh.www.vo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDetailLikeDataVO {
    //是否点赞
    private Boolean isLike;
    //点赞数量
    private Long likeCount;
}
