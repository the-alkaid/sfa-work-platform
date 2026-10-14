package com.sfa.workorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sfa.workorder.entity.WorkOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WorkOrderMapper extends BaseMapper<WorkOrder> {
}
