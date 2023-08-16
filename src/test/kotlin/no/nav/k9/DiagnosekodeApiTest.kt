import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.testing.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import no.nav.k9.DiagnosekodeApi
import org.junit.Test

class DiagnosekodeApiTest {

    @Test
    fun testRequests() = withTestApplication(Application::DiagnosekodeApi) {
        with(handleRequest(HttpMethod.Get, "/diagnosekoder?query=E614&max=")) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertTrue(response.contentType().match("application/json"))
            assertEquals("""[{"kode":"E614","beskrivelse":"Krommangel"}]""", response.content)
        }
    }
}
