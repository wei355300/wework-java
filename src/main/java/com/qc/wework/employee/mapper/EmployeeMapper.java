package com.qc.wework.employee.mapper;

import com.qc.wework.employee.dto.Employee;
import com.qc.wework.employee.dto.EmployeeDetail;
import me.chanjar.weixin.cp.bean.WxCpUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (Employee)表数据库访问层
 *
 * @author makejava
 * @since 2020-12-03 23:09:04
 */
@Mapper
public interface EmployeeMapper {

    int insertOrUpdateCpUserDetail(@Param("list") List<WxCpUser> list);

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EmployeeDetail queryById(@Param("id")Integer id);

    /**
     * 通过手机号查询单条数据
     *
     * @param mobile 主键
     * @return 实例对象
     */
    EmployeeDetail queryByMobile(@Param("mobile")String mobile);

    /**
     * 查询指定行数据
     *
     * @return 对象列表
     */
    List<Employee> list();

    List<Employee> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);

}