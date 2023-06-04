package com.practical.assignment.util;

import com.practical.assignment.constants.Constants;
import com.practical.assignment.listeners.TestListener;
import io.restassured.http.Headers;
import net.datafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.asserts.SoftAssert;

import java.util.TreeMap;

@Listeners({TestListener.class})
public abstract class TestBase {

    protected static TreeMap<String, String> testData = null;
    protected static final String ENVIRONMENT;
    protected static final Faker faker;
    protected static Headers headers;
    protected static final Logger logger = LogManager.getLogger(TestBase.class.getName());
    protected static SoftAssert softAssert;

    static {
        Config.setConfigFilePath(Constants.DEFAULT_CONFIG_LOCATION);
        Configurator.initialize("MyApp", Config.getProperty("logFileOutputPath"));
        ENVIRONMENT = (System.getProperty("environment") == null)? Config.getProperty("environment"): System.getProperty("environment");
        faker = new Faker();
    }

    @BeforeSuite
    public void startUp() throws Exception {

        try {
            String path = "/testdata/" + ENVIRONMENT.toLowerCase() + "_data.properties";
            logger.info("Load the properties file " + path);
            testData = new LoadDataProperties().getWebDataMapping(path);

        } catch (Exception e) {
            throw new Exception("Failed : startUp()" + e.getLocalizedMessage());
        }
    }

    @BeforeMethod
    public void beforeMethod() throws Exception {

        try {
            softAssert = new SoftAssert();

        } catch (Exception e) {
            throw new Exception("Failed : beforeMethod()" + e.getLocalizedMessage());
        }
    }

}
