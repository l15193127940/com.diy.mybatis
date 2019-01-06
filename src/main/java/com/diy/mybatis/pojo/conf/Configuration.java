package com.diy.mybatis.pojo.conf;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.diy.mybatis.pojo.MappedStatement;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Configuration {
    private String driverClassName;
    private String jdbcURL;
    private String userName;
    private String password;
    private Map<String,MappedStatement> statementMap=new HashMap<String,MappedStatement>();
	
}
