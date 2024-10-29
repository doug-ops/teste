package com.manager.systems.model;

public abstract class BaseObject
{    
    /**
     * Returns a multi-line String with key=value pairs.
     * @return a String representation of this class.
     */
    @Override
	public abstract String toString();

    /**
     * Compares object equality. When using Hibernate, the primary key should
     * not be a part of this comparison.
     * @param o object to compare to
     * @return true/false based on equality tests
     */
    @Override
	public abstract boolean equals(Object o);

    /**
     * When you override equals, you should override hashCode. See "Why are
     * equals() and hashCode() importation" for more information:
     * http://www.hibernate.org/109.html
     * @return hashCode
     */
    @Override
	public abstract int hashCode();
}