package nl.tudelft.cse1110.andy.grader.util;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.junit.jupiter.api.Assertions.*;

public class FilesUtilsTest {


    @Test
    void testConcatenateWindowsDirectories() {

        String dir1 = "C:/Users/alice/education/cse1110/test/code/";
        String dir2 = "delft";

        assertThat(FilesUtils.concatenateDirectories(dir1, dir2))
                .isEqualTo("C:/Users/alice/education/cse1110/test/code/delft");
    }


    @Test
    void testConcatenateWindowsDirectoriesAddSlash() {

        String dir1 = "C:/Users/alice/education/cse1110/test/code";
        String dir2 = "delft";

        assertThat(FilesUtils.concatenateDirectories(dir1, dir2))
                .isEqualTo("C:/Users/alice/education/cse1110/test/code/delft");
    }

    @Test
    void testConcatenateLinuxDirectories() {

        String dir1 = "/Users/alice/education/cse1110/test/code/";
        String dir2 = "delft";

        assertThat(FilesUtils.concatenateDirectories(dir1, dir2))
                .isEqualTo("/Users/alice/education/cse1110/test/code/delft");
    }


    @Test
    void testConcatenateLinuxDirectoriesAddSlash() {

        String dir1 = "/Users/alice/education/cse1110/test/code";
        String dir2 = "delft";

        assertThat(FilesUtils.concatenateDirectories(dir1, dir2))
                .isEqualTo("/Users/alice/education/cse1110/test/code/delft");
    }


    @Test
    void testCopyFileNonExistentWindowsPaths() {

        String sourceFile = "C:\\Users\\alice\\AppData\\NonExistentPath\\Temp\\junit15144570570849569761\\domain\\addingnumbers\\Solution.java";
        String destFile = "C:\\Users\\alice\\AppData\\NonExistentPath\\Temp\\metaWorkplace8612545675667245859";

        assertThrows(RuntimeException.class,
                () -> FilesUtils.copyFile(sourceFile, destFile));
    }


    @Test
    void testMoveFileNonExistentWindowsPaths() {

        String sourceFile = "C:\\Users\\alice\\education\\cse1110\\test\\code\\delft\\Solution.java";
        String destDir = "C:/Users/alice/NonExistentPath/cse1110/test/code/delft";
        String destFileName = "Solution.java";

        assertThrows(RuntimeException.class,
                () -> FilesUtils.moveFile(sourceFile, destDir, destFileName));
    }


    @Test
    void testCopyFileNonExistentLinuxPaths() {

        String sourceFile = "\\Users\\alice\\AppData\\NonExistentPath\\Temp\\junit15144570570849569761\\domain\\addingnumbers\\Solution.java";
        String destFile = "\\Users\\alice\\AppData\\NonExistentPath\\Temp\\metaWorkplace8612545675667245859";

        assertThrows(RuntimeException.class,
                () -> FilesUtils.copyFile(sourceFile, destFile));
    }


    @Test
    void testMoveFileNonExistentLinuxPaths() {

        String sourceFile = "\\Users\\alice\\education\\cse1110\\test\\code\\delft\\Solution.java";
        String destDir = "/Users/alice/NonExistentPath/cse1110/test/code/delft";
        String destFileName = "Solution.java";

        assertThrows(RuntimeException.class,
                () -> FilesUtils.moveFile(sourceFile, destDir, destFileName));
    }



}
