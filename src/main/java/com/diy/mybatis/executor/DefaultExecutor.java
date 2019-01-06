package com.diy.mybatis.executor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.diy.mybatis.pojo.MappedStatement;
import com.diy.mybatis.pojo.conf.Configuration;
import com.mysql.jdbc.PreparedStatement;

public class DefaultExecutor implements Executor {
	
	private final Configuration configuration;
	
	  public DefaultExecutor(Configuration configuration) {
		  this.configuration = configuration;
		  }
	@Override
	public <E> List<E> query(MappedStatement ms, Object parameter) {
		// TODO Auto-generated method stub
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		//加载驱动类
		try {
			Class.forName(configuration.getDriverClassName());
			conn=DriverManager.getConnection(configuration.getJdbcURL(),configuration.getUserName(),configuration.getPassword());
			ps=(PreparedStatement) conn.prepareStatement(ms.getSql());
			handleParameter(ps, parameter);
			rs=ps.executeQuery();
			return handleResultSet(rs,ms.getResultType());
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(null!=rs){
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(null!=ps){
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(null!=conn){
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		return null;
	}
	
	//处理请求参数
	private void handleParameter(PreparedStatement ps,Object parameter) throws SQLException{
		if(parameter instanceof Integer){
			ps.setInt(1, (int) parameter);
		}else if(parameter instanceof Long){
			ps.setLong(1, (Long) parameter);
		}else if(parameter instanceof String){
			ps.setString(1, (String) parameter);
		}
	}
	
	//处理返回结果
	private <E> List<E> handleResultSet(ResultSet rs,String resultType){
	  List<E> list=new ArrayList<E>();
	  if(rs!=null){
		  try {  
		  ResultSetMetaData rsmd = rs.getMetaData(); 
		  Field[] fields = Class.forName(resultType).getDeclaredFields();
			while(rs.next()){
				//获取返回值类型
				E bean = (E) Class.forName(resultType).newInstance();
				for (int _iterator = 0; _iterator < rsmd.getColumnCount(); _iterator++) { 
					// getting the SQL column name 
					String columnName = rsmd.getColumnName(_iterator + 1); 
					// reading the value of the SQL column 
					Object columnValue = rs.getObject(_iterator + 1); 
					for (Field field : fields) {
						if(field.getName().equalsIgnoreCase(columnName)){
							field.setAccessible(true);
							field.set(bean, columnValue);
						}
					}

				}
				list.add(bean);
			 
			    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	 return list;
	}

}
