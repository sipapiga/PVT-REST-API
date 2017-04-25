package filters;

import org.springframework.beans.factory.annotation.Required;
import play.Logger;
import play.filters.cors.CORSFilter;
import play.http.DefaultHttpFilters;
import play.http.HttpFilters;
import play.mvc.EssentialAction;
import play.mvc.EssentialFilter;

import javax.inject.Inject;
import javax.inject.Singleton;

public class Filters extends DefaultHttpFilters {
    @Inject public Filters(CORSFilter corsFilter, LoggingFilter loggingFilter) {
        //super(corsFilter, loggingFilter);
        super(loggingFilter);
    }
}

/*public class Filters implements HttpFilters {

    @Inject
    CORSFilter corsFilter;

    @Inject
    LoggingFilter loggingFilter;

    public EssentialFilter[] filters() {
        return new EssentialFilter[] {corsFilter.asJava(), loggingFilter.asJava()};
    }
}*/

/*@Singleton
public class Filters extends EssentialFilter implements HttpFilters {

    @Inject
    private CORSFilter corsFilter;

    @Override
    public EssentialAction apply(EssentialAction next) {
        return corsFilter.asJava().apply(next);
    }

    @Override
    public EssentialFilter[] filters() {

        EssentialFilter[] result = new EssentialFilter[1];
        result[0] = this;

        return result;

    }
}*/
