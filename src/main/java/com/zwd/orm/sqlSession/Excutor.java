package com.zwd.orm.sqlSession;

public interface Excutor {
    <T> T query(String statement,Object parameter);
}