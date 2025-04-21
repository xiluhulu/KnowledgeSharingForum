package com.fgh.www.vo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 作者的信息（文章数量，被阅读总量，被关注量）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorInfoVO {
    private Integer articleCount;
    private Integer readCount;
    private Integer collectCount;
}
