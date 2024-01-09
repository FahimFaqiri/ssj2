package nl.hva.backend.services;

import nl.hva.backend.models.StoredFile;
import nl.hva.backend.repositories.StoredFileRepository;
import nl.hva.backend.utils.FileCompressor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class StorageService {

    @Autowired
    private StoredFileRepository storedFileRepository;

    /**
     * Compression is NOT recommended.
     *
     * @param file
     * @param filename
     * @param compressFile
     * @return
     * @throws IOException
     */
    public String upload(MultipartFile file, String filename, boolean compressFile) throws IOException {
        // get original if exists
        Optional<StoredFile> originalFile = storedFileRepository.findUserFileByName(filename);

        // if file exists just modify else make a new one
        StoredFile newFile = originalFile.orElseGet(StoredFile::new);
        newFile.setName(filename);
        newFile.setType(file.getContentType());
        newFile.setData(compressFile ? FileCompressor.compress(file.getBytes()) : file.getBytes());
        newFile.setCompressed(compressFile);

        StoredFile savedFile = storedFileRepository.save(newFile);
        if (savedFile != null) {
            return "Saved file: " + savedFile.getName();
        } else {
            return null;
        }
    }

    //Upload resume
    public String uploadResume(MultipartFile resume, String filename) throws IOException {
        StoredFile storedResume = storedFileRepository.findUserFileByName(filename).orElseGet(StoredFile::new);
        storedResume.setName(filename);
        storedResume.setType(resume.getContentType());
        storedResume.setData(resume.getBytes());

        StoredFile savedResume = storedFileRepository.save(storedResume);
        return "The saved resume: " + savedResume.getName();
    }


    /**
     * @param file
     * @return
     * @throws IOException
     */
    public String upload(MultipartFile file) throws IOException {
        return upload(file, file.getOriginalFilename(), true);
    }

    /**
     * @param filename
     * @return
     */
    public byte[] download(String filename) {
        Optional<StoredFile> file = storedFileRepository.findUserFileByName(filename);
        if (file.isEmpty()) {
            return null;
        }

        return file.get().isCompressed() ? FileCompressor.decompress(file.get().getData()) : file.get().getData();
    }

    public boolean delete(String filename) {
        if (fileExists(filename)) {
            storedFileRepository.deleteByName(filename);
            return true;
        }

        return false;
    }

    public boolean fileExists(String filename) {
        return storedFileRepository.existsByName(filename);
    }
}
