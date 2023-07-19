package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.result.ResultBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static nl.tudelft.cse1110.andy.utils.FilesUtils.filePathsAsString;
import static nl.tudelft.cse1110.andy.utils.FilesUtils.getAllZippedFiles;

public class UnzipStep implements ExecutionStep {

    @Override
    public void execute(Context ctx, ResultBuilder result) {

        try {
            DirectoryConfiguration dirCfg = ctx.getDirectoryConfiguration();

            List<String> zippedFiles = ctx.getRunConfiguration().zippedFiles();
            byte[] buffer = new byte[1024];
            String destDir = dirCfg.getOutputDir();

            if(zippedFiles.size() < 1){
                return;
            }

            for (String fileP : zippedFiles) {

                ZipInputStream zis = new ZipInputStream(new FileInputStream(fileP));
                ZipEntry ze = zis.getNextEntry();

                while (ze != null) {
                    File file = new File(destDir, ze.getName());

                    FileOutputStream fos = new FileOutputStream(file);
                    int length;
                    while ((length = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }

                    fos.close();

                    ze = zis.getNextEntry();
                }
            }

        } catch (Exception e) {
            result.genericFailure(this, e);
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
