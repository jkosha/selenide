package com.codeborne.selenide.drivercommands;

import com.codeborne.selenide.impl.Cleanup;
import com.codeborne.selenide.proxy.SelenideProxyServer;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CloseBrowser implements Runnable {
  private static final Logger log = LoggerFactory.getLogger(CloseBrowser.class);

  private final WebDriver webdriver;
  private final SelenideProxyServer proxy;

  CloseBrowser(WebDriver webdriver, SelenideProxyServer proxy) {
    this.webdriver = webdriver;
    this.proxy = proxy;
  }

  @Override
  public void run() {
    try {
      log.info("Trying to close the browser " + webdriver.getClass().getSimpleName() + " ...");
      webdriver.quit();
    }
    catch (UnreachableBrowserException e) {
      // It happens for Firefox. It's ok: browser is already closed.
      log.debug("Browser is unreachable", e);
    }
    catch (WebDriverException cannotCloseBrowser) {
      log.error("Cannot close browser normally: {}", Cleanup.of.webdriverExceptionMessage(cannotCloseBrowser));
    }

    if (proxy != null) {
      log.info("Trying to shutdown {} ...", proxy);
      proxy.shutdown();
    }
  }
}
