package daggerok

import com.codeborne.selenide.Condition.exist
import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Selenide.`$`
import com.codeborne.selenide.Selenide.open
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.openqa.selenium.By
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class `App Tests`(@LocalServerPort val port: Int) {

  companion object {
    private val log = LoggerFactory.getLogger(`App Tests`::class.java)
    private const val delay = 5_000L

    @BeforeAll @JvmStatic fun info() {
      val key = "selenide.browser"
      val browser = System.getProperty(key, "chrome") // from IDEA we wanna chrome, or use runConfigurations
      val debugTestMessage = "execution e2e tests in $browser browser"
      System.setProperty(key, browser)
      log.debug(debugTestMessage)
      println(debugTestMessage)
    }
  }

  @BeforeEach
  fun `before each test setup`() {
    // apply and populate test data needed for each test...
  }

  @Test
  fun `documentation page`() {

    open("http://127.0.0.1:$port/")

    `$`(By.id("header"))
        .should(exist)
        .shouldHave(text("My super awesome app"))

    `$`(By.id("content"))
        .should(exist)
        .shouldHave(text("User Guide"))
        .shouldHave(text("Administration"))
        .shouldHave(text("Installation Guide"))
        .`$$`(By.cssSelector("#content .sect1"))
          .shouldHaveSize(2)

    `$`(By.id("footer"))
        .should(exist)
        .`$`(By.id("footer-text"))
          .should(exist)
          .shouldHave(text("Version"))
          .shouldHave(text("Last updated"))
  }
}
