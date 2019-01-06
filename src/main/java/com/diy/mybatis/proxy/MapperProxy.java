package com.diy.mybatis.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;

import com.diy.mybatis.session.SqlSession;

public class MapperProxy implements InvocationHandler {

	private SqlSession sqlSession;
	
	public MapperProxy(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		isDefaultMethod(method);
		Class<?> resultType=method.getReturnType();
		if(Collection.class.isAssignableFrom(resultType))
			return sqlSession.selectList(method.getDeclaringClass().getName()+"."+method.getName(), null==args?null:args[0]);
		else
			return sqlSession.selectOne(method.getDeclaringClass().getName()+"."+method.getName(), null==args?null:args[0]);
	}
	
	 private boolean isDefaultMethod(Method method) {
		    return ((method.getModifiers()
		        & (Modifier.ABSTRACT | Modifier.PUBLIC | Modifier.STATIC)) == Modifier.PUBLIC)
		        && method.getDeclaringClass().isInterface();
		  }

}
