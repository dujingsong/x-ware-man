package cn.imadc.application.xwareman.module.cluster.dto.request;

import cn.imadc.application.base.common.search.BaseSearchDTO;
import cn.imadc.application.xwareman.core.data.constant.Constant;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ClusterQueryReqDTO extends BaseSearchDTO implements Serializable {

    private Integer delFlag = Constant.NOT_DEL_VAL;

}
