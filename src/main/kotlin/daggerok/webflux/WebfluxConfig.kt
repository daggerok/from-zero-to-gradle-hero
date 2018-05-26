package daggerok.webflux

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.http.MediaType.TEXT_HTML
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.ServerResponse.permanentRedirect
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.toMono
import java.net.URI
// Kotlin Webflux DSL: this is needs to be imported!
// spring-webflux-5.0.6.RELEASE-sources.jar!/org/springframework/web/reactive/function/server/ServerResponseExtensions.kt:32
import org.springframework.web.reactive.function.server.body

@Configuration
class WebfluxConfig(val json: ObjectMapper) {

  @Bean
  fun routes() = router {

    resources("/docs/**", ClassPathResource("public/docs/"))

    ("/").nest {

      contentType(APPLICATION_JSON_UTF8)

      path("/api/**") {

        val requestBody = it.bodyToMono(String::class.java).defaultIfEmpty("")
        val queryParams = it.queryParams()
            .orEmpty()
            .map { json.writeValueAsString(it) }
            .joinToString { ", " }
            .toMono()

        ok().body( // see proper import in line 10!
            requestBody
                .or(queryParams)
                .or("Hello my friend!".toMono())
                .map { mapOf( "message" to json.writeValueAsString(it) ) }
        )
      }

      contentType(TEXT_HTML)

      path("/pdf") {
        permanentRedirect(URI.create("/docs/documentation.pdf")).build()
      }

      val html = permanentRedirect(URI.create("/docs/documentation.html")).build()

      path("/") { html }
      path("/doc") { html }
      path("/docs") { html }
      path("/html") { html }
    }
  }
}
