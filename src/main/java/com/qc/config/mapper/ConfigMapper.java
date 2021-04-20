package com.qc.config.mapper;

import com.qc.config.dto.Config;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@Mapper
public interface ConfigMapper {

    Collection<Config> queryByModule(@Param("module") String module);

    Collection<Config> queryByModules(@Param("modules") List<String> modules);
}
