package pl.sggw.support.webservice.populator;

/**
 * Created by Kamil on 2017-10-21.
 */
public abstract class AbstractPopulator<S,T> {

    public abstract void populate(S source, T target);

    public abstract void reversePopulate(T source, S target);
}
