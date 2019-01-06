package com.diy.mybatis.executor;

import java.sql.SQLException;
import java.util.List;

import com.diy.mybatis.pojo.MappedStatement;

public interface Executor {

  <E> List<E> query(MappedStatement ms, Object parameter);
	
}
