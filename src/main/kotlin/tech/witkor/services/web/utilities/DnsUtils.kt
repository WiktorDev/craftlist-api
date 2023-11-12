package tech.witkor.services.web.utilities

import org.xbill.DNS.Lookup
import org.xbill.DNS.Type

fun isValidRecord(domain: String, value: String): Boolean {
    val records = Lookup(domain, Type.TXT).run() ?: return false
    records.forEach {
        if (it.rdataToString().contains(value)) return true
    }
    return false
}