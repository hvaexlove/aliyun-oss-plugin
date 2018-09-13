package com.fit2cloud.jenkins.aliyunoss;

import java.io.*;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * @author shaoshuai
 * @since 2018-09-13 16:18
 */
public class Mimetypes {

    /* The default MIME type */
    public static final String DEFAULT_MIMETYPE = "application/octet-stream";

    private static Mimetypes mimetypes = null;

    private HashMap<String, String> extensionToMimetypeMap = new HashMap<String, String>();

    private Mimetypes() {}

    public synchronized static Mimetypes getInstance() {
        if (mimetypes != null) {
            return mimetypes;
        }
        mimetypes = new Mimetypes();
        InputStream is = mimetypes.getClass().getResourceAsStream("/mime.types");
        if (is != null) {

            try {
                mimetypes.loadMimetypes(is);
            } catch (IOException e) {

            } finally {
                try {
                    is.close();
                } catch (IOException ex) { }
            }
        } else {
        }
        return mimetypes;
    }

    public void loadMimetypes(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line =  null;

        while ((line = br.readLine()) != null) {
            line = line.trim();

            if (line.startsWith("#") || line.length() == 0) {
                // Ignore comments and empty lines.
            } else {
                StringTokenizer st = new StringTokenizer(line, " \t");
                if (st.countTokens() > 1) {
                    String extension = st.nextToken();
                    if (st.hasMoreTokens()) {
                        String mimetype = st.nextToken();
                        extensionToMimetypeMap.put(extension.toLowerCase(), mimetype);
                    }
                }
            }
        }
    }

    public String getMimetype(String fileName) {
        String mimeType = getMimetypeByExt(fileName);
        if (mimeType != null) {
            return mimeType;
        }
        return DEFAULT_MIMETYPE;
    }

    public String getMimetype(File file) {
        return getMimetype(file.getName());
    }

    public String getMimetype(File file, String key) {
        return getMimetype(file.getName(), key);
    }

    public String getMimetype(String primaryObject, String secondaryObject) {
        String mimeType = getMimetypeByExt(primaryObject);
        if (mimeType != null) {
            return mimeType;
        }

        mimeType = getMimetypeByExt(secondaryObject);
        if (mimeType != null) {
            return mimeType;
        }

        return DEFAULT_MIMETYPE;
    }

    private String getMimetypeByExt(String fileName) {
        int lastPeriodIndex = fileName.lastIndexOf(".");
        if (lastPeriodIndex > 0 && lastPeriodIndex + 1 < fileName.length()) {
            String ext = fileName.substring(lastPeriodIndex + 1).toLowerCase();
            if (extensionToMimetypeMap.keySet().contains(ext)) {
                String mimetype = (String) extensionToMimetypeMap.get(ext);
                return mimetype;
            }
        }
        return null;
    }
}
