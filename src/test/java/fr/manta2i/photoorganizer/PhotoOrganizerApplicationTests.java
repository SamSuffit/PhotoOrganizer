package fr.manta2i.photoorganizer;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PhotoOrganizerApplicationTests {

    private static Path intput1;
    private static Path intput2;
    private static Path output;
    private static Path f1;
    private static Path f2;
    private static Path f3;

    @BeforeAll
    public static void init() throws IOException {
        intput1 = Files.createTempDirectory("input1");
        intput2 = Files.createTempDirectory("input2");
        output = Files.createTempDirectory("output");

        f1 = Files.createTempFile(intput1, "s1", "F1");
        f2 = Files.createTempFile(intput1, "s1", "F2");
        f3 = Files.createTempFile(intput2, "s2", "F3");

        setFileTime(f1, 1687250642000L); // Tue, 20 Jun 2023 08:44:02 GMT
        setFileTime(f2, 1679312802000L); // Monday 20 March 2023 11:46:42
        setFileTime(f3, 1687250642000L); // Tue, 20 Jun 2023 08:44:02 GMT

        // Set system properties
        System.setProperty("inputDirectory", intput1.toAbsolutePath() + "," + intput2.toAbsolutePath());
        System.setProperty("outputDirectory", output.toAbsolutePath().toString());
    }

    private static void setFileTime(Path f1, long timeInMillies) throws IOException {
        BasicFileAttributeView attributes = Files.getFileAttributeView(f1.toAbsolutePath(), BasicFileAttributeView.class);
        FileTime time = FileTime.fromMillis(timeInMillies);
        attributes.setTimes(time, time, time);
    }

    @AfterAll
    public static void tearDown() throws IOException {
        FileSystemUtils.deleteRecursively(intput1);
        FileSystemUtils.deleteRecursively(intput2);
        FileSystemUtils.deleteRecursively(output);
    }

    @Test
    void shouldMoveFileInTheCorrectDirectory() {

        // GIVEN
        // before All

        // WHEN
        // context loaded :)

        // THEN
        File file = new File(output.toUri());
        assertThat(file.list())
                .containsExactlyInAnyOrder("2023 06", "2023 03");
        assertThat(new File(file, "2023 03").list())
                .containsExactlyInAnyOrder(f2.getFileName().toString());
        assertThat(new File(file, "2023 06").list())
                .containsExactlyInAnyOrder(f1.getFileName().toString(), f3.getFileName().toString());

    }

}
