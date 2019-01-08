package catt.mvp.sample.base.model.network.component

import android.util.Log
import android.util.Log.e
import catt.mvp.sample.BuildConfig
import catt.mvp.sample.base.function.component.generatedArrayTypeClass
import catt.mvp.sample.base.function.component.generatedTypeClass
import catt.mvp.sample.base.model.network.base.OkRft
import catt.mvp.sample.base.model.network.callback.ICallResult
import catt.mvp.sample.base.model.network.resopnse.JsonTargetDataField
import catt.mvp.sample.base.model.network.resopnse.JsonField
import catt.mvp.sample.base.model.network.throwables.ResponseBodyException
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

private val callTemplMap:Map<String, String> by lazy { mutableMapOf(
    "code" to "code",
    "timestamp" to "timestamp",
    "msg" to "msg",
    "data" to "data"
) }

fun <B> Call<ResponseBody>.callResponseForArray(result: ICallResult<Array<B>>) {
    enqueue(object : retrofit2.Callback<ResponseBody> {
        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            var code = -1
            var timestamp = ""
            var msg = ""
            try {
                response.body() ?: throw ResponseBodyException("Response callback is null.")
                result.onBeforeResponse()
                val jsonStr: String = response.body()!!.string()
                if (BuildConfig.DEBUG) {
                    Log.d("Http:httpUrl", "Call Url=${call.request().url()}, content=$jsonStr")
                }
                val clazz = result::class.java.generatedArrayTypeClass<B>()
                val root = JsonParser().parse(jsonStr).asJsonObject


                val dataElement: JsonElement = clazz.getJsonAnnotation(JsonField::class.java).run json@{
                    code = root.getAsJsonPrimitive(this@json.code).asInt
                    if (root.has(this@json.msg)) {
                        msg = root.getAsJsonPrimitive(this@json.msg).asString
                    }
                    timestamp = root.getAsJsonPrimitive(this@json.timestamp).asString
                    root.get(this@json.data)
                }

                if (code == 1) {
                    if(dataElement.isJsonNull){
                        throw JsonParseException("json field must not be null")
                    }
                    val targetDataElement = getJsonDataElement(clazz, dataElement)
                    if(targetDataElement.isJsonNull){
                        throw JsonParseException("json field must not be null")
                    }
                    if(!targetDataElement.isJsonArray){
                        throw JsonParseException("json field must be JsonArray")
                    }
                    val jsonArray = targetDataElement.asJsonArray
                    val list = arrayListOf<B>()
                    for (index: Int in 0 until jsonArray.size()) {
                        list.add(OkRft.gson.fromJson(jsonArray[index], clazz))
                    }
                    result.onResponse(
                        list.toArray(java.lang.reflect.Array.newInstance(clazz, list.size) as Array<B>)
                    )
                } else {
                    throw ResponseBodyException(msg)
                }
            }
            catch (ex: ResponseBodyException){
                ex.printStackTrace()
                    onFailure(code, call, result, ex)
            }
            catch (ex: Exception) {
                ex.printStackTrace()
                    onFailure(-1, call, result, ex)
            }
        }

        override fun onFailure(call: Call<ResponseBody>, t: Throwable) =
            onFailure(-1, call, result, t)
    })
}

