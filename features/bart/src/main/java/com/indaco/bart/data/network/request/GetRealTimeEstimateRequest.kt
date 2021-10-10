package com.indaco.bart.data.network.request

class GetRealTimeEstimateRequest(
    val plat: String?,
    val orig: String,
    val dir: String?
) : BartRequest() {

    override fun toMap(vararg values: Pair<String, String?>): Map<String, String?> {
        return super.toMap(Pair("plat", plat), Pair("orig",orig), Pair("dir",dir))
    }
}