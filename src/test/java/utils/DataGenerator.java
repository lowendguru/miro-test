package utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class DataGenerator {

    final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static String getRandomEmail() {
        return RandomStringUtils.randomAlphabetic(2, 10) + "@" + RandomStringUtils.randomAlphabetic(2, 10) + ".com";
    }

    public static String getRandomName() {
        return RandomStringUtils.randomAlphabetic(3, 12);
    }

    public static String getStringOfLength(int length) {
        String random = RandomStringUtils.randomAlphanumeric(length);
        logger.info("Creating String of length " + length + ": " + random);
        return random;
    }

    public static String getRandomValidPassword() {
        return RandomStringUtils.randomAlphabetic(8, 20);
    }

}
