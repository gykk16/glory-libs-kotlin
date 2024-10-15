package io.glory.coremvc.filter.servletwrapperprovider

import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import org.springframework.util.StreamUtils
import java.io.*

class CachedBodyHttpServletRequest(request: HttpServletRequest) : HttpServletRequestWrapper(request) {

    private val cachedBody: ByteArray = StreamUtils.copyToByteArray(request.inputStream)
    private val parameterMap: Map<String, Array<String>> = request.parameterMap

    @Throws(IOException::class)
    override fun getInputStream(): ServletInputStream {
        return CachedBodyServletInputStream(this.cachedBody)
    }

    @Throws(IOException::class)
    override fun getReader(): BufferedReader {
        // Create a reader from cachedContent and return it
        val byteArrayInputStream = ByteArrayInputStream(this.cachedBody)
        return BufferedReader(InputStreamReader(byteArrayInputStream))
    }

    override fun getParameterMap(): Map<String, Array<String>> {
        return this.parameterMap
    }

}