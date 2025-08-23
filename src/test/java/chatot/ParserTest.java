package chatot;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    @Test
    public void testParseValidCommandWithArguments() {
        Command result = Parser.parse("todo read book");
        assertEquals(CommandType.TODO, result.getType());
        assertEquals("read book", result.getArguments());
    }

    @Test
    public void testParseValidCommandWithoutArguments() {
        Command result = Parser.parse("list");
        assertEquals(CommandType.LIST, result.getType());
        assertEquals("", result.getArguments());
    }

    @Test
    public void testParseUnknownCommand() {
        Command result = Parser.parse("invalidcommand");
        assertEquals(CommandType.UNKNOWN, result.getType());
        assertEquals("", result.getArguments());
    }

    @Test
    public void testParseComplexCommandWithMultipleSpaces() {
        Command result = Parser.parse("deadline submit assignment /by 2024-12-01");
        assertEquals(CommandType.DEADLINE, result.getType());
        assertEquals("submit assignment /by 2024-12-01", result.getArguments());
    }
}