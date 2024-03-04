package by.clevertec.commentsproject.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constant {

    @UtilityClass
    public class BaseApi {

        public static final String ID = "/{id}";
        public static final String HTTP_LOCALHOST_8081 = "http://localhost:8081";
        public static final String NEWS_ID = "/news/{id}";
        public static final String COMMENTS = "/comments";
        public static final String NEWS_NEWS_ID = "/news/{newsId}";

    }

    @UtilityClass
    public class Messages {

        public static final String TEXT_SHOULD_BE_MINIMUM_5_SYMBOLS = "Text should be minimum 5 symbols";
        public static final String TITLE_CANNOT_BE_BLANK = "Title cannot be blank";
        public static final String TEXT_CANNOT_BE_BLANK = "Text cannot be blank";

    }

    @UtilityClass
    public class Atrubutes {

        public static final String NEWS = "news";
        public static final String COMMENT = "comment";
    }

}
