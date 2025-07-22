package com.babestudios.companyinfouk.shared.data.network

import com.babestudios.companyinfouk.shared.data.model.filinghistory.DescriptionValuesDto
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonTransformingSerializer

/**
 * [DescriptionValuesDto]s are normally String to String pairs, but for "capital" we get an unused object.
 * We wipe this out before serialization with this transform.
 */
object DescriptionValueTransformingSerializer : JsonTransformingSerializer<DescriptionValuesDto>(
	DescriptionValuesDto.serializer()
) {

	override fun transformDeserialize(element: JsonElement): JsonElement {
		val mapItems: Map<String, JsonElement> = (element as JsonObject).toMap().mapValues { simplify(it) }
		return JsonObject(mapOf("pairs" to JsonObject(mapItems)))
	}

	private fun simplify(it: Map.Entry<String, JsonElement>): JsonElement {
		return if (it.value is JsonPrimitive) it.value else return JsonPrimitive("")
	}

}
