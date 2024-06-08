package  dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.fixture.client


interface Client {
    /**
     * @param version api version (v1)
     * @param path resource path (ad/create)
     * @param request message body
     * @return response body
     */
    suspend fun sendAndReceive(version: String, path: String, request: String): String
}
