package models



import scala.concurrent.{ExecutionContext, Future}

case class Post(postID: Long, text: String, userID: Long)
case class PostFromJSON(text: String, userID: Long)
case class deletePostJSON(postID: Long,userID: Long)

