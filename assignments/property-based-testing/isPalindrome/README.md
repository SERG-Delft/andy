In this exercise, you will be testing the `isPalindrome` method.

> This method returns whether a given word is a palindrome. A word is a palindrome if the result of reading it from left to right is the same as that of reading it from right to left. The function is case-insensitive, so the string `"Aa"` is considered a palindrome.

Use **property-based testing** to create a suitable test suite for this method.

Tips:

- Use the [JQWik manual](https://jqwik.net/docs/current/user-guide.html)
- The `equalsIgnoreCase` method (which is used in the `isPalindrome` implementation) can behave unexpectedly for letters outside the English alphabet. Feel free to use the `@AlphaChars` annotation to limit the generated strings to only use alphabetic characters.