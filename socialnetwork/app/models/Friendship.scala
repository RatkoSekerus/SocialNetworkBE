package models
import scala.concurrent.{ExecutionContext, Future}

case class Friendships(userSenderID: Long, userReceiverID: Long, status: String)
case class FriendshipFromJSON(userSenderID: Long, userReceiverID: Long)
