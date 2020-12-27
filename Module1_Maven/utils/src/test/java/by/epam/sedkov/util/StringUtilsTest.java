package by.epam.sedkov.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StringUtilsTest {

    @Test
    public void isPositiveNumberTrue() {
        boolean actual = StringUtils.isPositiveNumber("55");
        assertTrue(actual);
    }

    @Test
    public void isPositiveNumberFalse() {
        boolean actual = StringUtils.isPositiveNumber("-55");
        assertFalse(actual);
    }

}