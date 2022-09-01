package JavaExtractor;

import com.github.javaparser.ParseProblemException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.kohsuke.args4j.CmdLineException;

import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Runs an example End-to-End test on the example .jsonl
 * Important: The args cannot end with a space!
 * E.g. having "--file " instead of "--file" will result in errors.
 */
public class EndToEndTest {

    @Tag("File")
    @Test
    void testApp_OnExampleJavaFileWithComments_ShouldWork() throws CmdLineException {
        String testFilePath = "src/test/resources/examples/comments.java";
        String[] args = {"--file",testFilePath,"--max_path_length","200","--max_path_width","10"};

        App.main(args);
    }

    @Tag("File")
    @Test
    void testApp_OnExampleJavaFileWithComments_CommentsDontHaveLeadingSpace_ShouldWork() throws CmdLineException {
        //Difference: These comments do not have a space before them
        String testFilePath = "src/test/resources/examples/comments2.java";
        String[] args = {"--file",testFilePath,"--max_path_length","200","--max_path_width","10"};

        App.main(args);
    }
    @Tag("File")
    @Test
    void testApp_OnExampleJavaFileWithoutComments_ShouldWork() throws CmdLineException {
        String testFilePath = "src/test/resources/examples/nocomments.java";
        String[] args = {"--file",testFilePath,"--max_path_length","200","--max_path_width","10"};

        App.main(args);
    }

    @Tag("File")
    @Test
    void testApp_OnExampleFile_ThatDoesntHaveAClass_ShouldWork() throws CmdLineException {
        // Note: Partial Java Classes are ok? i.E. the File does not have a class around it, just a method.
        String testFilePath = "src/test/resources/examples/onlyMethod.java";
        String[] args = {"--file",testFilePath,"--max_path_length","200","--max_path_width","10"};

        App.main(args);
    }

    @Tag("File")
    @Test
    void testApp_OnExampleJavaFileWithLongNames_ShouldWork() throws CmdLineException {
        String testFilePath = "src/test/resources/examples/longNames.java";
        String[] args = {"--file",testFilePath,"--max_path_length","200","--max_path_width","10"};

        App.main(args);
    }

    @Tag("File")
    @Test
    void testApp_OnExampleDir_ShouldWork() throws CmdLineException {
        String testDirPath = "src/test/resources/examples/nocomments.java";
        String[] args = {"--dir",testDirPath,"--max_path_length","200","--max_path_width","10"};

        App.main(args);
    }

    @Tag("File")
    @Test
    void testApp_OnBadFile_ShouldThrowError() {
        String testFilePath = "src/test/resources/jsonls/jsonTest.jsonl";
        String[] args = {"--file",testFilePath,"--max_path_length","200","--max_path_width","10"};

        assertThrows(ParseProblemException.class,() -> App.main(args) );
    }

    @Tag("File")
    @Test
    void testApp_OnExampleBadDir_DoesntWorkButShouldExit() throws CmdLineException {
        String testDirPath = "src/test/resources/jsonls/jsonTest.jsonl";
        String[] args = {"--dir",testDirPath,"--max_path_length","200","--max_path_width","10"};

        App.main(args);
    }

    @Tag("File")
    @Test
    void testApp_missingPathLength_shouldError() {
        String testFilePath = "src/test/resources/jsonTest.jsonl";
        String[] args = {"--file",testFilePath,"--max_path_width","10"};

        assertThrows(CmdLineException.class,() -> App.main(args) );
    }

    @Tag("File")
    @Test
    void testApp_missingPathWidth_shouldError() {
        String testFilePath = "src/test/resources/jsonTest.jsonl";
        String[] args = {"--file",testFilePath,"--max_path_length","200",};

        assertThrows(CmdLineException.class,() -> App.main(args) );
    }

    @Tag("File")
    @Test
    void testApp_OnExampleFile_WithTypos_ShouldFail() throws CmdLineException {
        String testFilePath = "src/test/resources/bad_examples/spelling.java";
        String[] args = {"--file",testFilePath,"--max_path_length","200","--max_path_width","10"};

        assertThrows(ParseProblemException.class,() -> App.main(args) );
    }


    @Tag("File")
    @Test
    void testApp_OnExampleFile_MissingBracket_ShouldFail() throws CmdLineException {
        String testFilePath = "src/test/resources/bad_examples/bracket.java";
        String[] args = {"--file",testFilePath,"--max_path_length","200","--max_path_width","10"};

        assertThrows(ParseProblemException.class,() -> App.main(args) );
    }


    @Test
    void testApp_emptyArgs_shouldFail(){
        String[] emptyArgs = {};

        assertThrows(CmdLineException.class,() -> App.main(emptyArgs) );
    }

}
