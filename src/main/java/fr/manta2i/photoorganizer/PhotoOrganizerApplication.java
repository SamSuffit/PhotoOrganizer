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


    private final List<String> inputDirectory;

    private final File outputDirectory;

    private final OrderingFileService orderingFileService;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM")
            .withZone(ZoneId.systemDefault());

    public PhotoOrganizerApplication(@Value("${inputDirectory}") List<String> inputDirectory,
                                     @Value("${outputDirectory}") File outputDirectory,
                                     OrderingFileService orderingFileService) {
        this.inputDirectory = inputDirectory;
        this.outputDirectory = outputDirectory;
        this.orderingFileService = orderingFileService;
    }

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
                .forEach(orderingFileService::orderDirectory)
        ;
    }


}
