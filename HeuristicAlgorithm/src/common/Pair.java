package common;

// Order of elements in pair is irrelevant. It does not affect the comparison.


public class Pair<FIRST, SECOND> implements Comparable<Pair<FIRST, SECOND>> {
public final FIRST first;
public final SECOND second;


public Pair(FIRST first, SECOND second) {
    this.first = first;
    this.second = second;
}


public static <FIRST, SECOND> Pair<FIRST, SECOND> of(FIRST first, SECOND second) {
    return new Pair<FIRST, SECOND>(first, second);
}


@Override
public int compareTo(Pair<FIRST, SECOND> o) {
    int cmp = compare(first, o.first);
    int normalCompare = (cmp == 0 ? compare(second, o.second) : cmp);
    cmp = compare(first, o.second);
    int crossCompare = (cmp == 0 ? compare(second, o.first) : cmp);
    return (normalCompare == 0 ? crossCompare : cmp);
}
// todo move this to a helper class.
private static int compare(Object o1, Object o2) {
    return o1 == null ? o2 == null ? 0 : -1 : o2 == null ? +1 : ((Comparable) o1).compareTo(o2);
}


@Override
public int hashCode() {
    return 31 * hashcode(first) + hashcode(second);
}


// todo move this to a helper class.
private static int hashcode(Object o) {
    return o == null ? 0 : o.hashCode();
}


@Override
public boolean equals(Object obj) {
    if (!(obj instanceof Pair)) return false;
    if (this == obj) return true;
    return ((equal(first, ((Pair) obj).first) && equal(second, ((Pair) obj).second)) || 
    		(equal(first, ((Pair) obj).second) && equal(second, ((Pair) obj).first)));
}


// todo move this to a helper class.
private boolean equal(Object o1, Object o2) {
    return o1 == null ? o2 == null : (o1 == o2 || o1.equals(o2));
}


@Override
public String toString() {
    return "(" + first + ", " + second + ')';
}
}

