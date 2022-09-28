import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.testing.*
import junit.framework.TestCase.assertEquals
import no.nav.k9.DiagnosekodeApi
import org.junit.Test

class DiagnosekodeApiTest {

    @Test
    fun testRequests() = withTestApplication(Application::DiagnosekodeApi) {
        with(handleRequest(HttpMethod.Get, "/diagnosekoder?query=luftv&max=")) {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }
}
