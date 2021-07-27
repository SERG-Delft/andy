package tudelft.structural;

class CountLetters {

    /** Requirement: Counting words
     * Given a sentence, the program should count the number of words that end with either an "s" or an "r".
     * A word ends when a non-letter appears.
     * @param str - String
     * @return wordcount as int
     */
    public int count(String str) {
        int words = 0;
        char last = ' ';
        for(int i = 0; i < str.length(); i++) {
            if(!Character.isLetter(str.charAt(i)) &&
                    (last == 'r' || last == 's')) {
                words++;
            }

            last = str.charAt(i);
        }

        if(last == 'r' || last == 's')
            words++;

        return words;
    }

}