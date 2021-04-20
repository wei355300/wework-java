package com.qc.security.account.mapper;

import com.qc.security.account.dto.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AccountMapper {

    Account getByMobile(@Param("mobile") String mobile);

    Account getByToken(@Param("token") String token);

    void updateTokenByAccount(@Param("id") Integer id, @Param("token") String token);

    void updateTokenByToken(@Param("oldToken") String oldToken, @Param("newToken") String newToken);

    List<Account> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);

    List<Account> list();

}
