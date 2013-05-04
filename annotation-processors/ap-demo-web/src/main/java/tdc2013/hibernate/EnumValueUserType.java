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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.EnhancedUserType;
import org.hibernate.usertype.ParameterizedType;
import tdc2013.link.Documentation;

@Documentation("https://community.jboss.org/wiki/Java5EnumUserType")
public class EnumValueUserType implements EnhancedUserType, ParameterizedType {

    private Class<Enum> enumClass;

	@Override
	public void setParameterValues(Properties parameters) {
		String enumClassName = parameters.getProperty("enumClassName");
		try {
            enumClass = (Class<Enum>)Class.forName(enumClassName);
		} catch (ClassNotFoundException cnfe) {
			throw new HibernateException("Enum class not found", cnfe);
		}
	}

	@Override
	public Object assemble(Serializable cached, Object owner)
			throws HibernateException {
		return cached;
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
        return (Enum)value;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		return x == y;
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names,
			SessionImplementor session, Object owner) throws HibernateException, SQLException {
		String name = rs.getString(names[0]);
		return rs.wasNull() ? null : Enum.valueOf(enumClass, name);
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index,
			SessionImplementor session) throws HibernateException, SQLException {
		if (value == null) {
			st.setNull(index, Types.VARCHAR);
		} else {
            st.setString(index, ((Enum)value).name());
		}
	}

	@Override
	public Object replace(Object original, Object target, Object owner)
			throws HibernateException {
		return original;
	}

	@Override
	public Class returnedClass() {
		return enumClass;
	}

	@Override
	public int[] sqlTypes() {
        return new int[]{Types.VARCHAR};
	}

	@Override
	public Object fromXMLString(String xmlValue) {
		return Enum.valueOf(enumClass, xmlValue);
	}

	@Override
	public String objectToSQLString(Object value) {
        return '\'' + ((Enum)value).name() + '\'';
	}

	@Override
	public String toXMLString(Object value) {
        return ((Enum)value).name();
	}
}
