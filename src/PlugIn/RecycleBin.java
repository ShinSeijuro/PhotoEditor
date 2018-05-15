/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlugIn;

import com.sun.jna.platform.FileUtils;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author CMQ
 */
public class RecycleBin {

    public static void delete(File[] files) throws IOException {
        FileUtils fileUtils = FileUtils.getInstance();
        if (fileUtils.hasTrash()) {
            try {
                fileUtils.moveToTrash(files);
            } catch (IOException ex) {
                throw ex;
            }
        } else {
            throw new UnsupportedOperationException("No Recycle Bin available.");
        }
    }

    public static void delete(File file) throws IOException {
        delete(new File[]{file});
    }
}
