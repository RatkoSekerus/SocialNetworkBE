-- default schema

-- !Ups

CREATE TABLE user (
    userID BIGINT NOT NULL auto_increment,
    firstName TEXT NOT NULL,
    lastName TEXT NOT NULL,
    age BIGINT NOT NULL,
    image TEXT NOT NULL,
    userName TEXT NOT NULL,
    password TEXT NOT NULL,
    PRIMARY KEY (userID)
);

-- !Downs

DROP TABLE user;


-- !Ups

CREATE TABLE post (
    postID BIGINT NOT NULL auto_increment,
    text TEXT NOT NULL,
    userID BIGINT NOT NULL,
    PRIMARY KEY (postID),
    FOREIGN KEY (userID) REFERENCES user(userID)
);
-- !Downs

DROP TABLE post;


-- !Ups
CREATE TABLE likes  (
    likeID BIGINT NOT NULL auto_increment,
    userID BIGINT NOT NULL,
    postID BIGINT NOT NULL,
    PRIMARY KEY (likeID),
    FOREIGN KEY (userID) REFERENCES user(userID),
    FOREIGN KEY (postID) REFERENCES post(postID)
);
-- !Downs

DROP TABLE likes ;

-- !Ups
CREATE TABLE friendships  (
userSenderID BIGINT NOT NULL,
userReceiverID BIGINT NOT NULL,
status TEXT NOT NULL,
FOREIGN KEY (userSenderID) REFERENCES User(userID),
FOREIGN KEY (userReceiverID) REFERENCES User(userID),
PRIMARY KEY (userSenderID, userReceiverID)
);

-- !Downs

DROP TABLE friendships;