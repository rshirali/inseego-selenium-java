package com.demo.core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Core utility for creating/stopping a thread-local WebDriver.
 */
public final class DriverFactory {

    private static final Logger log = LoggerFactory.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> TL = new ThreadLocal<>();

    /** Utility class: no instances. */
    private DriverFactory() {
        super(); // make the implicit 'super()' explicit to satisfy the inspection
        throw new AssertionError("No com.demo.core.DriverFactory instances");
    }

    /**
     * Returns the current thread's driver (may be {@code null} if not started).
     * @return the thread-local {@link WebDriver}, or {@code null}
     */
    public static WebDriver getDriver() {
        WebDriver d = TL.get();
        log.debug("getDriver() -> {}", d);
        return d;
    }

    /**
     * Start a WebDriver for the given browser if one isn't already running.
     * @param browser  e.g. "chrome" (default if null/blank)
     * @param headless true to run headless
     */
    public static void start(final String browser, final boolean headless) {
        if (TL.get() != null) return;

        final String br = (browser == null || browser.isBlank()) ? "chrome" : browser.toLowerCase();
        log.info("Starting WebDriver: browser={}, headless={}", br, headless);

        switch (br) {
            case "chrome" -> {
                WebDriverManager.chromedriver().setup();
                final ChromeOptions opts = new ChromeOptions();
                if (headless) opts.addArguments("--headless=new", "--window-size=1920,1080");
                opts.addArguments("--no-sandbox", "--disable-dev-shm-usage");
                TL.set(new ChromeDriver(opts));
            }
            default -> throw new IllegalArgumentException("Unsupported browser: " + br);
        }

        WebDriver d = TL.get();
        d.manage().window().maximize();
    }

    /** Quit and dispose the current thread's driver. Safe to call multiple times. */
    public static void stop() {
        WebDriver d = TL.get();
        if (d != null) {
            try {
                log.info("Stopping WebDriver");
                d.quit();
            } finally {
                TL.remove();
            }
        }
    }
}
