# JUnit Platform OpenTelemetry Tracing
![Build](https://github.com/ryandens/junit-platform-otel/workflows/Build/badge.svg?branch=main)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.ryandens/junit-platform-otel-api/badge.svg#)](https://maven-badges.herokuapp.com/maven-central/com.ryandens/junit-platform-otel-api)

Implementations of JUnit Platform extension points for tracing of JUnit test suites with OpenTelemetry


## Inspirations
- [rakyll/go-test-trace](https://github.com/rakyll/go-test-trace)
- [junit-team/junit5/junit-platform-jfr](https://github.com/junit-team/junit5/tree/main/junit-platform-jfr)


## Usage


### Auto registration
To automatically register the JUnit Platform `LauncherDiscoveryListener` and `TestExecutionListener` created by this 
project, simply add the `junit-platform-otel-auto` module as a runtime dependency of your test suite:

```kotlin
dependencies {
  testRuntimeOnly("com.ryandens:junit-platform-otel-auto:0.1.0")
}
```

To configure the OpenTelemetry SDK used by this module, refer to the [OpenTelemetry SDK AutoConfigure Extension 
documentation](https://github.com/open-telemetry/opentelemetry-java/tree/main/sdk-extensions/autoconfigure).

By default, this module expects an OpenTelemetry collector to be listening on `localhost:4317`, but this can be easily
configured with the `OTEL_EXPORTER_OTLP_ENDPOINT` environment variable read by the OpenTelemetry AutoConfigure 
Extension. See [Running the example](#running-the-example) for easy instructions to run a collector locally.


### Manual registration
If you would like to manually instantiate the JUnit platform extension points, you must add the 
`junit-platform-otel-api` module as a runtime dependency of your test suite:

```kotlin
dependencies {
  testImplementation("com.ryandens:junit-platform-otel-api:0.1.0")
}
```

Then, follow directions for manual registration of extension points via the 
[JUnit Platform Launcher API](https://junit.org/junit5/docs/current/user-guide/#launcher-api-launcher-config).

## Running the example

This project includes an example test suite for you to try it out and see how it 
works. This test suite expects an OpenTelemetry Collector to be listening for OLTP
GRPC traces on `localhost:4317`. Collectors are responsible for running "near" instrumented
code, receiving and processing the traces, and exporting them to a human-readable destination.

In order to make this easy, this project ships with two reference collector configurations.

### Export to Honeycomb.io
If you have a honeycomb.io account, use the following command to start an OpenTelemetry collector 
docker container that exports data to Honeycomb. This command expects the `HONEYCOMB_TEAM_KEY`
environment variable to be set on your host. Alternatively, replace `${X_HONEYCOMB_TEAM}` in 
[honeycomb-otel-config.yaml](./honeycomb-otel-config.yaml) with your Honeycomb API key.

```bash
$ docker run --rm -d -p 4317:4317 -v $(pwd)/honeycomb-otel-config.yaml:/etc/otel/config.yaml \
  --env X_HONEYCOMB_TEAM=$HONEYCOMB_TEAM_KEY \
  otel/opentelemetry-collector-contrib:latest
```

### Export to logger

If you just want to see what kind of data is generated by the example test suite, use the following
command to start and OpenTelemetry collector docker container that exports data to the container's 
logger.

```bash
$ docker run --rm -d -p 4317:4317 -v $(pwd)/logging-otel-config.yaml:/etc/otel/config.yaml \
  otel/opentelemetry-collector-contrib:latest
```

### Export to a different address
If you have an OpenTelemetry collector listening on a different address, simply specify the 
address using one of the mechanisms exposed by the [OpenTelemetry Java SDK Autoconfigure Extension].

Example:
```bash
OTEL_EXPORTER_OTLP_ENDPOINT=http://my.customm.host:4317 ./gradlew :example:test
```

### 🚀 Releasing

1. Make sure the `sonatypeUsername` and `sonatypePassword` properties are set.
1. Make sure the `signing.keyId`, `signing.password`, and `signing.secretKeyRingFile` properties are set
1. `./gradlew build signNebulaPublication publishNebulaPublicationToSonatypeRepository closeAndReleaseSonatypeStagingRepository`
