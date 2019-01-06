package com.diy.mybatis.session;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.diy.mybatis.pojo.MappedStatement;
import com.diy.mybatis.pojo.conf.Configuration;

public class SqlSessionFactory {
	private final Configuration configuration;

	public SqlSessionFactory() {
		// 初始化数据库连接信息
		this.configuration = initConfiguration();
		// 加载mapper
		initMapper();
	}

	// 加載配置的数据库信息
	private Configuration initConfiguration() {
		Configuration configuration = new Configuration();
		Properties prop = new Properties();
		try {
			prop.load(SqlSessionFactory.class.getClassLoader()
					.getResourceAsStream("application.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		configuration.setDriverClassName(prop
				.getProperty("jdbc.driverClassName"));
		configuration.setJdbcURL(prop.getProperty("jdbc.url"));
		configuration.setUserName(prop.getProperty("jdbc.username"));
		configuration.setPassword(prop.getProperty("jdbc.password"));
		return configuration;
	}

	// 加載配置mapper.xml
	private void initMapper() {
		URL resources = this.getClass().getClassLoader()
				.getResource("mappers");
		File mappers = new File(resources.getFile());
		if (mappers.isDirectory()) {
			Arrays.asList(mappers.listFiles()).stream()
					.forEach(f -> parseMapper(f));
		}

	}

	// 解析mapper.xml
	private void parseMapper(File f) {
		try {
			Document document= DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
			//获取根元素
			Element rootElement=document.getDocumentElement();
			String namespace=rootElement.getAttribute("namespace");
			NodeList nodeList = document.getElementsByTagName("select");
			for(int i=0;i<nodeList.getLength();i++){
				Node node=nodeList.item(i);
				String id=node.getAttributes().getNamedItem("id").getTextContent();
				String resultType=node.getAttributes().getNamedItem("resultType").getTextContent();
				String sql=node.getTextContent();
				String mapperId=namespace+"."+id;
				
				MappedStatement ms=new MappedStatement();
				ms.setId(id);
				ms.setResultType(resultType);
				ms.setSql(sql);
				ms.setMapperId(mapperId);
				configuration.getStatementMap().put(mapperId, ms);
				
			}
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 创建会话
	public SqlSession openSession() {
		return new DefaultSqlSession(configuration);
	}

	//

}
