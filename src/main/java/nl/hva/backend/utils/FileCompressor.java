package nl.hva.backend.utils;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Not written by me! -R
 * Got the methods from somewhere on the internet, however I can't seem to find it anymore.
 */
public class FileCompressor {

    private static final Logger logger = LoggerFactory.getLogger(FileCompressor.class);

    public static byte[] compress(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4 * 1024]; // 4MB
        while (!deflater.finished()) {
            int size = deflater.deflate(tmp);
            outputStream.write(tmp, 0, size);
        }

        try {
            outputStream.close();
        } catch (Exception how) {
            logger.error(how.getMessage());
        }
        return outputStream.toByteArray();
    }


    public static byte[] decompress(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4 * 1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0, count);
            }
            outputStream.close();
        } catch (Exception how) {
            logger.error(how.getMessage());
        }
        return outputStream.toByteArray();
    }

}
