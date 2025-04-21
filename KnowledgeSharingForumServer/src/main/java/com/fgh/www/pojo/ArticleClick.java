package com.fgh.www.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
/**
 * @author fgh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleClick {

    private Integer articleId;
    private Long timestamp;

}
