package com.diy.mybatis;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;

import sun.misc.ProxyGenerator;

import com.diy.mybatis.mapper.PersonMapper;
import com.diy.mybatis.pojo.Person;
import com.diy.mybatis.session.SqlSession;
import com.diy.mybatis.session.SqlSessionFactory;

public class Application {

	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
  	
		//创建会话工厂
		SqlSessionFactory factory=new SqlSessionFactory();
		//通过工厂生成sqlSession
		SqlSession sqlSession=factory.openSession();
		//使用动态代理生成PersonMapper
		PersonMapper pm=sqlSession.getMapper(PersonMapper.class);
		List<Person> ps=pm.queryAllPerson();
		ps.forEach(p->System.out.println(p));
		System.out.println("################");
		System.out.println(pm.getPersonById(2));
		
		//以下是查看生成代理类的字节码，已经输出到本地，通过反编译工具可以查看
		Field field = System.class.getDeclaredField("props");    
		field.setAccessible(true);    
		Properties props = (Properties) field.get(null);    
		props.put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");  
		byte[] proxyClassFile = ProxyGenerator.generateProxyClass(pm.getClass().getName(),pm.getClass().getInterfaces());
		//将动态代理类生成
		exportClazzToFile("D:/log1/", pm.getClass().getName()+".class", proxyClassFile);
	}
	
	  /**
     * 
     * @param dirPath
     *目录以/结尾，且必须存在
     * @param fileName
     * @param data
     */
    private static void exportClazzToFile(String dirPath, String fileName, byte[] data) {
        try {
        	 File dir = new File(dirPath);
        	    if (!dir.exists()) {
        	    	dir.mkdir();
                   
                }  
        	
            File file = new File(dirPath + fileName);
            if (!file.exists()) {
                file.createNewFile();    
            }    
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.close();    
        } catch (Exception e) {
            System.out.println("exception occured while doing some file operation");
            e.printStackTrace();
        }
    }

}
