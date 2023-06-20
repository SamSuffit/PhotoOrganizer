package fr.manta2i.photoorganizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.List;

@SpringBootApplication
public class PhotoOrganizerApplication implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory
            .getLogger(PhotoOrganizerApplication.class);


    private final List<String> inputDirectory;


    private final OrderingFileService orderingFileService;

    public PhotoOrganizerApplication(@Value("${inputDirectory}") List<String> inputDirectory,
                                     OrderingFileService orderingFileService) {
        this.inputDirectory = inputDirectory;
        this.orderingFileService = orderingFileService;
    }

    public static void main(String[] args) {
        SpringApplication.run(PhotoOrganizerApplication.class, args);
    }

    @Override
    public void run(String... args) {
        LOG.info("EXECUTING : command line runner");

        inputDirectory.stream()
                .peek(s -> LOG.info("Starting input {}", s))
                .map(File::new)
                .filter(File::exists)
                .forEach(orderingFileService::orderDirectory)
        ;
    }


}
