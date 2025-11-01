package com.demo.tests;

import com.demo.core.DriverFactory;
import com.demo.pages.HomePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.*;

public class HeaderFooterSmokeTest {
    private static final Logger log = LogManager.getLogger(HeaderFooterSmokeTest.class);

    @BeforeClass
    public void setUp() {
        final String browser  = System.getProperty("browser", "chrome");
        final boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        final String baseUrl = System.getProperty("baseUrl", "https://inseego.com");
        log.info("==== Test setup: browser={}, headless={}, baseUrl={} ====", browser, headless, baseUrl);

        DriverFactory.start(browser, headless);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        log.info("==== Test teardown ====");
        DriverFactory.stop();
    }

    @Test
    public void headerFooter_flow_in_order() {
        final String baseUrl = System.getProperty("baseUrl", "https://inseego.com");
        HomePage home = new HomePage(DriverFactory.getDriver());

        log.info("STEP 1: Land on {}", baseUrl);
        home.open(baseUrl);
        pause(400);

        log.info("STEP 2: Accept cookie banner (if present)");
        home.acceptCookiesIfPresent();
        pause(400);

        log.info("STEP 3: Verify HEADER is visible");
        Assert.assertTrue(home.waitForHeaderVisible(), "Header should be visible after landing.");
        pause(400);

        log.info("STEP 4: Slow scroll toward FOOTER (promo dialog may appear)");
        home.slowScrollTowardFooterAndHandlePromo();
        pause(400);

        log.info("STEP 5: Verify FOOTER is visible");
        Assert.assertTrue(home.waitForFooterVisible(), "Footer should be visible after scrolling.");
    }

    private static void pause(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }
    }
}
