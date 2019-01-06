package com.diy.mybatis.session;

import java.lang.reflect.Proxy;
import java.util.List;

import com.diy.mybatis.executor.DefaultExecutor;
import com.diy.mybatis.executor.Executor;
import com.diy.mybatis.pojo.MappedStatement;
import com.diy.mybatis.pojo.conf.Configuration;
import com.diy.mybatis.proxy.MapperProxy;

public class DefaultSqlSession implements SqlSession {
	
	private final Configuration configuration;
	private Executor executor;
	
	  public DefaultSqlSession(Configuration configuration) {
		  this.configuration = configuration;
		  this.executor=new DefaultExecutor(configuration);
		  }

	@Override
	public <T> T selectOne(String statement, Object parameter) {
	    List<T> list = this.<T>selectList(statement, parameter);
	    if (list.size() == 1) {
	      return list.get(0);
	    } else if (list.size() > 1) {
	      throw new RuntimeException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
	    } else {
	      return null;
	    }
	}

	@Override
	public <E> List<E> selectList(String statement, Object parameter) {
		MappedStatement ms = configuration.getStatementMap().get(statement);
		return executor.query(ms, parameter);
	}

	@Override
	public <T> T getMapper(Class<T> type) {
		MapperProxy mp=new MapperProxy(this);
		return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{type}, mp);
	}

}
