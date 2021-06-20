package models



import scala.concurrent.{ExecutionContext, Future}

case class Likes(likeID: Long, userID: Long, postID: Long)
case class LikeFromJSON(userID: Long, postID: Long)
case class DislikeFromJSON(userID: Long, postID: Long)

