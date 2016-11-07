package com.hw3

// My stuff
import com.hw3.Actors._
import com.hw3.Patterns.Messages.Language

// Akka stuff
import akka.actor.{ActorSystem, Props}

//  http stuff
import akka.http.scaladsl.Http

// stream stuff
import akka.stream.ActorMaterializer


/**
  * Created by krbalmryde on 10/27/16.
  */
object Main extends App {

    implicit val system = ActorSystem("HelloSystem")
    implicit val materializer = ActorMaterializer()
    import system.dispatcher

    val masterActor = system.actorOf(Props[MasterActor], name = "master")

    masterActor ! Language("Java")

    //     Sleep for a moment
    Thread.sleep(200000)
    println("Thanks for playing!")
    // Shut the system down
    Http().shutdownAllConnectionPools().onComplete(_ => system.terminate())

//    /**
//      * @param url: "https://api.github.com/search/repositories?q=language:python&sort=stars&order=desc"
//      */
//    def RequestJsonM1(url: String) = {
//        val source = Uri(url)
//        var futureResponse: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = source))  // uri = "https://api.github.com/search/repositories?q=language:python&sort=stars&order=desc"
//        val response = Await.result(futureResponse, 5.seconds)
//        println("The content type is ", response.entity.contentType )
//        response.entity.dataBytes
//                .runFold(ByteString(""))(_ ++ _).map(body => println( JsonParser(body.utf8String).prettyPrint ))
//        //     Sleep for a moment
//            println("Server is running press RETURN to stop...")
//            StdIn.readLine() // let it run until user presses return
//        // Shut the system down
//            Http().shutdownAllConnectionPools().onComplete(_ => system.terminate())
//    }


//    /**
//      * @param host: "api.github.com"
//      * @param url: "https://api.github.com/search/repositories?q=language:python&sort=stars&order=desc"
//    */
//    def RequestJsonM2(host:String, url:String) = {
//
//        val connectionFlow: Flow[HttpRequest, HttpResponse, Future[Http.OutgoingConnection]] = {
//            Http().outgoingConnection(host)
//        }
//
//        val responseFuture: Future[HttpResponse] = {
//            Source.single(HttpRequest(uri = url))
//                    .via(connectionFlow)
//                    .runWith(Sink.head)
//        }
//
//        responseFuture.andThen {
//            case Success(x) => {
//                println("content type is", x.entity.contentType)
//                println(x.entity.dataBytes.runFold(ByteString(""))(_ ++ _).map(b=>JsonParser(b.utf8String).prettyPrint))
//            }
//            case Failure(_) => println("request failed")
//        }.andThen {
//            case _ => system.terminate()
//        }
//
//    }

}


