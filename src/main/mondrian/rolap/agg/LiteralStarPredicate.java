/*
// $Id$
// This software is subject to the terms of the Common Public License
// Agreement, available at the following URL:
// http://www.opensource.org/licenses/cpl.html.
// Copyright (C) 2006-2006 Julian Hyde
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
*/
package mondrian.rolap.agg;

import mondrian.rolap.StarPredicate;
import mondrian.rolap.RolapStar;
import mondrian.rolap.StarColumnPredicate;

import java.util.Collection;
import java.util.List;

/**
 * A constraint which always returns true or false.
 *
 * @author jhyde
 * @version $Id$
 * @since Nov 2, 2006
 */
public class LiteralStarPredicate extends AbstractColumnPredicate {
    private final boolean value;

    public static final LiteralStarPredicate TRUE =
        new LiteralStarPredicate(null, true);
    public static final LiteralStarPredicate FALSE =
        new LiteralStarPredicate(null, false);

    /**
     * Creates a LiteralStarPredicate.
     *
     * @param column Constrained column
     * @param value Truth value
     */
    public LiteralStarPredicate(RolapStar.Column column, boolean value) {
        super(column);
        this.value = value;
    }


    public int hashCode() {
        return value ? 2 : 1;
    }

    public boolean equals(Object obj) {
        if (obj instanceof LiteralStarPredicate) {
            LiteralStarPredicate that =
                (LiteralStarPredicate) obj;
            return this.value == that.value;
        } else {
            return false;
        }
    }

    public boolean evaluate(List<Object> valueList) {
        assert valueList.isEmpty();
        return value;
    }

    public boolean equalConstraint(StarPredicate that) {
        throw new UnsupportedOperationException();
    }

    public String toString() {
        return Boolean.toString(value);
    }

    public void values(Collection collection) {
    }

    public boolean evaluate(Object value) {
        return this.value;
    }

    public void describe(StringBuilder buf) {
        buf.append("=any");
    }

    public Overlap intersect(
        StarColumnPredicate predicate) {
        return new Overlap(value, null, 0f);
    }

    public boolean mightIntersect(StarPredicate other) {
        // FALSE intersects nothing
        // TRUE intersects everything except FALSE
        if (!value) {
            return false;
        } else if (other instanceof LiteralStarPredicate) {
            return ((LiteralStarPredicate) other).value;
        } else {
            return true;
        }
    }

    public StarColumnPredicate minus(StarPredicate predicate) {
        assert predicate != null;
        if (value) {
            // We have no 'not' operator, so there's no shorter way to represent
            // "true - constraint".
            return new MinusStarPredicate(this, (StarColumnPredicate) predicate);
        } else {
            // "false - constraint" is "false"
            return this;
        }
    }

    public boolean getValue() {
        return value;
    }
}

// End LiteralStarPredicate.java