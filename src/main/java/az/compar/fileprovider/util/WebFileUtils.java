package az.compar.fileprovider.util;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class WebFileUtils {

    public static HttpEntity<ByteArrayResource> downloadFile(String name, String type, File file) throws IOException {
        return accessFile(name, type, file, Disposition.ATTACHMENT);
    }

    public static HttpEntity<ByteArrayResource> viewFile(String name, String type, File file) throws IOException {
        return accessFile(name, type, file, Disposition.INLINE);
    }

    private static HttpEntity<ByteArrayResource> accessFile(String fileName, String fileType, File file, Disposition disposition) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(file.length());
        headers.setContentType(MediaType.parseMediaType(fileType));

        ContentDisposition contentDisposition;
        if (disposition.equals(Disposition.ATTACHMENT)) {
            contentDisposition = ContentDisposition.attachment().filename(fileName).build();
        } else {
            contentDisposition = ContentDisposition.inline().filename(fileName).build();
        }
        headers.setContentDisposition(contentDisposition);
        return new HttpEntity<>(new ByteArrayResource(Files.readAllBytes(file.toPath())), headers);
    }

    private enum Disposition {
        INLINE,
        ATTACHMENT
    }
}
