package com.ekkoe.fitty.converter

import com.ekkoe.fitty.api.ApiResponse
import com.ekkoe.fitty.api.CustomHttpException
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


class KotlinConverterFactory : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        return KotlinResponseBodyConverter(serializer = serializer(type))
    }
}

class KotlinResponseBodyConverter<T>(private val serializer: KSerializer<T>) :
    Converter<ResponseBody, T> {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    override fun convert(value: ResponseBody): T? {
        return json.decodeFromString(ApiResponse.serializer(serializer), value.string()).also {
            if (it.errorCode != 0)
                throw CustomHttpException(it.errorCode, it.errorMsg)
        }.data
    }
}