package com.zwd.orm.configuration;

public class Function {
    /**
     * sql类型(select/update/delete)
     */
    private String sqltype;
    /**
     * mapper中的函数名(id)
     */
    private String funcName;
    /**
     * mapper中的sql
     */
    private String sql;
    /**
     * mapper中的结果类型
     */
    private Object resultType;
    /**
     * mapper中的参数的类型
     */
    private String parameterType;

    public String getSqltype() {
        return sqltype;
    }

    public void setSqltype(String sqltype) {
        this.sqltype = sqltype;
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object getResultType() {
        return resultType;
    }

    public void setResultType(Object resultType) {
        this.resultType = resultType;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }
}