fun <B> Call<ResponseBody>.callResponseForObject(result: ICallResult<B>) {
    enqueue(object : retrofit2.Callback<ResponseBody> {
        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            var code = -1
            var timestamp = ""
            var msg = ""
            try {
                response.body() ?: throw ResponseBodyException("Response callback is null.")
                result.onBeforeResponse()
                val jsonStr: String = response.body()!!.string()
                if(BuildConfig.DEBUG){
                    Log.d("Http:httpUrl", "Call Url=${call.request().url()}, content=$jsonStr")
                }
                val clazz: Class<B> = result::class.java.generatedTypeClass()
                val root = JsonParser().parse(jsonStr).asJsonObject
                val dataElement = clazz.getJsonAnnotation(JsonField::class.java).run json@{
                    code = root.getAsJsonPrimitive(this@json.code).asInt
                    if (root.has(this@json.msg)) {
                        msg = root.getAsJsonPrimitive(this@json.msg).asString
                    }
                    timestamp = root.getAsJsonPrimitive(this@json.timestamp).asString
                    root.get(this@json.data)
                }
                if (code == 1) {
                    if(dataElement.isJsonNull){
                        throw JsonParseException("json field must not be null")
                    }
                    val targetDataElement = getJsonDataElement(clazz, dataElement)
                    if(targetDataElement.isJsonNull){
                        throw JsonParseException("json field must not be null")
                    }
                    if(!dataElement.isJsonObject){
                        throw JsonParseException("Json field must be JsonObject")
                    }
                    result.onResponse(OkRft.gson.fromJson(getJsonDataElement(clazz, root), clazz))
                }
                else throw ResponseBodyException(msg)
            }
            catch (ex: ResponseBodyException){
                ex.printStackTrace()
                onFailure(code, call, result, ex)
            }
            catch (ex: Exception) {
                ex.printStackTrace()
                onFailure(-1, call, result, ex)
            }
        }

        override fun onFailure(call: Call<ResponseBody>, t: Throwable) =
            onFailure(-1, call, result, t)
    })
}


fun <B> Call<ResponseBody>.callResponseForArray(result: ICallResult<Array<B>>, coroutine: CoroutineScope) {
    enqueue(object : retrofit2.Callback<ResponseBody> {
        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            coroutine.launch(Dispatchers.IO){
                var code = -1
                var timestamp = ""
                var msg = ""
                try {
                    response.body() ?: throw ResponseBodyException("Response callback is null.")
                    withContext(Dispatchers.Main){ result.onBeforeResponse() }
                    val jsonStr: String = response.body()!!.string()
                    if (BuildConfig.DEBUG) {
                        Log.d("Http:httpUrl", "Call Url=${call.request().url()}, content=$jsonStr")
                    }
                    val clazz = result::class.java.generatedArrayTypeClass<B>()
                    val jsonFields = clazz.getJsonAnnotation(JsonField::class.java)
                    val root = JsonParser().parse(jsonStr).asJsonObject

                    val dataElement = clazz.getJsonAnnotation(JsonField::class.java).run json@{
                        code = root.getAsJsonPrimitive(this@json.code).asInt
                        if (root.has(this@json.msg)) {
                            msg = root.getAsJsonPrimitive(this@json.msg).asString
                        }
                        timestamp = root.getAsJsonPrimitive(this@json.timestamp).asString
                        root.get(this@json.data)
                    }
                    if (code == 1) {
                        if(dataElement.isJsonNull){
                            throw JsonParseException("json field must not be null")
                        }
                        val targetDataElement = getJsonDataElement(clazz, dataElement)
                        if(targetDataElement.isJsonNull){
                            throw JsonParseException("json field must not be null")
                        }
                        if(!targetDataElement.isJsonArray){
                            throw JsonParseException("json field must be JsonArray")
                        }
                        val jsonArray = targetDataElement.asJsonArray

                        val list = arrayListOf<B>()
                        for (index: Int in 0 until jsonArray.size()) {
                            list.add(OkRft.gson.fromJson(jsonArray[index], clazz))
                        }
                        withContext(Dispatchers.Main){
                            result.onResponse(
                                list.toArray(java.lang.reflect.Array.newInstance(clazz, list.size) as Array<B>)
                            )
                        }
                    } else {
                        throw ResponseBodyException(msg)
                    }
                }
                catch (ex: ResponseBodyException){
                    ex.printStackTrace()
                    withContext(Dispatchers.Main){
                        onFailure(code, call, result, ex)
                    }
                }
                catch (ex: Exception) {
                    ex.printStackTrace()
                    withContext(Dispatchers.Main){
                        onFailure(-1, call, result, ex)
                    }
                }
            }
        }

        override fun onFailure(call: Call<ResponseBody>, t: Throwable) =
            onFailure(-1, call, result, t)
    })
}

