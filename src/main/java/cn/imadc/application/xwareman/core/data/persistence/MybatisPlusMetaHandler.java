package cn.imadc.application.xwareman.core.data.persistence;

import cn.imadc.application.base.common.context.ReqCtxConstant;
import cn.imadc.application.base.common.context.RequestContext;
import cn.imadc.application.xwareman.core.data.constant.Constant;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * <p>
 * mybatis plus 持久化时添加数据配置
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-24
 */
@Component
public class MybatisPlusMetaHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        RequestContext currentContext = RequestContext.getCurrentContext();

        this.setFieldValByName(Constant.CREATE_BY, currentContext.get(ReqCtxConstant.NAME, String.class), metaObject);
        this.setFieldValByName(Constant.CREATE_BY_ID, currentContext.get(ReqCtxConstant.ID, Long.class), metaObject);

        this.setFieldValByName(Constant.UPDATE_BY, currentContext.get(ReqCtxConstant.NAME, String.class), metaObject);
        this.setFieldValByName(Constant.UPDATE_BY_ID, currentContext.get(ReqCtxConstant.ID, Long.class), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        RequestContext currentContext = RequestContext.getCurrentContext();

        this.setFieldValByName(Constant.UPDATE_BY, currentContext.get(ReqCtxConstant.NAME, String.class), metaObject);
        this.setFieldValByName(Constant.UPDATE_BY_ID, currentContext.get(ReqCtxConstant.ID, Long.class), metaObject);
    }
}
