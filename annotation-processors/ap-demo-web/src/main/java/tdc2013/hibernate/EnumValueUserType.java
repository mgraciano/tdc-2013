/*
 * Copyright (c) 2013, Klaus Boeing & Michel Graciano.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the project's authors nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS AND/OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package tdc2013.hibernate;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.TypeResolver;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

/**
 * @see https://community.jboss.org/wiki/Java5EnumUserType
 * @see http://stackoverflow.com/questions/4744179/java-lang-verifyerror-on-hibernate-specific-usertype
 */
public class EnumValueUserType implements UserType, ParameterizedType {

    private Class<? extends Enum> enumClass;
    private Class<?> identifierType;
    private Method identifierMethod;
    private Method valueOfMethod;
    private static final String defaultIdentifierMethodName = "name";
    private static final String defaultValueOfMethodName = "valueOf";
    private AbstractSingleColumnStandardBasicType type;
    private int[] sqlTypes;

    @Override
    public void setParameterValues(Properties parameters) {
        String enumClassName = parameters.getProperty("enumClass");
        try {
            enumClass = Class.forName(enumClassName).asSubclass(Enum.class);
        } catch (ClassNotFoundException exception) {
            throw new HibernateException("Enum class not found", exception);
        }

        String identifierMethodName = parameters.getProperty("identifierMethod", defaultIdentifierMethodName);

        try {
            identifierMethod = enumClass.getMethod(identifierMethodName,
                    new Class[0]);
            identifierType = identifierMethod.getReturnType();
        } catch (NoSuchMethodException | SecurityException exception) {
            throw new HibernateException("Failed to optain identifier method",
                    exception);
        }

        TypeResolver tr = new TypeResolver();
        type = (AbstractSingleColumnStandardBasicType) tr.basic(identifierType.getName());
        if (type == null) {
            throw new HibernateException("Unsupported identifier type " + identifierType.getName());
        }
        sqlTypes = new int[]{type.sqlType()};

        String valueOfMethodName = parameters.getProperty("valueOfMethod",
                defaultValueOfMethodName);

        try {
            valueOfMethod = enumClass.getMethod(valueOfMethodName,
                    new Class[]{identifierType});
        } catch (NoSuchMethodException | SecurityException exception) {
            throw new HibernateException("Failed to optain valueOf method",
                    exception);
        }
    }

    @Override
    public Class returnedClass() {
        return enumClass;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
            throws HibernateException, SQLException {
        Object identifier = type.get(rs, names[0], session);
        try {
            return valueOfMethod.invoke(enumClass, new Object[]{identifier});
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
            throw new HibernateException("Exception while invoking valueOfMethod of enumeration class: ",
                    exception);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
            throws HibernateException, SQLException {
        try {
            Object identifier = value != null ? identifierMethod.invoke(value, new Object[0]) : null;
            st.setObject(index, identifier);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SQLException exception) {
            throw new HibernateException("Exception while invoking identifierMethod of enumeration class: ",
                    exception);

        }
    }

    @Override
    public int[] sqlTypes() {
        return sqlTypes;
    }

    @Override
    public Object assemble(Serializable cached, Object owner)
            throws HibernateException {
        return cached;
    }

    @Override
    public Object deepCopy(Object value)
            throws HibernateException {
        return value;
    }

    @Override
    public Serializable disassemble(Object value)
            throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public boolean equals(Object x, Object y)
            throws HibernateException {
        return x == y;
    }

    @Override
    public int hashCode(Object x)
            throws HibernateException {
        return x.hashCode();
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Object replace(Object original, Object target, Object owner)
            throws HibernateException {
        return original;
    }
}
