import akka.actor._
import akka.testkit.{TestProbe, TestKit}
import org.scalatest.{Suites, BeforeAndAfter, BeforeAndAfterAll, FlatSpecLike}
import scala.concurrent.duration._
import com.hw3.Patterns.Messages.{Hello, MyMessage, WakeUp}
import org.scalatest.FunSuite
import com.hw3.Utils._

/**
  * Created by krbalmryde on 10/30/16.
  */



class TestProbesTestSuites extends Suites(new TestProbesTest)

class TestProbesTest extends TestKit(ActorSystem("TestProbesTestSystem")) with FlatSpecLike with BeforeAndAfterAll with BeforeAndAfter {
    override def afterAll: Unit = {
        TestKit.shutdownActorSystem(system)
    }

    "A Simple Test" should "test TestProbes Greeting each other" in {
        val actorRef = system.actorOf(Props[TestActor], "TestActor")
        val tester1 = TestProbe()
        val tester2 = TestProbe()

        Thread.sleep(500.milliseconds.toMillis)

        actorRef ! (tester1.ref, tester2.ref)
        within(800.milliseconds, 900800.milliseconds) {
            tester1.expectMsg(WakeUp)
            tester2.expectMsg(Hello)
        }
    }


    "A CloneTest" should "test CloneProcess" in {

    }
}

class TestActor extends Actor with ActorLogging {
    override def receive: Receive = {
        case (actorRef1: ActorRef, actorRef2: ActorRef) => {
            // When you change the schedule time in the next line to 100.milliseconds the test fails
            context.system.scheduler.scheduleOnce(400.milliseconds, actorRef1,  WakeUp)(context.system.dispatcher)
            context.system.scheduler.scheduleOnce(800.milliseconds, actorRef2,  Hello)(context.system.dispatcher)
        }
        case x => log.warning(x.toString)
    }
}