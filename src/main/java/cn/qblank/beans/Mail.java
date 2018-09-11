package cn.qblank.beans;

import lombok.*;

import java.util.Set;

/**
 * @author evan_qb
 * @date 2018/8/29 16:11
 */
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mail {
    private String subject;

    private String message;

    private Set<String> receivers;
}
