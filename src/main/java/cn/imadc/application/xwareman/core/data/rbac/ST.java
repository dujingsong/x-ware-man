package cn.imadc.application.xwareman.core.data.rbac;

import cn.imadc.application.base.common.action.IEnumAble;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 主体类型
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-24
 */
@Getter
@AllArgsConstructor
public enum ST implements IEnumAble {
    USER("用户"),
    ;
    private final String description;

    @Override
    public String v() {
        return this.toString();
    }
}
