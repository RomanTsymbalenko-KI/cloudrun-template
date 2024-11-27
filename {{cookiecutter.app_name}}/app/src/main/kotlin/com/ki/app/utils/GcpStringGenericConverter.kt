package com.ki.app.utils

import com.google.protobuf.ByteString
import org.springframework.core.convert.TypeDescriptor
import org.springframework.core.convert.converter.GenericConverter
import kotlin.text.Charsets.UTF_8

class GcpStringGenericConverter : GenericConverter {

    override fun getConvertibleTypes(): MutableSet<GenericConverter.ConvertiblePair> {
        return mutableSetOf(GenericConverter.ConvertiblePair(ByteString::class.java, String::class.java))
    }

    override fun convert(
        source: Any?,
        sourceType: TypeDescriptor,
        targetType: TypeDescriptor
    ): String? {
        return (source as? ByteString)?.toString(UTF_8)
    }
}
