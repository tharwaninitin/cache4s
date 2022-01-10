import cache4s.Cache
import zio.test.Assertion.equalTo
import zio.test._
import scala.concurrent.duration._

object CacheTestSuite extends DefaultRunnableSpec {

  val cache: Cache[String, String] = Cache.create[String, String]()
  cache.put("key1", "123")
  cache.put("key3", "123", ttl = Some(2.second))

  override def spec: ZSpec[environment.TestEnvironment, Any] =
    suite("Cache Test suite")(
      test("The value stored in the underlying cache should return correctly") {
        val op = cache.get("key1").getOrElse("NA")
        assertTrue(op == "123")
      },
      test("The value not present in the underlying cache should return None") {
        val op = cache.get("key2")
        assert(op)(equalTo(None))
      },
      test("The value stored in the underlying cache should return None after ttl") {
        Thread.sleep(5000)
        val op = cache.get("key3")
        assert(op)(equalTo(None))
      },
      test("ToMap should return the size of underlying cache") {
        val size = cache.toMap.size
        assertTrue(size == 1)
      },
      test("getStats should return the map with detials of underlying cache") {
        val op = cache.getStats
        val expected =
          Map(
            "size"         -> "1",
            "missCount"    -> "2",
            "hitCount"     -> "1",
            "requestCount" -> "3",
            "missRate"     -> "0.6666666666666666",
            "hitRate"      -> "0.3333333333333333"
          )
        assertTrue(op == expected)
      }
    ) @@ TestAspect.sequential
}
