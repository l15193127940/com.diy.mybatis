1.请在开发工具上自行配置lombok
2.jdk使用1.8及以上
3。数据库使用mysql，建表语句如下：
CREATE TABLE `person` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT '',
  `age` varchar(20) DEFAULT '',
  `hobby` varchar(20) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
4、导入数据如下：
INSERT INTO `person` VALUES (1, 'lee', '18', 'fishing');
INSERT INTO `person` VALUES (2, 'wei', '20', 'swimming');
INSERT INTO `person` VALUES (3, 'jim', '30', 'singing');
5、简要说明：使用Application类启动，会输出你要查询的数据返回结果，同时输出动态代理产生的class文件，可自行反编译查看内容