fun <B> Call<ResponseBody>.callResponseForObject(result: ICallResult<B>, coroutine: CoroutineScope) {
    enqueue(object : retrofit2.Callback<ResponseBody> {
        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            coroutine.launch (Dispatchers.IO){
                var code = -1
                var timestamp = ""
                var msg = ""
                try {
                    response.body() ?: throw ResponseBodyException("Response callback is null.")
                    withContext(Dispatchers.Main){ result.onBeforeResponse() }
                    val jsonStr: String = response.body()!!.string()
                    if(BuildConfig.DEBUG){
                        Log.d("Http:httpUrl", "Call Url=${call.request().url()}, content=$jsonStr")
                    }
                    val clazz: Class<B> = result::class.java.generatedTypeClass()
                    val root = JsonParser().parse(jsonStr).asJsonObject
                    val dataElement = clazz.getJsonAnnotation(JsonField::class.java).run json@{
                        code = root.getAsJsonPrimitive(this@json.code).asInt
                        if (root.has(this@json.msg)) {
                            msg = root.getAsJsonPrimitive(this@json.msg).asString
                        }
                        timestamp = root.getAsJsonPrimitive(this@json.timestamp).asString
                        root.get(this@json.data)
                    }
                    if (code == 1) {
                        if(dataElement.isJsonNull){
                            throw JsonParseException("json field must not be null")
                        }
                        val targetDataElement = getJsonDataElement(clazz, dataElement)
                        if(targetDataElement.isJsonNull){
                            throw JsonParseException("json field must not be null")
                        }
                        if(!dataElement.isJsonObject){
                            throw JsonParseException("Json field must be JsonObject")
                        }
                        val fromJson = OkRft.gson.fromJson(dataElement, clazz)
                        withContext(Dispatchers.Main){
                            result.onResponse(fromJson)
                        }
                    }
                    else throw ResponseBodyException(msg)
                }
                catch (ex: ResponseBodyException){
                    ex.printStackTrace()
                    withContext(Dispatchers.Main) {
                        onFailure(code, call, result, ex)
                    }
                }
                catch (ex: Exception) {
                    ex.printStackTrace()
                    withContext(Dispatchers.Main) {
                        onFailure(-1, call, result, ex)
                    }
                }
            }
        }

        override fun onFailure(call: Call<ResponseBody>, t: Throwable) =
            onFailure(-1, call, result, t)
    })
}


private fun getJsonDataElement(clazz: Class<*>, root:JsonElement):JsonElement{
    val dataAnn = clazz.getJsonAnnotation(JsonTargetDataField::class.java)
    var element = root
    val split = dataAnn.hierarchy.split("/")
    for(index: Int in split.indices){
        if(element.isJsonNull){
            throw JsonParseException("hierarchy json field must not be null")
        }
        if(element.isJsonObject){
            if(element.asJsonObject.has(split[index])){
                element = element.asJsonObject.get(split[index])
            }
        }
        else {
            throw JsonParseException("hierarchy json field must be JsonObject")
        }
    }
    return element
}


@Throws(NullPointerException::class, IllegalArgumentException::class)
private fun <T, A : Annotation> Class<T>.getJsonAnnotation(annotation: Class<A>): A {
    return (when (this.isArray) {
        true -> {
            val array = this as Array<*>
            if (array.isEmpty()) throw IllegalArgumentException("Array is empty.")
            val any = array[0]!!::class.java
            any::class.java.getAnnotation(annotation)
        }
        false -> this.getAnnotation(annotation)
    }) ?: throw NullPointerException("Need JsonField annotation.")
}

private fun <T> onFailure(code:Int, call: Call<ResponseBody>, result: ICallResult<T>, t: Throwable) {
    try {
        result.onFailure2(code, t)
        result.onFailure(t)
    } finally {
        if(BuildConfig.DEBUG){
            Log.w("Http:onFailure", "Call_____$call")
        }
        result.onAfterFailure(code, call, t)
    }
}