package az.compar.fileprovider.service;

import az.compar.fileprovider.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class FileService {


    public File getFile(String path) {
        return new File(path);
    }

    public String saveFile(User user, String fileName, MultipartFile fileToUpload) throws IOException {
        String assets = "src/main/resources/assets";
        String folderPath = assets + "/" + user.getId();
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdir();
        }
        String filePath = folderPath + "/" + fileName;
        File newFile = new File(filePath);
        newFile.createNewFile();
        OutputStream os = new FileOutputStream(newFile);
        os.write(fileToUpload.getBytes());
        os.close();
        return filePath;
    }

    public void deleteFile(String path) {
        File file = new File(path);
        file.delete();
    }
}
