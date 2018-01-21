package folder;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * When an Application subclass is present in the archive, if both
 * Application.getClasses and Application.getSingletons return an empty
 * collection then all root resource classes and providers packaged in the web
 * application MUST be included and the JAX-RS implementation is REQUIRED to
 * discover them automatically by scanning a .war file as described above. If
 * either getClasses or getSingletons returns a non-empty collection then only
 * those classes or singletons returned MUST be included in the published JAX-RS
 * application.
 */
@ApplicationPath("/api")
public class MyApplication extends Application {

    /**
     * The default lifecycle for resource class instances is per-request. The
     * default lifecycle for providers is singleton.
     */
    @Override
    public Set<Class<?>> getClasses() {
        return new HashSet<Class<?>>() {
            {
                add(MyRegisteredResource.class);
            }
        };
    }
}
