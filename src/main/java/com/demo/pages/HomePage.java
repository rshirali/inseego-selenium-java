package com.demo.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Home page object tailored for https://inseego.com
 */
public class HomePage {
    private static final Logger log = LogManager.getLogger(HomePage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Conservative probes
    private final By header = By.cssSelector("header, [role='banner']");
    private final By footer = By.cssSelector("footer, .MainSiteFooter");

    // OneTrust cookie banner (site-specific id)
    private final By cookieAccept = By.cssSelector("#onetrust-accept-btn-handler");

    // Promo dialog bits (close affordances only)
    private final By promoContainer = By.cssSelector("div.dialogContents");
    private final By promoCloseCandidates = By.cssSelector(
            "div.dialogContents button[aria-label*='close' i], " +
                    "div.dialogContents [role='button'][aria-label*='close' i], " +
                    "div.dialogContents button.close, " +
                    "div.dialogContents .close"
    );

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(12));
        log.debug("HomePage initialized");
    }

    /* ================= high-level actions ================= */

    /**
     * Open the site. Falls back to https://inseego.com if input is null/blank and
     * auto-prefixes https:// if caller passed a schemeless host.
     */
    public void open(String baseUrl) {
        String url = sanitizeBaseUrl(baseUrl);
        log.info("Opening {}", url);
        driver.get(url);
        // basic readiness
        wait.until(d -> !d.getTitle().isBlank());
    }

    public void acceptCookiesIfPresent() {
        try {
            List<WebElement> btns = driver.findElements(cookieAccept);
            if (!btns.isEmpty() && btns.get(0).isDisplayed()) {
                btns.get(0).click();
                log.info("Cookie banner accepted.");
                sleep(500);
            } else {
                log.info("No cookie banner present.");
            }
        } catch (WebDriverException e) {
            log.info("Cookie banner not clickable / already gone: {}", e.getClass().getSimpleName());
        }
    }

    public boolean waitForHeaderVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(header));
            log.info("Header is visible.");
            return true;
        } catch (TimeoutException te) {
            log.info("Header not visible within timeout.");
            return false;
        }
    }

    /**
     * Slowly scrolls toward the footer. After EACH micro-scroll, tries to
     * dismiss the promo dialog if it appeared. Ends by attempting a direct
     * scrollIntoView of the footer.
     */
    public void slowScrollTowardFooterAndHandlePromo() {
        final int steps = 22;           // readable sequence in logs
        final long stepDelayMs = 250L;  // small pause for visual trace

        Dimension size = driver.manage().window().getSize();
        int chunk = Math.max(200, (int) (size.getHeight() * 0.30));

        for (int i = 1; i <= steps; i++) {
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, arguments[0]);", chunk);
            log.debug(" ... scrolled step {}/{}", i, steps);

            // If promo pops while scrolling, try to close it immediately
            if (dismissPromoIfPresent(i)) {
                // After closing, give layout a beat to reflow
                sleep(300);
            }

            sleep(stepDelayMs);

            // If footer is already in viewport, finish early
            if (isFooterInViewport()) {
                log.info("Footer reached during slow scroll (step {}).", i);
                return;
            }
        }

        // Final nudge: direct into view
        try {
            WebElement f = driver.findElement(footer);
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView({block:'center'});", f);
            log.info("Performed final scrollIntoView to footer.");
        } catch (NoSuchElementException nse) {
            log.info("Footer not found in DOM after slow scroll.");
        }
    }

    public boolean waitForFooterVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(footer));
            log.info("Footer is visible.");
            return true;
        } catch (TimeoutException te) {
            log.info("Footer not visible within timeout.");
            return false;
        }
    }

    /* ================= helpers ================= */

    private static String sanitizeBaseUrl(String baseUrl) {
        String url = baseUrl == null ? "" : baseUrl.trim();
        if (url.isEmpty()) {
            url = "https://inseego.com";
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }
        return url;
    }

    private boolean dismissPromoIfPresent(int step) {
        try {
            List<WebElement> containers = driver.findElements(promoContainer);
            if (containers.isEmpty()) return false;

            WebElement container = containers.get(0);
            if (!container.isDisplayed()) return false;

            // Try explicit “close” controls only
            List<WebElement> closers = driver.findElements(promoCloseCandidates);
            for (WebElement c : closers) {
                if (c.isDisplayed() && c.isEnabled()) {
                    String tag  = safe(() -> c.getTagName());
                    String aria = safe(() -> c.getAttribute("aria-label"));
                    String cls  = safe(() -> c.getAttribute("class"));

                    c.click();
                    log.info("Promo close clicked: tag={} aria-label={} class={}", tag, aria, cls);
                    sleep(250);

                    // confirm it’s gone
                    if (driver.findElements(promoContainer).isEmpty()
                            || !driver.findElements(promoContainer).get(0).isDisplayed()) {
                        log.info("Promo dialog dismissed during scroll (step {}).", step);
                        return true;
                    }
                }
            }

            // Fallbacks for stubborn dialogs:
            try {
                driver.switchTo().activeElement().sendKeys(Keys.ESCAPE);
                sleep(250);
                if (driver.findElements(promoContainer).isEmpty()
                        || !driver.findElements(promoContainer).get(0).isDisplayed()) {
                    log.info("Promo dismissed via ESC (step {}).", step);
                    return true;
                }
            } catch (Exception ignored) {}

            // Last resort: hide container (demo safety valve)
            ((JavascriptExecutor) driver).executeScript(
                    "const el = document.querySelector('div.dialogContents'); if (el) el.style.display='none';");
            sleep(150);
            log.info("Promo forcibly hidden (step {}).", step);
            return true;

        } catch (StaleElementReferenceException ignored) {
            // transient; treat as handled and proceed
        } catch (WebDriverException e) {
            log.debug("Promo dismiss attempt hit {} (continuing).", e.getClass().getSimpleName());
        }
        return false;
    }

    private boolean isFooterInViewport() {
        try {
            WebElement f = driver.findElement(footer);
            Boolean inView = (Boolean) ((JavascriptExecutor) driver).executeScript(
                    "const r = arguments[0].getBoundingClientRect();" +
                            "return r.top >= 0 && r.bottom <= (window.innerHeight || document.documentElement.clientHeight);",
                    f);
            return Boolean.TRUE.equals(inView);
        } catch (NoSuchElementException nse) {
            return false;
        }
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }
    }

    private static String safe(java.util.concurrent.Callable<String> getter) {
        try { return getter.call(); } catch (Exception ignored) { return null; }
    }
}
