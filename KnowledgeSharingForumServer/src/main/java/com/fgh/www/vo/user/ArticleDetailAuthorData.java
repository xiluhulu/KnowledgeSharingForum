package com.fgh.www.vo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDetailAuthorData {
    //文章阅读总量
    private Long articlesReadNumber;
    //文章总量
    private Long articlesNumber;
    //粉丝总量
    private Long fansNumber;
    //作者名字
    private String name;
    //作者头像
    private String avatar;
}
