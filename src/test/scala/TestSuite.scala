import zio._
import zio.test.Assertion.equalTo
import zio.test._

object TestSuite extends DefaultRunnableSpec {
  override def spec: ZSpec[environment.TestEnvironment, Any] =
    suite("Test Suite")(
      test("Test 1") {
        assertTrue(1 == 1)
      },
      testM("Test 2") {
        assertM(Task("ok").foldM(ex => ZIO.succeed(ex.getMessage), _ => ZIO.succeed("ok")))(equalTo("ok"))
      }
    ) @@ TestAspect.sequential
}
