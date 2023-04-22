package fr.manta2i.photoorganizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SpringBootApplication
public class PhotoOrganizerApplication implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory
            .getLogger(PhotoOrganizerApplication.class);


    private final List<String> inputDirectory = List.of("C:\\Users\\Samuel\\Pictures\\onePlus", "U:\\Images\\Pixel");

    private final File output = new File("C:\\Users\\Samuel\\Pictures");

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM")
            .withZone(ZoneId.systemDefault());

    public static void main(String[] args) {
        SpringApplication.run(PhotoOrganizerApplication.class, args);
    }

    @Override
    public void run(String... args) {
        LOG.info("EXECUTING : command line runner");

        inputDirectory.stream()
                .peek(s -> LOG.info("Starting input {}" ,s ))
                .map(File::new)
                .filter(File::exists)
                .forEach(directory -> Arrays.stream(Objects.requireNonNull(directory.listFiles()))
                        .filter(PhotoOrganizerApplication::isIgnoreFile)
                        .forEach(file -> {
                            try {
                                BasicFileAttributes attr =
                                        Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                                String outputDirYearMonth = formatter.format(attr.lastModifiedTime().toInstant());
                                File outputDir = new File(output, outputDirYearMonth);
                                File outputFile = new File(outputDir, file.getName());
                                LOG.info("File {} {} exist {} isDir {} outFileExist {}", file.getName(), outputDirYearMonth, outputDir.exists(), outputDir.isDirectory(), outputFile.exists());
                                copyFileIfNeeded(file,  outputFile);

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        })
                )
        ;
    }

    private static void copyFileIfNeeded(File file, File outputFile) throws IOException {
        if (!outputFile.exists()) {
            File outputDir = outputFile.getParentFile();
            if (!outputDir.exists()) {
                Files.createDirectories(outputDir.toPath());
            }
            Files.copy(file.toPath(), outputFile.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
        }
    }

    private static boolean isIgnoreFile(File file) {
        return !file.getName().startsWith(".");
    }

}
