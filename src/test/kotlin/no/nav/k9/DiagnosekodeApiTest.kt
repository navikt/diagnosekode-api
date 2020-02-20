import io.ktor.application.Application
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import no.nav.k9.DiagnosekodeApi
import org.junit.Test
import kotlin.test.assertEquals

class DiagnosekodeApiTest {
    @Test
    fun testRequests() = withTestApplication(Application::DiagnosekodeApi) {
        with(handleRequest(HttpMethod.Get, "/diagnosekoder?query=luftv&max=")) {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }
}
