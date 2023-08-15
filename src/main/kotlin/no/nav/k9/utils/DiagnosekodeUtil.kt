package no.nav.k9.utils

import no.nav.k9.domain.ICD10Diagnosekode
import no.nav.helse.diagnosekoder.Diagnosekoder

object DiagnosekodeUtil {
    fun transformValues(icd10: Map<String, Diagnosekoder.ICD10>): Map<String, ICD10Diagnosekode> {
        val optimizedDiagnosekodeMap = mutableMapOf<String, ICD10Diagnosekode>()
        icd10.forEach { _, value ->
            val newKey = "${value.code}::${value.text}".lowercase()
            optimizedDiagnosekodeMap[newKey] = ICD10Diagnosekode(value.code, value.text)
        }
        return optimizedDiagnosekodeMap
    }
}
