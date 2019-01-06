package com.diy.mybatis.mapper;

import java.util.List;

import com.diy.mybatis.pojo.Person;

public interface PersonMapper {
     public Person getPersonById(int id);
     public List<Person> queryAllPerson();
}
