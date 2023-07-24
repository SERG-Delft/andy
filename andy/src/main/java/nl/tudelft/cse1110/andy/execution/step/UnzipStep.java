package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.Context.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.result.ResultBuilder;

import java.io.*;
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
            File destDir = new File(dirCfg.getOutputDir());

            if(zippedFiles.size() < 1){
                return;
            }

            for (String fileP : zippedFiles) {

                ZipInputStream zis = new ZipInputStream(new FileInputStream(fileP));
                ZipEntry zipEntry = zis.getNextEntry();

                while (zipEntry != null) {
                    File newFile = newFile(destDir, zipEntry);

                    if (zipEntry.isDirectory()) {
                        if (!newFile.isDirectory() && !newFile.mkdirs()) {
                            throw new IOException("Failed to create directory " + newFile);
                        }
                    } else {

                        // fix for Windows-created archives
                        File parent = newFile.getParentFile();
                        if (!parent.isDirectory() && !parent.mkdirs()) {
                            throw new IOException("Failed to create directory " + parent);
                        }

                        // write file content
                        FileOutputStream fos = new FileOutputStream(newFile);
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                        fos.close();

                        zipEntry = zis.getNextEntry();
                    }
                }

                zis.closeEntry();
                zis.close();
            }


        } catch (Exception e) {
            result.genericFailure(this, e);
        }
    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
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
