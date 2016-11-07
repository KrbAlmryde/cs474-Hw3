package com.hw3.Patterns

import akka.actor.Status.Success
import akka.http.scaladsl.model.HttpResponse
import com.hw3.Patterns.Messages.CloneRepo
import org.json4s.JsonAST.JValue

import scala.concurrent.Future

/**
  * Created by krbalmryde on 11/5/16.
  * The following are a series of Messages which will be passed
  * back and forth between Actors
  */
// Greetings
object Messages {

    case object Empty
    case object Hello
    case object SayHello

    // Languages
    case object Java
    case object Python

    /**  REQUESTS **/
    // Github API 'Request' patterns
    case class Search(lang:String)  // Searches for Repositories containing the given language
    case class Content(fullName:String)  // Requests Content of a given Repository and Owner (mostly to get the SHA)

    case class JsonResult(json:JValue)
    case class CloneResult(success:Int)
    case class UDBResult(success:Int)
    case class FinalOutput(result:String)  // Should be used

    // Placeholder
    case class Foo(id:String, name:String, language:String, foo:String)

    // When Its time to generate the .UDB file, send this message
    case class DepGraph(id:String, name:String)
    case class UDB(id:String, name:String, language:String)
    case class RepoDetails(id:String, name:String, url:String, lang:String) // Simple example

    // In order to clone a Repo, send this request message
    case class CloneRepo(id:String, name:String, url:String)
    case class CleanRepo(id:String, name:String)


    /**  RESPONSE **/
    case class SHA(sha:String)


    /**  GENERAL **/
    case class Done(m:String)
    case class MyMessage( m:String )
    case class Language(name:String)
    case class Greeting(from:String)
    case class Received(from:String)

    /**  MISC **/
    case class Single(x:JValue)
    case class Double(x:JValue, y:JValue)
    case class Composed(x:JValue, y:JValue, z:JValue)




}
