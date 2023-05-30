The method `indexOf` has the following requirements:

```

/**
 * Finds the index of the given value in the array starting at the given index.
 *
 * This method returns INDEX_NOT_FOUND (-1) for a null
 * input array.
 *
 * A negative startIndex is treated as zero. A startIndex larger than the array
 * length will return INDEX_NOT_FOUND (-1).
 *
 * @param array
 *            the array to search through for the object, may be null
 * @param valueToFind
 *            the value to find
 * @param startIndex
 *            the index to start searching at
 * @return the index of the value within the array, INDEX_NOT_FOUND
 *         (-1) if not found or {@code null} array input
 */
```

Test it!

(From Apache Commons Lang: https://github.com/apache/commons-lang/blob/d2687419c6973572d1621afc1b8546f5262769c3/src/main/java/org/apache/commons/lang3/ArrayUtils.java#L2325)