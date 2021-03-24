package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

        @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) VALUES " +
                "(#{url}, #{username}, #{key}, #{password}, #{userid})")
        @Options(useGeneratedKeys = true, keyProperty = "credentialid")
        int insert(Credential credential);

        @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userid}")
        List<Credential> getUserCredentials(Integer userid);

        @Update("""
                UPDATE CREDENTIALS
                SET     url = #{url},
                        username = #{username},
                        password = #{password}
                WHERE   credentialid = #{credentialid}
                """)
        boolean update(Credential credential);

        @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialid}")
        boolean deleteById(int id);

}
