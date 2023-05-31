package delft;

import java.util.Collection;

class CollectionUtils {

	/** <code>CollectionUtils</code> should not normally be instantiated. */
	private CollectionUtils() {
	}

	/**
	 * Returns <code>true</code> iff at least one element is in both collections.
	 *
	 * <p>
	 *
	 * @param coll1
	 *            the first collection, must not be null
	 * @param coll2
	 *            the second collection, must not be null
	 * @return <code>true</code> iff the intersection of the collections is
	 *         non-empty
	 * @since 2.1
	 */
	public static boolean containsAny(final Collection<?> coll1, final Collection<?> coll2) {
		if (coll1.size() < coll2.size()) {
			for (final Object aColl1 : coll1) {
				if (coll2.contains(aColl1)) {
					return true;
				}
			}
		} else {
			for (final Object aColl2 : coll2) {
				if (coll1.contains(aColl2)) {
					return true;
				}
			}
		}
		return false;
	}
}
