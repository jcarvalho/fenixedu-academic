alter table `REMOTE_HOST` add `NAME` text;
UPDATE REMOTE_HOST SET URL = CONCAT(CHAR_LENGTH(URL),":",URL);