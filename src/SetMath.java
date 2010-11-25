import java.util.*;

/**
 * Scala already put this class out of business so why am I writing it?
 */
public class SetMath {


    public static <T> List<T> cloneList(List<T> list) {
        List<T> list2 = new ArrayList<T>(list.size());
        list2.addAll(list);
        return list2;
    }

    public static <T> List<T> union(List<T> a, List<T> b) {
        List<T> u = new ArrayList<T>(a.size() + b.size());
        u.addAll(a);
        u.addAll(b);
        return u;
    }

    public static <T> Set<T> union(Set<T> s, Set<T> t) {
        Set<T> joined = new HashSet<T>(s.size() + t.size());
        joined.addAll(s);
        joined.addAll(t);
        return joined;
    }

    public static <T> Set<T> append(T t, Set<T> s) {
        return union(s, Collections.singleton(t));
    }

    public static <T> Set<T> intersect(Set<T> s, Set<T> t) {
        Set<T> i = new HashSet<T>(s);
        i.retainAll(t);
        return i;
    }

    public static String mkstr(Collection c, String pre, String sep, String post) {
        StringBuilder sb = new StringBuilder(pre);
        boolean first = true;
        for(Object o : c) {
            if(!first) {
                sb.append(sep);
            }
            sb.append(o);
            first = false;
        }
        sb.append(post);
        return sb.toString();
    }
}
