You need to import the ssl cert for tomcat to work properly. On the localhost

## Runtime deserialization mitigation 🔒

We apply a JVM-level deserialization filter at startup to reduce the risk of Java deserialization vulnerabilities (CVE-2016-1000027).

The filter is set in `devops/setenv.sh` via the `jdk.serialFilter` system property and defaults to allow only the JDK base classes and the `com.esri.geoportal.*` application packages:

```
export JAVA_OPTS="${JAVA_OPTS:-} -Djdk.serialFilter=maxdepth=5;java.base/*;com.esri.geoportal.*;!*"
```

If your application legitimately needs to deserialize other types, update the filter in `devops/setenv.sh` to include required packages and test carefully.

Additional defenses: a CI workflow is added to detect source references to Java deserialization or Spring remoting classes; PRs that introduce potential deserialization endpoints will fail the check and require review.
