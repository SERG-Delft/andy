package nl.tudelft.cse1110.grader.util;

import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClassUtilsTest {


    @Test
    void testExtractPackageName() {

        String sourceCode = "package tudelft.domain;\n" +
                "\n" +
                "class LeapYear {\n" +
                "\n" +
                "    public boolean isLeapYear(int year) {\n" +
                "        if ( year % 400 == 0)\n" +
                "            return true;\n" +
                "        if ( year % 100 == 0)\n" +
                "            return false;\n" +
                "        return year % 4 == 0;\n" +
                "    }\n" +
                "}\n";

        String result = ClassUtils.extractPackageName(sourceCode);

        assertThat(result).isEqualTo("tudelft.domain");
    }

    @Test
    void testNoPackageNameFound() {

        String sourceCode =  "\n" +
                "class LeapYear {\n" +
                "\n" +
                "    public boolean isLeapYear(int year) {\n" +
                "        if ( year % 400 == 0)\n" +
                "            return true;\n" +
                "        if ( year % 100 == 0)\n" +
                "            return false;\n" +
                "        return year % 4 == 0;\n" +
                "    }\n" +
                "}\n";

        assertThrows(RuntimeException.class,
                () -> ClassUtils.extractPackageName(sourceCode));
    }




}
