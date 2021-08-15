package nl.tudelft.cse1110.grader.util;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FileUtilsTest {


    @Test
    void testConcatenateDirectories() {

        String dir1 = "C:/Users/alice/education/cse1110/test/code/";
        String dir2 = "delft";

        assertThat(FileUtils.concatenateDirectories(dir1, dir2))
                .isEqualTo("C:/Users/alice/education/cse1110/test/code/delft");
    }


    @Test
    void testConcatenateDirectoriesAddSlash() {

        String dir1 = "C:/Users/alice/education/cse1110/test/code";
        String dir2 = "delft";

        assertThat(FileUtils.concatenateDirectories(dir1, dir2))
                .isEqualTo("C:/Users/alice/education/cse1110/test/code/delft");
    }

    @Test
    void testCopyFileNonExistentPaths() {

        String sourceFile = "C:\\Users\\alice\\AppData\\NonExistentPath\\Temp\\junit15144570570849569761\\domain\\addingnumbers\\Solution.java";
        String destFile = "C:\\Users\\alice\\AppData\\NonExistentPath\\Temp\\metaWorkplace8612545675667245859";

        assertThrows(RuntimeException.class,
                () -> FileUtils.copyFile(sourceFile, destFile));
    }


    @Test
    void testMoveFileNonExistentPaths() {

        String sourceFile = "C:\\Users\\alice\\education\\cse1110\\test\\code\\delft\\Solution.java";
        String destDir = "C:/Users/alice/NonExistentPath/cse1110/test/code/delft";
        String destFileName = "Solution.java";

        assertThrows(RuntimeException.class,
                () -> FileUtils.moveFile(sourceFile, destDir, destFileName));
    }



}
