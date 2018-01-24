package org.kulingkwi.leanring;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import sun.nio.ch.IOUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Map;

public class FastDFSClient {

    public static final String CONFIG_FILENAME = "~/projects/idea/learning/fastdfs/src/main/resources/fdfs_client.conf.sample";

    private static StorageClient1 storageClient;

    static {
        try {
            ClientGlobal.init(CONFIG_FILENAME);
            TrackerClient trackerClient = new TrackerClient(ClientGlobal.getG_tracker_group());
            TrackerServer trackerServer = trackerClient.getConnection();
            if (trackerServer == null) {
                throw new IllegalStateException("get tracker server failed with null");
            }
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            if (storageServer == null) {
                throw new IllegalStateException("get storage server failed with null");
            }
            storageClient = new StorageClient1(trackerServer, storageServer);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String uploadFile(File file, String fileName) {
        return uploadFile(file,fileName,null);
    }

    public static String uploadFile(File file, String fileName, Map<String,String> metaList) {
        try {
            byte[] buff = IOUtils.toByteArray(new FileInputStream(file));
            NameValuePair[] nameValuePairs = null;
            if (metaList != null) {
                nameValuePairs = new NameValuePair[metaList.size()];
                int index = 0;
                for (Iterator<Map.Entry<String,String>> iterator = metaList.entrySet().iterator(); iterator.hasNext();) {
                    Map.Entry<String,String> entry = iterator.next();
                    String name = entry.getKey();
                    String value = entry.getValue();
                    nameValuePairs[index++] = new NameValuePair(name,value);
                }
            }
            return storageClient.upload_file1(buff, FilenameUtils.getExtension(fileName),nameValuePairs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int downloadFile(String fileId, File outFile) {
        FileOutputStream fos = null;
        try {
            byte[] content = storageClient.download_file1(fileId);
            fos = new FileOutputStream(outFile);
            IOUtils.copy(new ByteArrayInputStream(content),fos);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }

    public static int deleteFile(String fileId) {
        try {
            return storageClient.delete_file1(fileId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

}
