package filters;

import play.filters.cors.CORSFilter;
import play.http.HttpFilters;
import play.mvc.EssentialAction;
import play.mvc.EssentialFilter;

import javax.inject.Inject;

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

}
