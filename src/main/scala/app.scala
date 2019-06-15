package main.scala

import akka.actor.{ ActorSystem, Props, Actor}
import akka.actor.actorRef2Scala
import akka.actor.ActorLogging
import com.typesafe.scalalogging.LazyLogging

// 나의 액터 클래스
class Hello(val hello: String) extends Actor with ActorLogging{

  def receive = {
    case `hello` => // 받은 메세지가 'hello' 라면
      log.info(s"Received a $hello!")
    case msg => // 받은 메세지가 아무것이라면
      log.info(s"Unexpected message '$msg'")
      context.stop(self)
  }
}

object Application extends App with LazyLogging {

  override def main(args: Array[String]) {
    
    logger.info("starting")
    
    // 액터 시스템 생성 . 이름은 mysystem
    val mySystem = ActorSystem("mysystem")
    
    // 액터 시스템을 통하여 나의 Actor 생성. 이름은 greeter 타입은 ActorRef
    val hiActor = mySystem.actorOf(Props(new Hello("hi")).withDispatcher("my-blocking-dispatcher"), "greeter")

    // 나의 액터와 교신. 'hi' 라는 메세지를 보낸다. // Received a hi! 출력
    hiActor ! "hi"
    hiActor ! 3
    
    sys.ShutdownHookThread {
      logger.info("exiting")
      // 액터 시스템 종료  
       mySystem.terminate()
    }
  }
}
